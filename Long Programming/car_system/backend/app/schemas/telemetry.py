from pydantic import BaseModel, ConfigDict
from typing import Optional
from datetime import datetime


class TelemetryBase(BaseModel):
    vehicle_id: int
    latitude: float
    longitude: float
    speed: Optional[float] = None
    heading: Optional[float] = None
    engine_rpm: Optional[int] = None
    fuel_level: Optional[float] = None
    battery_level: Optional[float] = None
    event_type: Optional[str] = "normal"
    severity: Optional[int] = 0


class TelemetryCreate(TelemetryBase):
    pass


class TelemetryResponse(TelemetryBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    timestamp: datetime
