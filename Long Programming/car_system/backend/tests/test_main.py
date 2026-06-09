import pytest


def test_read_root(client):
    response = client.get("/")
    assert response.status_code == 200
    assert response.json()["message"] == "Welcome to the Vehicle Safety Platform API"


def test_health_check(client):
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json()["status"] == "ok"


def test_openapi_docs_accessible(client):
    response = client.get("/api/v1/docs")
    assert response.status_code == 200


def test_openapi_schema_accessible(client):
    response = client.get("/api/v1/openapi.json")
    assert response.status_code == 200
    schema = response.json()
    assert "paths" in schema
    assert "components" in schema
