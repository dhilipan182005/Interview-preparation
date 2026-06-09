import pytest


class TestSQLInjectionPrevention:
    def test_sql_injection_in_login(self, client):
        response = client.post(
            "/api/v1/auth/login/access-token",
            data={"username": "' OR '1'='1", "password": "' OR '1'='1"},
        )
        assert response.status_code in (401, 422)

    def test_sql_injection_in_vehicle_vin(self, client, auth_headers):
        response = client.post(
            "/api/v1/vehicles/",
            json={"vin": "'; DROP TABLE vehicles; --"},
            headers=auth_headers,
        )
        assert response.status_code in (201, 400)

    def test_vehicle_id_path_traversal(self, client, auth_headers):
        response = client.get("/api/v1/vehicles/../../etc/passwd", headers=auth_headers)
        assert response.status_code in (404, 422)

    def test_extremely_long_vin_handled(self, client, auth_headers):
        response = client.post(
            "/api/v1/vehicles/",
            json={"vin": "A" * 5000},
            headers=auth_headers,
        )
        assert response.status_code in (201, 400, 422)


class TestJWTSecurity:
    def test_tampered_token_rejected(self, client):
        response = client.get(
            "/api/v1/auth/me",
            headers={"Authorization": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.INVALID"},
        )
        assert response.status_code == 401

    def test_missing_bearer_prefix_rejected(self, client):
        response = client.get(
            "/api/v1/auth/me",
            headers={"Authorization": "sometoken"},
        )
        assert response.status_code == 401

    def test_empty_token_rejected(self, client):
        response = client.get(
            "/api/v1/auth/me",
            headers={"Authorization": "Bearer "},
        )
        assert response.status_code == 401

    def test_no_auth_header_rejected(self, client):
        for path in ["/api/v1/vehicles/", "/api/v1/sos/active", "/api/v1/auth/me"]:
            response = client.get(path)
            assert response.status_code == 401


class TestAuthorizationEnforcement:
    def test_regular_user_cannot_dispatch_sos(self, client, auth_headers, fleet_headers):
        veh_res = client.post(
            "/api/v1/vehicles/", json={"vin": "SEC_SOS_001"}, headers=fleet_headers
        )
        vehicle_id = veh_res.json()["id"]
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
        response = client.put(f"/api/v1/sos/{alert_id}/dispatch", headers=auth_headers)
        assert response.status_code == 403

    def test_user_cannot_access_other_users_data(self, client, auth_headers, admin_headers):
        veh_res = client.post(
            "/api/v1/vehicles/", json={"vin": "SEC_OTHER_001"}, headers=admin_headers
        )
        vehicle_id = veh_res.json()["id"]
        response = client.get(f"/api/v1/vehicles/{vehicle_id}", headers=auth_headers)
        assert response.status_code == 403


class TestInputValidation:
    def test_invalid_latitude_rejected(self, client, auth_headers):
        veh_res = client.post("/api/v1/vehicles/", json={"vin": "VAL_LAT_001"}, headers=auth_headers)
        vehicle_id = veh_res.json()["id"]
        response = client.post(
            "/api/v1/telemetry/",
            json={"vehicle_id": vehicle_id, "latitude": "not_a_number", "longitude": -74.0},
            headers=auth_headers,
        )
        assert response.status_code == 422

    def test_missing_required_field_rejected(self, client, auth_headers):
        response = client.post(
            "/api/v1/sos/",
            json={"vehicle_id": 1, "latitude": 40.7},
            headers=auth_headers,
        )
        assert response.status_code == 422

    def test_missing_vehicle_id_rejected(self, client, auth_headers):
        response = client.post(
            "/api/v1/telemetry/",
            json={"latitude": 40.7, "longitude": -74.0},
            headers=auth_headers,
        )
        assert response.status_code == 422
