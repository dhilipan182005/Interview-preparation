import sys
import os

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", ".."))

import pytest
import threading
import queue
from unittest.mock import patch, MagicMock, call
from hardware_sim.sim import simulate_vehicle, build_payload, SimulationConfig


class TestSimulationConfig:
    def test_default_config_valid(self):
        config = SimulationConfig()
        assert config.api_url.startswith("http")
        assert config.iterations > 0
        assert config.interval_seconds >= 0

    def test_custom_config(self):
        config = SimulationConfig(api_url="http://testserver", iterations=3, interval_seconds=0)
        assert config.iterations == 3


class TestBuildPayload:
    def test_payload_structure(self):
        payload = build_payload(vehicle_id=1)
        required_keys = {"vehicle_id", "latitude", "longitude", "speed", "event_type"}
        assert required_keys.issubset(payload.keys())

    def test_payload_vehicle_id(self):
        payload = build_payload(vehicle_id=42)
        assert payload["vehicle_id"] == 42

    def test_latitude_in_range(self):
        for _ in range(20):
            payload = build_payload(vehicle_id=1)
            assert -90 <= payload["latitude"] <= 90

    def test_longitude_in_range(self):
        for _ in range(20):
            payload = build_payload(vehicle_id=1)
            assert -180 <= payload["longitude"] <= 180

    def test_speed_non_negative(self):
        for _ in range(20):
            payload = build_payload(vehicle_id=1)
            assert payload["speed"] >= 0

    def test_collision_payload_has_high_severity(self):
        payload = build_payload(vehicle_id=1, event_type="collision")
        assert payload["event_type"] == "collision"
        assert payload.get("severity", 0) >= 8


class TestSimulateVehicle:
    def test_simulate_vehicle_success(self):
        results = queue.Queue()
        with patch("hardware_sim.sim.requests.Session") as MockSession:
            mock_session_instance = MagicMock()
            mock_response = MagicMock()
            mock_response.status_code = 201
            mock_session_instance.post.return_value = mock_response
            MockSession.return_value = mock_session_instance

            config = SimulationConfig(api_url="http://testserver", iterations=3, interval_seconds=0)
            simulate_vehicle(vehicle_id=1, results_queue=results, config=config)

        assert mock_session_instance.post.call_count == 3

    def test_simulate_vehicle_handles_network_error(self):
        results = queue.Queue()
        with patch("hardware_sim.sim.requests.Session") as MockSession:
            mock_session_instance = MagicMock()
            mock_session_instance.post.side_effect = ConnectionError("Connection refused")
            MockSession.return_value = mock_session_instance

            config = SimulationConfig(api_url="http://localhost:9999", iterations=2, interval_seconds=0)
            successes, failures = simulate_vehicle(vehicle_id=1, results_queue=results, config=config)

        assert failures == 2
        assert successes == 0

    def test_multi_vehicle_concurrent_simulation(self):
        with patch("hardware_sim.sim.requests.Session") as MockSession:
            mock_session_instance = MagicMock()
            mock_response = MagicMock()
            mock_response.status_code = 201
            mock_session_instance.post.return_value = mock_response
            MockSession.return_value = mock_session_instance

            results = queue.Queue()
            config = SimulationConfig(iterations=2, interval_seconds=0)
            threads = []
            for vid in range(1, 6):
                t = threading.Thread(
                    target=simulate_vehicle,
                    args=(vid,),
                    kwargs={"results_queue": results, "config": config},
                )
                threads.append(t)
                t.start()
            for t in threads:
                t.join(timeout=10)

            total_success = 0
            while not results.empty():
                r = results.get()
                total_success += r["successes"]
            assert total_success == 10

    def test_crash_simulation_scenario(self):
        with patch("hardware_sim.sim.requests.Session") as MockSession:
            mock_session_instance = MagicMock()
            mock_response = MagicMock()
            mock_response.status_code = 201
            mock_session_instance.post.return_value = mock_response
            MockSession.return_value = mock_session_instance

            results = queue.Queue()
            config = SimulationConfig(iterations=3, interval_seconds=0, simulate_crash=True)
            simulate_vehicle(vehicle_id=1, results_queue=results, config=config)

            call_kwargs_list = [c.kwargs for c in mock_session_instance.post.call_args_list]
            collision_calls = [c for c in call_kwargs_list if c.get("json", {}).get("event_type") == "collision"]
            assert len(collision_calls) >= 1
