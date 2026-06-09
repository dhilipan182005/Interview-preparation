from pydantic import BaseModel, ConfigDict
from typing import Optional
from datetime import datetime


class SOSAlertCreate(BaseModel):
    vehicle_id: int
    latitude: float
    longitude: float
    trigger_type: str
    additional_info: Optional[str] = None


class SOSAlertUpdate(BaseModel):
    status: str


class SOSAlertResponse(SOSAlertCreate):
    model_config = ConfigDict(from_attributes=True)

    id: int
    status: str
    timestamp: datetime
