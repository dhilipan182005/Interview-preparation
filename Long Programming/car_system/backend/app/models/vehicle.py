from sqlalchemy import Column, Integer, String, Boolean, DateTime, ForeignKey, Float
from sqlalchemy.sql import func
from sqlalchemy.orm import relationship
from app.db.session import Base

class Vehicle(Base):
    __tablename__ = "vehicles"

    id = Column(Integer, primary_key=True, index=True)
    vin = Column(String(50), unique=True, index=True, nullable=False)
    make = Column(String(100))
    model = Column(String(100))
    year = Column(Integer)
    license_plate = Column(String(50), index=True)
    
    # Ownership/Assignment
    owner_id = Column(Integer, ForeignKey("users.id"))
    fleet_id = Column(Integer, nullable=True) # Future use for fleets
    
    # Status
    is_active = Column(Boolean(), default=True)
    last_known_lat = Column(Float, nullable=True)
    last_known_lng = Column(Float, nullable=True)
    
    # Audit fields
    created_at = Column(DateTime(timezone=True), server_default=func.now())
    updated_at = Column(DateTime(timezone=True), onupdate=func.now())
    
    owner = relationship("User", backref="vehicles")
