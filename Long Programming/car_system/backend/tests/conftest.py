import sys
import os

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", ".."))

import pytest
from fastapi.testclient import TestClient
from sqlalchemy import create_engine, event
from sqlalchemy.orm import sessionmaker
from sqlalchemy.pool import StaticPool

from app.main import app
from app.db.session import Base, get_db
from app.models.user import User
from app.models.vehicle import Vehicle
from app.core.hashing import get_password_hash


SQLALCHEMY_DATABASE_URL = "sqlite:///:memory:"

engine = create_engine(
    SQLALCHEMY_DATABASE_URL,
    connect_args={"check_same_thread": False},
    poolclass=StaticPool,
)


@event.listens_for(engine, "connect")
def _set_sqlite_pragma(dbapi_connection, connection_record):
    cursor = dbapi_connection.cursor()
    cursor.execute("PRAGMA foreign_keys=ON")
    cursor.close()


TestingSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)


@pytest.fixture(scope="session", autouse=True)
def setup_db():
    Base.metadata.create_all(bind=engine)
    yield
    Base.metadata.drop_all(bind=engine)


@pytest.fixture(scope="function")
def db_session(setup_db):
    connection = engine.connect()
    transaction = connection.begin()
    session = TestingSessionLocal(bind=connection)
    yield session
    session.close()
    transaction.rollback()
    connection.close()


@pytest.fixture(scope="function")
def client(db_session):
    def override_get_db():
        yield db_session

    app.dependency_overrides[get_db] = override_get_db
    with TestClient(app) as test_client:
        yield test_client
    app.dependency_overrides.clear()


def _make_user(db_session, email, password, full_name="Test User", role="user"):
    user = User(
        email=email,
        hashed_password=get_password_hash(password),
        full_name=full_name,
        is_active=True,
        role=role,
    )
    db_session.add(user)
    db_session.commit()
    db_session.refresh(user)
    return user


@pytest.fixture(scope="function")
def test_user(db_session):
    return _make_user(db_session, "test@example.com", "testpassword", role="user")


@pytest.fixture(scope="function")
def admin_user(db_session):
    return _make_user(db_session, "admin@example.com", "adminpassword", full_name="Admin", role="admin")


@pytest.fixture(scope="function")
def fleet_manager(db_session):
    return _make_user(
        db_session,
        "fleet@example.com",
        "fleetpassword",
        full_name="Fleet Manager",
        role="fleet_manager",
    )


def get_token(client, email, password):
    response = client.post(
        "/api/v1/auth/login/access-token",
        data={"username": email, "password": password},
    )
    return response.json()["access_token"]


@pytest.fixture(scope="function")
def user_token(client, test_user):
    return get_token(client, "test@example.com", "testpassword")


@pytest.fixture(scope="function")
def admin_token(client, admin_user):
    return get_token(client, "admin@example.com", "adminpassword")


@pytest.fixture(scope="function")
def fleet_token(client, fleet_manager):
    return get_token(client, "fleet@example.com", "fleetpassword")


@pytest.fixture(scope="function")
def auth_headers(user_token):
    return {"Authorization": f"Bearer {user_token}"}


@pytest.fixture(scope="function")
def admin_headers(admin_token):
    return {"Authorization": f"Bearer {admin_token}"}


@pytest.fixture(scope="function")
def fleet_headers(fleet_token):
    return {"Authorization": f"Bearer {fleet_token}"}


@pytest.fixture(scope="function")
def test_vehicle(db_session, test_user):
    vehicle = Vehicle(vin="TESTVEHICLE001", make="Toyota", model="Camry", year=2023, owner_id=test_user.id)
    db_session.add(vehicle)
    db_session.commit()
    db_session.refresh(vehicle)
    return vehicle
