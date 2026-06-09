import pytest


def test_create_vehicle_unauthenticated(client):
    response = client.post("/api/v1/vehicles/", json={"vin": "VIN12345"})
    assert response.status_code == 401


def test_create_vehicle_authenticated(client, auth_headers):
    response = client.post(
        "/api/v1/vehicles/",
        json={"vin": "VIN_NEWCAR", "make": "Toyota", "model": "Camry", "year": 2023},
        headers=auth_headers,
    )
    assert response.status_code == 201
    data = response.json()
    assert data["vin"] == "VIN_NEWCAR"
    assert data["make"] == "Toyota"


def test_create_duplicate_vin(client, auth_headers):
    client.post("/api/v1/vehicles/", json={"vin": "VIN_DUP"}, headers=auth_headers)
    response = client.post("/api/v1/vehicles/", json={"vin": "VIN_DUP"}, headers=auth_headers)
    assert response.status_code == 400


def test_get_vehicles_returns_only_own(client, auth_headers, admin_headers):
    client.post("/api/v1/vehicles/", json={"vin": "VIN_USER_CAR"}, headers=auth_headers)
    client.post("/api/v1/vehicles/", json={"vin": "VIN_ADMIN_CAR"}, headers=admin_headers)

    user_response = client.get("/api/v1/vehicles/", headers=auth_headers)
    assert user_response.status_code == 200
    vins = [v["vin"] for v in user_response.json()]
    assert "VIN_USER_CAR" in vins
    assert "VIN_ADMIN_CAR" not in vins


def test_admin_sees_all_vehicles(client, auth_headers, admin_headers):
    client.post("/api/v1/vehicles/", json={"vin": "VIN_USR_A"}, headers=auth_headers)
    client.post("/api/v1/vehicles/", json={"vin": "VIN_ADM_A"}, headers=admin_headers)

    response = client.get("/api/v1/vehicles/", headers=admin_headers)
    assert response.status_code == 200
    vins = [v["vin"] for v in response.json()]
    assert "VIN_USR_A" in vins
    assert "VIN_ADM_A" in vins


def test_get_vehicle_by_id(client, auth_headers):
    create_res = client.post("/api/v1/vehicles/", json={"vin": "VIN_BYID"}, headers=auth_headers)
    vehicle_id = create_res.json()["id"]
    response = client.get(f"/api/v1/vehicles/{vehicle_id}", headers=auth_headers)
    assert response.status_code == 200
    assert response.json()["vin"] == "VIN_BYID"


def test_get_nonexistent_vehicle(client, auth_headers):
    response = client.get("/api/v1/vehicles/99999", headers=auth_headers)
    assert response.status_code == 404


def test_update_vehicle(client, auth_headers):
    create_res = client.post("/api/v1/vehicles/", json={"vin": "VIN_UPDATE"}, headers=auth_headers)
    vehicle_id = create_res.json()["id"]
    update_res = client.put(
        f"/api/v1/vehicles/{vehicle_id}",
        json={"make": "Honda", "model": "Civic"},
        headers=auth_headers,
    )
    assert update_res.status_code == 200
    assert update_res.json()["make"] == "Honda"


def test_delete_vehicle(client, auth_headers):
    create_res = client.post("/api/v1/vehicles/", json={"vin": "VIN_DELETE"}, headers=auth_headers)
    vehicle_id = create_res.json()["id"]
    del_res = client.delete(f"/api/v1/vehicles/{vehicle_id}", headers=auth_headers)
    assert del_res.status_code == 204
    get_res = client.get(f"/api/v1/vehicles/{vehicle_id}", headers=auth_headers)
    assert get_res.status_code == 404


def test_user_cannot_access_other_user_vehicle(client, auth_headers, admin_headers):
    create_res = client.post("/api/v1/vehicles/", json={"vin": "VIN_PRIV"}, headers=admin_headers)
    vehicle_id = create_res.json()["id"]
    response = client.get(f"/api/v1/vehicles/{vehicle_id}", headers=auth_headers)
    assert response.status_code == 403
