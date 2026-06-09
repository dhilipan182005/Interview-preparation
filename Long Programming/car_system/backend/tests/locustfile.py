from locust import HttpUser, task, between, events
import random
import json


class VehiclePlatformLoadUser(HttpUser):
    wait_time = between(0.5, 2)
    token = None
    vehicle_id = None

    def on_start(self):
        email = f"loadtest_{random.randint(1, 999999)}@test.com"
        reg = self.client.post(
            "/api/v1/auth/register",
            json={"email": email, "password": "loadtest123", "full_name": "Load Tester"},
            name="/api/v1/auth/register",
        )
        if reg.status_code != 201:
            return

        login = self.client.post(
            "/api/v1/auth/login/access-token",
            data={"username": email, "password": "loadtest123"},
            name="/api/v1/auth/login",
        )
        if login.status_code == 200:
            self.token = login.json()["access_token"]
            self.client.headers.update({"Authorization": f"Bearer {self.token}"})

        if self.token:
            vin = f"LOAD_{random.randint(100000, 999999)}"
            veh = self.client.post(
                "/api/v1/vehicles/",
                json={"vin": vin, "make": "LoadTest"},
                name="/api/v1/vehicles/ [POST]",
            )
            if veh.status_code == 201:
                self.vehicle_id = veh.json()["id"]

    @task(5)
    def send_telemetry(self):
        if not self.vehicle_id:
            return
        self.client.post(
            "/api/v1/telemetry/",
            json={
                "vehicle_id": self.vehicle_id,
                "latitude": 34.05 + random.uniform(-0.5, 0.5),
                "longitude": -118.24 + random.uniform(-0.5, 0.5),
                "speed": random.uniform(0, 120),
                "event_type": "normal",
            },
            name="/api/v1/telemetry/ [POST]",
        )

    @task(2)
    def check_active_sos(self):
        self.client.get("/api/v1/sos/active", name="/api/v1/sos/active [GET]")

    @task(1)
    def get_vehicles(self):
        self.client.get("/api/v1/vehicles/", name="/api/v1/vehicles/ [GET]")

    @task(1)
    def get_telemetry_history(self):
        if not self.vehicle_id:
            return
        self.client.get(
            f"/api/v1/telemetry/vehicle/{self.vehicle_id}?limit=20",
            name="/api/v1/telemetry/vehicle/{id} [GET]",
        )

    @task(1)
    def health_check(self):
        self.client.get("/health", name="/health [GET]")
