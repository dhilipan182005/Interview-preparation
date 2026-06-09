from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from typing import List

from app.db.session import get_db
from app.models.telemetry import Telemetry
from app.models.vehicle import Vehicle
from app.models.user import User
from app.schemas.telemetry import TelemetryCreate, TelemetryResponse
from app.core.security import get_current_user

router = APIRouter()


@router.post("/", response_model=TelemetryResponse, status_code=status.HTTP_201_CREATED)
def ingest_telemetry(
    telemetry_in: TelemetryCreate,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user),
):
    vehicle = db.query(Vehicle).filter(Vehicle.id == telemetry_in.vehicle_id).first()
    if not vehicle:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Vehicle not found")

    if current_user.role not in ("admin", "fleet_manager") and vehicle.owner_id != current_user.id:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="Access denied")

    db_telemetry = Telemetry(**telemetry_in.model_dump())
    db.add(db_telemetry)

    vehicle.last_known_lat = telemetry_in.latitude
    vehicle.last_known_lng = telemetry_in.longitude

    db.commit()
    db.refresh(db_telemetry)

    try:
        from app.worker.tasks import process_telemetry_event
        process_telemetry_event.delay(db_telemetry.id)
    except Exception as e:
        import logging
        logging.getLogger(__name__).warning("Failed to dispatch Celery task: %s", str(e))

    return db_telemetry


@router.get("/vehicle/{vehicle_id}", response_model=List[TelemetryResponse])
def get_vehicle_telemetry(
    vehicle_id: int,
    skip: int = 0,
    limit: int = 100,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user),
):
    vehicle = db.query(Vehicle).filter(Vehicle.id == vehicle_id).first()
    if not vehicle:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Vehicle not found")

    if current_user.role not in ("admin", "fleet_manager") and vehicle.owner_id != current_user.id:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="Access denied")

    history = (
        db.query(Telemetry)
        .filter(Telemetry.vehicle_id == vehicle_id)
        .order_by(Telemetry.timestamp.desc())
        .offset(skip)
        .limit(limit)
        .all()
    )
    return history
