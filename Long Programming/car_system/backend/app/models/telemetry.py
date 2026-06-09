from sqlalchemy import Column, Integer, String, Float, DateTime, ForeignKey
from sqlalchemy.sql import func
from app.db.session import Base

class Telemetry(Base):
    __tablename__ = "telemetry"

    id = Column(Integer, primary_key=True, index=True)
    vehicle_id = Column(Integer, ForeignKey("vehicles.id"), index=True, nullable=False)
    timestamp = Column(DateTime(timezone=True), default=func.now(), index=True)
    
    # Location
    latitude = Column(Float, nullable=False)
    longitude = Column(Float, nullable=False)
    speed = Column(Float)
    heading = Column(Float)
    
    # Engine/Status
    engine_rpm = Column(Integer)
    fuel_level = Column(Float)
    battery_level = Column(Float)
    
    # AI/Sensors
    event_type = Column(String(50)) # e.g. "normal", "harsh_braking", "collision", "speeding"
    severity = Column(Integer, default=0) # 0 to 10
