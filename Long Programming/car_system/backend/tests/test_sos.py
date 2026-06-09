import pytest


def _create_vehicle_and_get_id(client, headers, vin="SOS_VEH_001"):
    res = client.post("/api/v1/vehicles/", json={"vin": vin}, headers=headers)
    return res.json()["id"]


def test_trigger_sos_unauthenticated(client):
    response = client.post(
        "/api/v1/sos/",
        json={"vehicle_id": 1, "latitude": 40.7, "longitude": -74.0, "trigger_type": "manual_button"},
    )
    assert response.status_code == 401


def test_trigger_sos_vehicle_not_found(client, auth_headers):
    response = client.post(
        "/api/v1/sos/",
        json={
            "vehicle_id": 99999,
            "latitude": 40.7128,
            "longitude": -74.0060,
            "trigger_type": "manual_button",
        },
        headers=auth_headers,
    )
    assert response.status_code == 404


def test_trigger_sos_success(client, auth_headers):
    vehicle_id = _create_vehicle_and_get_id(client, auth_headers, "SOS_MANUAL_001")
    response = client.post(
        "/api/v1/sos/",
        json={
            "vehicle_id": vehicle_id,
            "latitude": 40.7128,
            "longitude": -74.0060,
            "trigger_type": "manual_button",
        },
        headers=auth_headers,
    )
    assert response.status_code == 201
    data = response.json()
    assert data["status"] == "active"
    assert data["trigger_type"] == "manual_button"
    assert "id" in data
    assert "timestamp" in data


def test_get_active_alerts(client, auth_headers):
    vehicle_id = _create_vehicle_and_get_id(client, auth_headers, "SOS_ACTIVE_001")
    client.post(
        "/api/v1/sos/",
        json={
            "vehicle_id": vehicle_id,
            "latitude": 40.7,
            "longitude": -74.0,
            "trigger_type": "ai_crash_detection",
        },
        headers=auth_headers,
    )
    response = client.get("/api/v1/sos/active", headers=auth_headers)
    assert response.status_code == 200
    assert isinstance(response.json(), list)
    assert len(response.json()) >= 1


def test_dispatch_alert(client, fleet_headers, admin_headers):
    vehicle_id = _create_vehicle_and_get_id(client, fleet_headers, "SOS_DISP_001")
    sos_res = client.post(
        "/api/v1/sos/",
        json={
            "vehicle_id": vehicle_id,
            "latitude": 40.7,
            "longitude": -74.0,
            "trigger_type": "manual_button",
        },
        headers=fleet_headers,
    )
    alert_id = sos_res.json()["id"]

    dispatch_res = client.put(f"/api/v1/sos/{alert_id}/dispatch", headers=fleet_headers)
    assert dispatch_res.status_code == 200
    assert dispatch_res.json()["status"] == "dispatched"


def test_resolve_alert(client, fleet_headers):
    vehicle_id = _create_vehicle_and_get_id(client, fleet_headers, "SOS_RESOLV_001")
    sos_res = client.post(
        "/api/v1/sos/",
        json={
            "vehicle_id": vehicle_id,
            "latitude": 40.7,
            "longitude": -74.0,
            "trigger_type": "manual_button",
        },
        headers=fleet_headers,
    )
    alert_id = sos_res.json()["id"]

    resolve_res = client.put(f"/api/v1/sos/{alert_id}/resolve", headers=fleet_headers)
    assert resolve_res.status_code == 200
    assert resolve_res.json()["status"] == "resolved"


def test_regular_user_cannot_dispatch(client, auth_headers):
    vehicle_id = _create_vehicle_and_get_id(client, auth_headers, "SOS_PERM_001")
    sos_res = client.post(
        "/api/v1/sos/",
        json={
            "vehicle_id": vehicle_id,
            "latitude": 40.7,
            "longitude": -74.0,
            "trigger_type": "manual_button",
        },
        headers=auth_headers,
    )
    alert_id = sos_res.json()["id"]
    dispatch_res = client.put(f"/api/v1/sos/{alert_id}/dispatch", headers=auth_headers)
    assert dispatch_res.status_code == 403


def test_dispatch_nonexistent_alert(client, fleet_headers):
    response = client.put("/api/v1/sos/99999/dispatch", headers=fleet_headers)
    assert response.status_code == 404


def test_double_dispatch_rejected(client, fleet_headers):
    vehicle_id = _create_vehicle_and_get_id(client, fleet_headers, "SOS_DBLDISP_001")
    sos_res = client.post(
        "/api/v1/sos/",
        json={
            "vehicle_id": vehicle_id,
            "latitude": 40.7,
            "longitude": -74.0,
            "trigger_type": "manual_button",
        },
        headers=fleet_headers,
    )
    alert_id = sos_res.json()["id"]
    client.put(f"/api/v1/sos/{alert_id}/dispatch", headers=fleet_headers)
    second_dispatch = client.put(f"/api/v1/sos/{alert_id}/dispatch", headers=fleet_headers)
    assert second_dispatch.status_code == 400
