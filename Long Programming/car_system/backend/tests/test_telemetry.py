import pytest


def test_ingest_telemetry_unauthenticated(client):
    response = client.post(
        "/api/v1/telemetry/",
        json={"vehicle_id": 1, "latitude": 34.05, "longitude": -118.24},
    )
    assert response.status_code == 401


def test_ingest_telemetry_no_vehicle(client, auth_headers):
    response = client.post(
        "/api/v1/telemetry/",
        json={"vehicle_id": 9999, "latitude": 34.05, "longitude": -118.24},
        headers=auth_headers,
    )
    assert response.status_code == 404


def test_ingest_telemetry_success(client, auth_headers):
    veh_res = client.post("/api/v1/vehicles/", json={"vin": "TEL_TEST_001"}, headers=auth_headers)
    assert veh_res.status_code == 201
    vehicle_id = veh_res.json()["id"]

    response = client.post(
        "/api/v1/telemetry/",
        json={
            "vehicle_id": vehicle_id,
            "latitude": 34.05,
            "longitude": -118.24,
            "speed": 65.5,
            "event_type": "normal",
        },
        headers=auth_headers,
    )
    assert response.status_code == 201
    data = response.json()
    assert data["latitude"] == 34.05
    assert data["vehicle_id"] == vehicle_id
    assert data["speed"] == 65.5


def test_telemetry_updates_vehicle_location(client, auth_headers, db_session):
    from app.models.vehicle import Vehicle

    veh_res = client.post("/api/v1/vehicles/", json={"vin": "TEL_LOC_001"}, headers=auth_headers)
    vehicle_id = veh_res.json()["id"]

    client.post(
        "/api/v1/telemetry/",
        json={"vehicle_id": vehicle_id, "latitude": 40.7128, "longitude": -74.006},
        headers=auth_headers,
    )
    db_session.expire_all()
    vehicle = db_session.query(Vehicle).filter(Vehicle.id == vehicle_id).first()
    assert vehicle.last_known_lat == pytest.approx(40.7128, rel=1e-4)
    assert vehicle.last_known_lng == pytest.approx(-74.006, rel=1e-4)


def test_get_telemetry_history(client, auth_headers):
    veh_res = client.post("/api/v1/vehicles/", json={"vin": "TEL_HIST_001"}, headers=auth_headers)
    vehicle_id = veh_res.json()["id"]

    for i in range(5):
        client.post(
            "/api/v1/telemetry/",
            json={"vehicle_id": vehicle_id, "latitude": 34.0 + i, "longitude": -118.0},
            headers=auth_headers,
        )

    response = client.get(f"/api/v1/telemetry/vehicle/{vehicle_id}", headers=auth_headers)
    assert response.status_code == 200
    assert len(response.json()) == 5


def test_telemetry_history_pagination(client, auth_headers):
    veh_res = client.post("/api/v1/vehicles/", json={"vin": "TEL_PAGE_001"}, headers=auth_headers)
    vehicle_id = veh_res.json()["id"]

    for i in range(10):
        client.post(
            "/api/v1/telemetry/",
            json={"vehicle_id": vehicle_id, "latitude": 34.0 + i * 0.01, "longitude": -118.0},
            headers=auth_headers,
        )

    page1 = client.get(
        f"/api/v1/telemetry/vehicle/{vehicle_id}?skip=0&limit=5", headers=auth_headers
    )
    page2 = client.get(
        f"/api/v1/telemetry/vehicle/{vehicle_id}?skip=5&limit=5", headers=auth_headers
    )
    assert len(page1.json()) == 5
    assert len(page2.json()) == 5
    ids1 = {r["id"] for r in page1.json()}
    ids2 = {r["id"] for r in page2.json()}
    assert ids1.isdisjoint(ids2)


def test_telemetry_access_other_vehicle_denied(client, auth_headers, admin_headers):
    veh_res = client.post("/api/v1/vehicles/", json={"vin": "TEL_ADM_001"}, headers=admin_headers)
    vehicle_id = veh_res.json()["id"]

    response = client.get(f"/api/v1/telemetry/vehicle/{vehicle_id}", headers=auth_headers)
    assert response.status_code == 403
