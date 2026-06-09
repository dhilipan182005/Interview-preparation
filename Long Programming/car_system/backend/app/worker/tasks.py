import logging
from app.core.celery_app import celery_app
from app.db.session import SessionLocal
from app.models.telemetry import Telemetry
from app.models.sos import SOSAlert
from app.services.ai.accident import accident_engine

logger = logging.getLogger(__name__)


@celery_app.task(
    name="app.worker.tasks.process_telemetry_event",
    bind=True,
    max_retries=3,
    default_retry_delay=5,
)
def process_telemetry_event(self, telemetry_id: int):
    db = SessionLocal()
    try:
        telemetry = db.query(Telemetry).filter(Telemetry.id == telemetry_id).first()
        if not telemetry:
            logger.warning("Telemetry record %d not found", telemetry_id)
            return {"status": "not_found", "telemetry_id": telemetry_id}

        window = [
            {
                "speed": telemetry.speed or 0.0,
                "g_force": 1.0,
                "heading_change": 0.0,
                "event_type": telemetry.event_type or "normal",
                "severity": telemetry.severity or 0,
            }
        ]

        analysis = accident_engine.analyze_telemetry_window(window)

        if analysis["is_accident"] or analysis["event_type"] in ("collision", "rollover"):
            _create_sos_alert(db, telemetry, analysis)

        logger.info(
            "Telemetry %d processed: event=%s confidence=%.2f",
            telemetry_id,
            analysis["event_type"],
            analysis["confidence"],
        )
        return {"status": "processed", "telemetry_id": telemetry_id, "analysis": analysis}

    except Exception as exc:
        logger.error("Error processing telemetry %d: %s", telemetry_id, exc)
        raise self.retry(exc=exc)
    finally:
        db.close()


def _create_sos_alert(db, telemetry, analysis: dict):
    existing = (
        db.query(SOSAlert)
        .filter(
            SOSAlert.vehicle_id == telemetry.vehicle_id,
            SOSAlert.status == "active",
        )
        .first()
    )
    if existing:
        return

    alert = SOSAlert(
        vehicle_id=telemetry.vehicle_id,
        latitude=telemetry.latitude,
        longitude=telemetry.longitude,
        trigger_type="ai_crash_detection",
        status="active",
        additional_info=(
            f"AI detected {analysis['event_type']} with {analysis['confidence']:.0%} confidence. "
            f"Severity: {analysis['severity']}/10"
        ),
    )
    db.add(alert)
    db.commit()
    logger.critical(
        "SOS ALERT CREATED for vehicle %d at lat=%f lng=%f",
        telemetry.vehicle_id,
        telemetry.latitude,
        telemetry.longitude,
    )
