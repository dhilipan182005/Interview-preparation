from celery import Celery
from app.core.config import settings

celery_app = Celery(
    "vehicle_worker",
    broker=settings.CELERY_BROKER_URL,
    backend=settings.CELERY_RESULT_BACKEND,
)

celery_app.conf.task_routes = {
    "app.worker.tasks.*": {"queue": "main-queue"},
}

celery_app.conf.update(
    task_serializer="json",
    accept_content=["json"],
    result_serializer="json",
    timezone="UTC",
    enable_utc=True,
    task_acks_late=True,
    task_reject_on_worker_lost=True,
    worker_prefetch_multiplier=1,
    task_soft_time_limit=30,
    task_time_limit=60,
    task_max_retries=3,
    broker_connection_retry_on_startup=True,
)
