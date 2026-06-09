import hashlib
import secrets
import hmac


def _hash_with_salt(password: str, salt: str) -> str:
    key = hashlib.pbkdf2_hmac(
        "sha256",
        password.encode("utf-8"),
        salt.encode("utf-8"),
        iterations=260000,
        dklen=32,
    )
    return key.hex()


def get_password_hash(password: str) -> str:
    salt = secrets.token_hex(16)
    hashed = _hash_with_salt(password, salt)
    return f"pbkdf2:{salt}:{hashed}"


def verify_password(plain_password: str, hashed_password: str) -> bool:
    try:
        parts = hashed_password.split(":")
        if len(parts) != 3 or parts[0] != "pbkdf2":
            return False
        _, salt, stored_hash = parts
        computed = _hash_with_salt(plain_password, salt)
        return hmac.compare_digest(computed, stored_hash)
    except Exception:
        return False
