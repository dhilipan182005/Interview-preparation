import logging
from fastapi import APIRouter, Depends, HTTPException, BackgroundTasks, status
from sqlalchemy.orm import Session
from typing import List

from app.db.session import get_db
from app.models.sos import SOSAlert
from app.models.vehicle import Vehicle
from app.models.user import User
from app.schemas.sos import SOSAlertCreate, SOSAlertResponse
from app.core.security import get_current_user
from app.core.websocket import manager

logger = logging.getLogger(__name__)
router = APIRouter()


def _notify_emergency_services(alert_id: int, vin: str, lat: float, lng: float):
    logger.critical(
        "EMERGENCY DISPATCH: SOS Alert #%d for vehicle %s at (%.6f, %.6f)",
        alert_id,
        vin,
        lat,
        lng,
    )


@router.post("/", response_model=SOSAlertResponse, status_code=status.HTTP_201_CREATED)
async def trigger_sos(
    alert_in: SOSAlertCreate,
    background_tasks: BackgroundTasks,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user),
):
    vehicle = db.query(Vehicle).filter(Vehicle.id == alert_in.vehicle_id).first()
    if not vehicle:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Vehicle not found")

    if current_user.role not in ("admin", "fleet_manager") and vehicle.owner_id != current_user.id:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="Access denied")

    db_alert = SOSAlert(**alert_in.model_dump())
    db.add(db_alert)
    db.commit()
    db.refresh(db_alert)

    await manager.broadcast(
        f"SOS_ALERT_TRIGGERED:{db_alert.id}:vehicle={vehicle.vin}"
        f":lat={alert_in.latitude}:lng={alert_in.longitude}"
        f":type={alert_in.trigger_type}"
    )

    background_tasks.add_task(
        _notify_emergency_services,
        db_alert.id,
        vehicle.vin,
        alert_in.latitude,
        alert_in.longitude,
    )

    return db_alert


@router.get("/active", response_model=List[SOSAlertResponse])
def get_active_alerts(
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user),
):
    return db.query(SOSAlert).filter(SOSAlert.status == "active").all()


@router.get("/", response_model=List[SOSAlertResponse])
def list_alerts(
    skip: int = 0,
    limit: int = 50,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user),
):
    return db.query(SOSAlert).order_by(SOSAlert.timestamp.desc()).offset(skip).limit(limit).all()


@router.put("/{alert_id}/dispatch", response_model=SOSAlertResponse)
def dispatch_alert(
    alert_id: int,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user),
):
    if current_user.role not in ("admin", "fleet_manager", "emergency_responder"):
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="Insufficient permissions")

    alert = db.query(SOSAlert).filter(SOSAlert.id == alert_id).first()
    if not alert:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Alert not found")
    if alert.status != "active":
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=f"Alert is already in status: {alert.status}",
        )
    alert.status = "dispatched"
    db.commit()
    db.refresh(alert)
    return alert


@router.put("/{alert_id}/resolve", response_model=SOSAlertResponse)
def resolve_alert(
    alert_id: int,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user),
):
    if current_user.role not in ("admin", "fleet_manager", "emergency_responder"):
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="Insufficient permissions")

    alert = db.query(SOSAlert).filter(SOSAlert.id == alert_id).first()
    if not alert:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Alert not found")
    alert.status = "resolved"
    db.commit()
    db.refresh(alert)
    return alert
