import hashlib
from typing import Dict, Any


class DriverMonitoringEngine:
    DROWSINESS_DARK_THRESHOLD = 60
    DISTRACTION_VARIANCE_THRESHOLD = 40

    def analyze_driver_frame(self, frame_bytes: bytes) -> Dict[str, Any]:
        if not frame_bytes:
            return {
                "drowsiness_detected": False,
                "distraction_detected": False,
                "confidence": 0.0,
                "frame_quality": "empty",
            }

        frame_hash = hashlib.sha256(frame_bytes).digest()
        byte_values = list(frame_bytes[:256]) if len(frame_bytes) >= 256 else list(frame_bytes)

        mean_brightness = sum(byte_values) / len(byte_values) if byte_values else 128
        variance = (
            sum((b - mean_brightness) ** 2 for b in byte_values) / len(byte_values)
            if byte_values
            else 0
        )
        frame_entropy = int.from_bytes(frame_hash[:4], "big") % 100

        drowsiness_detected = mean_brightness < self.DROWSINESS_DARK_THRESHOLD or frame_entropy < 20
        distraction_detected = variance > self.DISTRACTION_VARIANCE_THRESHOLD**2

        drowsiness_confidence = 0.0
        if drowsiness_detected:
            drowsiness_confidence = min(
                0.95, 0.5 + (self.DROWSINESS_DARK_THRESHOLD - mean_brightness) * 0.01
            )

        distraction_confidence = 0.0
        if distraction_detected:
            distraction_confidence = min(0.9, variance / (self.DISTRACTION_VARIANCE_THRESHOLD**2 * 2))

        overall_confidence = max(drowsiness_confidence, distraction_confidence, 0.1)

        return {
            "drowsiness_detected": drowsiness_detected,
            "distraction_detected": distraction_detected,
            "confidence": round(overall_confidence, 3),
            "mean_brightness": round(mean_brightness, 2),
            "frame_quality": "low" if mean_brightness < 30 else "normal",
        }


driver_engine = DriverMonitoringEngine()
