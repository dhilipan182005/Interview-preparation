import pytest
from sqlalchemy.exc import IntegrityError
from app.models.user import User
from app.models.vehicle import Vehicle
from app.models.telemetry import Telemetry
from app.models.sos import SOSAlert
from app.core.hashing import get_password_hash


class TestDatabaseConstraints:
    def test_unique_email_constraint(self, db_session):
        user1 = User(
            email="unique_email_test@test.com",
            hashed_password=get_password_hash("pass"),
            full_name="User 1",
        )
        db_session.add(user1)
        db_session.flush()

        user2 = User(
            email="unique_email_test@test.com",
            hashed_password=get_password_hash("pass"),
            full_name="User 2",
        )
        db_session.add(user2)
        with pytest.raises(Exception):
            db_session.flush()
        db_session.rollback()

    def test_unique_vin_constraint(self, db_session, test_user):
        v1 = Vehicle(vin="VIN_DB_UNIQ_001", owner_id=test_user.id)
        db_session.add(v1)
        db_session.flush()

        v2 = Vehicle(vin="VIN_DB_UNIQ_001", owner_id=test_user.id)
        db_session.add(v2)
        with pytest.raises(Exception):
            db_session.flush()
        db_session.rollback()

    def test_telemetry_foreign_key_enforced(self, db_session):
        telemetry = Telemetry(
            vehicle_id=99999,
            latitude=40.0,
            longitude=-74.0,
        )
        db_session.add(telemetry)
        with pytest.raises(Exception):
            db_session.flush()
        db_session.rollback()

    def test_sos_foreign_key_enforced(self, db_session):
        alert = SOSAlert(
            vehicle_id=99999,
            latitude=40.0,
            longitude=-74.0,
            trigger_type="manual_button",
        )
        db_session.add(alert)
        with pytest.raises(Exception):
            db_session.flush()
        db_session.rollback()


class TestDatabaseTransactions:
    def test_rollback_preserves_state(self, db_session, test_user):
        initial_count = db_session.query(Vehicle).count()

        v = Vehicle(vin="VIN_ROLLBACK_TEST", owner_id=test_user.id)
        db_session.add(v)
        db_session.flush()
        mid_count = db_session.query(Vehicle).count()
        assert mid_count == initial_count + 1

        db_session.rollback()
        post_count = db_session.query(Vehicle).count()
        assert post_count == initial_count

    def test_cascade_query_efficiency(self, db_session, test_user):
        vehicle = Vehicle(vin="VIN_QUERY_EFF", owner_id=test_user.id)
        db_session.add(vehicle)
        db_session.flush()

        for i in range(20):
            t = Telemetry(vehicle_id=vehicle.id, latitude=40.0 + i * 0.01, longitude=-74.0)
            db_session.add(t)
        db_session.flush()

        results = (
            db_session.query(Telemetry)
            .filter(Telemetry.vehicle_id == vehicle.id)
            .order_by(Telemetry.timestamp.desc())
            .limit(10)
            .all()
        )
        assert len(results) == 10


class TestDatabaseDefaults:
    def test_user_is_active_default(self, db_session):
        user = User(
            email="defaulttest_db@example.com",
            hashed_password=get_password_hash("pass"),
        )
        db_session.add(user)
        db_session.flush()
        db_session.refresh(user)
        assert user.is_active is True

    def test_sos_status_default(self, db_session, test_user):
        vehicle = Vehicle(vin="VIN_SOS_DEF_DB", owner_id=test_user.id)
        db_session.add(vehicle)
        db_session.flush()

        alert = SOSAlert(
            vehicle_id=vehicle.id,
            latitude=40.7,
            longitude=-74.0,
            trigger_type="manual_button",
        )
        db_session.add(alert)
        db_session.flush()
        db_session.refresh(alert)
        assert alert.status == "active"

    def test_telemetry_severity_default(self, db_session, test_user):
        vehicle = Vehicle(vin="VIN_TEL_DEF_DB", owner_id=test_user.id)
        db_session.add(vehicle)
        db_session.flush()

        t = Telemetry(vehicle_id=vehicle.id, latitude=40.0, longitude=-74.0)
        db_session.add(t)
        db_session.flush()
        db_session.refresh(t)
        assert t.severity == 0
