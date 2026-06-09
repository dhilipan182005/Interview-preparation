from sqlalchemy import Column, Integer, String, Float, DateTime, ForeignKey
from sqlalchemy.sql import func
from app.db.session import Base

class SOSAlert(Base):
    __tablename__ = "sos_alerts"

    id = Column(Integer, primary_key=True, index=True)
    vehicle_id = Column(Integer, ForeignKey("vehicles.id"), index=True, nullable=False)
    
    latitude = Column(Float, nullable=False)
    longitude = Column(Float, nullable=False)
    
    trigger_type = Column(String(50), nullable=False) # manual_button, ai_crash_detection
    status = Column(String(50), default="active") # active, dispatched, resolved
    additional_info = Column(String(500), nullable=True)
    
    timestamp = Column(DateTime(timezone=True), default=func.now())
