.PHONY: up down logs test test-backend test-frontend test-all test-load lint install-dev build migrate makemigrations run-dev

up:
	docker-compose up -d

down:
	docker-compose down -v

logs:
	docker-compose logs -f

build:
	docker-compose build

migrate:
	cd backend && ..\venv\Scripts\alembic upgrade head

makemigrations:
	cd backend && ..\venv\Scripts\alembic revision --autogenerate -m "auto"

install-dev:
	.\venv\Scripts\pip install -r backend\requirements.txt

test-backend:
	cd backend && ..\venv\Scripts\pytest tests/ -v --tb=short

test-frontend:
	cd frontend && npm test -- --watchAll=false

test-all: test-backend test-frontend

test-load:
	cd backend && ..\venv\Scripts\locust -f tests\locustfile.py --headless -u 50 -r 5 -t 30s --host http://localhost:8000

lint:
	.\venv\Scripts\ruff check backend\app
	.\venv\Scripts\bandit -r backend\app -ll

run-dev:
	cd backend && ..\venv\Scripts\uvicorn app.main:app --reload --host 0.0.0.0 --port 8000

run-worker:
	cd backend && ..\venv\Scripts\celery -A app.core.celery_app worker --loglevel=info -Q main-queue
