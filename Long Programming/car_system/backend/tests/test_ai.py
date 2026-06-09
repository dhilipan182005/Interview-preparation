import pytest
from app.services.ai.accident import accident_engine
from app.services.ai.driver import driver_engine


class TestAccidentDetectionEngine:
    def test_normal_data_no_accident(self):
        result = accident_engine.analyze_telemetry_window([{"speed": 60, "g_force": 1.0}])
        assert "is_accident" in result
        assert "confidence" in result
        assert result["is_accident"] is False
        assert result["event_type"] == "normal"

    def test_empty_window_no_accident(self):
        result = accident_engine.analyze_telemetry_window([])
        assert result["is_accident"] is False
        assert result["confidence"] == 0.0

    def test_high_gforce_triggers_collision(self):
        result = accident_engine.analyze_telemetry_window([{"speed": 60, "g_force": 6.0}])
        assert result["is_accident"] is True
        assert result["event_type"] == "collision"
        assert result["confidence"] > 0.5

    def test_explicit_collision_event_type(self):
        result = accident_engine.analyze_telemetry_window(
            [{"speed": 50, "g_force": 1.5, "event_type": "collision"}]
        )
        assert result["is_accident"] is True
        assert result["confidence"] >= 0.95

    def test_high_severity_triggers_accident(self):
        result = accident_engine.analyze_telemetry_window(
            [{"speed": 40, "g_force": 1.0, "severity": 9}]
        )
        assert result["is_accident"] is True

    def test_harsh_braking_detected(self):
        result = accident_engine.analyze_telemetry_window(
            [
                {"speed": 80, "g_force": 1.0},
                {"speed": 55, "g_force": 1.2},
                {"speed": 30, "g_force": 1.5},
            ]
        )
        assert result["event_type"] == "harsh_braking"
        assert result["is_accident"] is False

    def test_rollover_detected(self):
        result = accident_engine.analyze_telemetry_window(
            [{"speed": 40, "g_force": 2.0, "heading_change": 90.0}]
        )
        assert result["event_type"] == "rollover"
        assert result["is_accident"] is True

    def test_confidence_below_threshold_not_accident(self):
        result = accident_engine.analyze_telemetry_window([{"speed": 30, "g_force": 1.2}])
        assert result["confidence"] < 0.5

    def test_result_has_all_required_keys(self):
        result = accident_engine.analyze_telemetry_window([{"speed": 60, "g_force": 1.0}])
        required_keys = {"is_accident", "confidence", "event_type", "severity"}
        assert required_keys.issubset(result.keys())

    def test_multiple_datapoints_use_worst_case(self):
        window = [
            {"speed": 60, "g_force": 1.0},
            {"speed": 58, "g_force": 5.5},
            {"speed": 50, "g_force": 1.2},
        ]
        result = accident_engine.analyze_telemetry_window(window)
        assert result["is_accident"] is True
        assert result["max_g_force"] == pytest.approx(5.5, rel=1e-3)


class TestDriverMonitoringEngine:
    def test_normal_frame_no_detection(self):
        frame = bytes([128] * 512)
        result = driver_engine.analyze_driver_frame(frame)
        assert "drowsiness_detected" in result
        assert "distraction_detected" in result
        assert "confidence" in result

    def test_empty_frame_returns_safe_defaults(self):
        result = driver_engine.analyze_driver_frame(b"")
        assert result["drowsiness_detected"] is False
        assert result["distraction_detected"] is False
        assert result["confidence"] == 0.0

    def test_dark_frame_triggers_drowsiness(self):
        dark_frame = bytes([10] * 512)
        result = driver_engine.analyze_driver_frame(dark_frame)
        assert result["drowsiness_detected"] is True
        assert result["confidence"] > 0.0

    def test_result_has_all_required_keys(self):
        result = driver_engine.analyze_driver_frame(bytes([100] * 100))
        required_keys = {"drowsiness_detected", "distraction_detected", "confidence"}
        assert required_keys.issubset(result.keys())

    def test_high_variance_frame_triggers_distraction(self):
        alternating = bytes([0 if i % 2 == 0 else 255 for i in range(512)])
        result = driver_engine.analyze_driver_frame(alternating)
        assert result["distraction_detected"] is True

    def test_confidence_is_float_between_0_and_1(self):
        frame = bytes([100] * 256)
        result = driver_engine.analyze_driver_frame(frame)
        assert 0.0 <= result["confidence"] <= 1.0
