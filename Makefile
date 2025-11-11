.PHONY: help dev up down build clean logs test

help: ## Show this help message
	@echo 'Usage: make [target]'
	@echo ''
	@echo 'Available targets:'
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  %-15s %s\n", $$1, $$2}' $(MAKEFILE_LIST)

dev: ## Start development environment (PostgreSQL only)
	docker-compose -f docker-compose.dev.yml up -d
	@echo "PostgreSQL is running on localhost:5432"
	@echo "Run 'cd backend && mvn spring-boot:run' to start backend"
	@echo "Run 'cd frontend && npm start' to start frontend"

up: ## Start all services (production mode)
	docker-compose up -d
	@echo "Services are starting..."
	@echo "Frontend: http://localhost:3000"
	@echo "Backend: http://localhost:8080"

down: ## Stop all services
	docker-compose down
	docker-compose -f docker-compose.dev.yml down

build: ## Build all Docker images
	docker-compose build

clean: ## Stop services and remove volumes
	docker-compose down -v
	docker-compose -f docker-compose.dev.yml down -v

logs: ## Show logs from all services
	docker-compose logs -f

test-backend: ## Run backend tests
	cd backend && mvn test

test-frontend: ## Run frontend tests
	cd frontend && npm test

install-frontend: ## Install frontend dependencies
	cd frontend && npm install

install-backend: ## Download backend dependencies
	cd backend && mvn dependency:go-offline

backend-run: ## Run backend locally
	cd backend && mvn spring-boot:run

frontend-run: ## Run frontend locally
	cd frontend && npm start
