import pytest


class TestEndToEndFlow:
    def test_full_vehicle_lifecycle(self, client):
        register_res = client.post(
            "/api/v1/auth/register",
            json={
                "email": "e2e_user@example.com",
                "password": "e2epassword",
                "full_name": "E2E User",
                "role": "user",
            },
        )
        assert register_res.status_code == 201

        login_res = client.post(
            "/api/v1/auth/login/access-token",
            data={"username": "e2e_user@example.com", "password": "e2epassword"},
        )
        assert login_res.status_code == 200
        token = login_res.json()["access_token"]
        headers = {"Authorization": f"Bearer {token}"}

        me_res = client.get("/api/v1/auth/me", headers=headers)
        assert me_res.status_code == 200
        assert me_res.json()["email"] == "e2e_user@example.com"

        vehicle_res = client.post(
            "/api/v1/vehicles/",
            json={"vin": "E2E_VIN_001", "make": "BMW", "model": "M3", "year": 2024},
            headers=headers,
        )
        assert vehicle_res.status_code == 201
        vehicle_id = vehicle_res.json()["id"]

        telemetry_res = client.post(
            "/api/v1/telemetry/",
            json={
                "vehicle_id": vehicle_id,
                "latitude": 34.0522,
                "longitude": -118.2437,
                "speed": 72.0,
                "event_type": "normal",
            },
            headers=headers,
        )
        assert telemetry_res.status_code == 201

        sos_res = client.post(
            "/api/v1/sos/",
            json={
                "vehicle_id": vehicle_id,
                "latitude": 34.0522,
                "longitude": -118.2437,
                "trigger_type": "manual_button",
            },
            headers=headers,
        )
        assert sos_res.status_code == 201
        assert sos_res.json()["status"] == "active"

        history_res = client.get(f"/api/v1/telemetry/vehicle/{vehicle_id}", headers=headers)
        assert history_res.status_code == 200
        assert len(history_res.json()) == 1

        active_alerts = client.get("/api/v1/sos/active", headers=headers)
        assert active_alerts.status_code == 200
        assert len(active_alerts.json()) >= 1

    def test_fleet_manager_full_workflow(self, client, fleet_headers):
        vehicle_res = client.post(
            "/api/v1/vehicles/",
            json={"vin": "E2E_FLEET_001", "make": "Ford"},
            headers=fleet_headers,
        )
        vehicle_id = vehicle_res.json()["id"]

        for i in range(3):
            client.post(
                "/api/v1/telemetry/",
                json={
                    "vehicle_id": vehicle_id,
                    "latitude": 40.0 + i * 0.01,
                    "longitude": -74.0,
                    "speed": 60.0 + i * 5,
                },
                headers=fleet_headers,
            )

        sos_res = client.post(
            "/api/v1/sos/",
            json={
                "vehicle_id": vehicle_id,
                "latitude": 40.03,
                "longitude": -74.0,
                "trigger_type": "ai_crash_detection",
                "additional_info": "AI detected collision",
            },
            headers=fleet_headers,
        )
        alert_id = sos_res.json()["id"]

        dispatch_res = client.put(f"/api/v1/sos/{alert_id}/dispatch", headers=fleet_headers)
        assert dispatch_res.json()["status"] == "dispatched"

        resolve_res = client.put(f"/api/v1/sos/{alert_id}/resolve", headers=fleet_headers)
        assert resolve_res.json()["status"] == "resolved"

        active_res = client.get("/api/v1/sos/active", headers=fleet_headers)
        alert_ids = [a["id"] for a in active_res.json()]
        assert alert_id not in alert_ids

    def test_unauthorized_cross_user_isolation(self, client, auth_headers, admin_headers):
        admin_vehicle = client.post(
            "/api/v1/vehicles/", json={"vin": "E2E_ISO_001"}, headers=admin_headers
        ).json()

        user_vehicle_res = client.get(
            f"/api/v1/vehicles/{admin_vehicle['id']}", headers=auth_headers
        )
        assert user_vehicle_res.status_code == 403

        user_telemetry = client.post(
            "/api/v1/telemetry/",
            json={
                "vehicle_id": admin_vehicle["id"],
                "latitude": 40.0,
                "longitude": -74.0,
            },
            headers=auth_headers,
        )
        assert user_telemetry.status_code == 403
