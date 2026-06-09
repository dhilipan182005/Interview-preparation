from dataclasses import dataclass
from typing import List, Dict, Any


@dataclass
class TelemetryWindow:
    speed: float
    g_force: float = 1.0
    heading_change: float = 0.0
    engine_rpm: int = 0
    event_type: str = "normal"
    severity: int = 0


class AccidentDetectionEngine:
    COLLISION_G_FORCE_THRESHOLD = 4.0
    HARSH_BRAKING_DECELERATION = 15.0
    ROLLOVER_HEADING_THRESHOLD = 45.0
    HIGH_SEVERITY_THRESHOLD = 8

    def analyze_telemetry_window(self, window: List[Dict[str, Any]]) -> Dict[str, Any]:
        if not window:
            return {"is_accident": False, "confidence": 0.0, "event_type": "normal", "severity": 0}

        max_g_force = max(entry.get("g_force", 1.0) for entry in window)
        max_heading_change = max(abs(entry.get("heading_change", 0.0)) for entry in window)
        speeds = [entry.get("speed", 0.0) for entry in window]
        max_deceleration = 0.0
        if len(speeds) > 1:
            for i in range(1, len(speeds)):
                delta = speeds[i - 1] - speeds[i]
                if delta > max_deceleration:
                    max_deceleration = delta
        has_explicit_collision = any(
            entry.get("event_type") in ("collision", "rollover") for entry in window
        )
        has_high_severity = any(
            entry.get("severity", 0) >= self.HIGH_SEVERITY_THRESHOLD for entry in window
        )

        confidence = 0.0
        event_type = "normal"
        severity = 0

        if has_explicit_collision or has_high_severity:
            confidence = 0.95
            event_type = "collision"
            severity = 10
        elif max_g_force >= self.COLLISION_G_FORCE_THRESHOLD:
            confidence = min(0.9, 0.5 + (max_g_force - self.COLLISION_G_FORCE_THRESHOLD) * 0.1)
            event_type = "collision"
            severity = min(10, int(max_g_force * 2))
        elif max_heading_change >= self.ROLLOVER_HEADING_THRESHOLD:
            confidence = 0.75
            event_type = "rollover"
            severity = 8
        elif max_deceleration >= self.HARSH_BRAKING_DECELERATION:
            confidence = min(0.85, 0.4 + max_deceleration * 0.02)
            event_type = "harsh_braking"
            severity = min(7, int(max_deceleration * 0.4))

        is_accident = confidence >= 0.5 and event_type in ("collision", "rollover")
        return {
            "is_accident": is_accident,
            "confidence": round(confidence, 3),
            "event_type": event_type,
            "severity": severity,
            "max_g_force": round(max_g_force, 3),
            "max_deceleration": round(max_deceleration, 3),
        }


accident_engine = AccidentDetectionEngine()
