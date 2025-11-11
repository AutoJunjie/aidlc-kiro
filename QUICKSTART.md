# Quick Start Guide

This guide will help you get the XHolacracy MVP up and running quickly.

## Prerequisites Check

Before starting, ensure you have:

- [ ] Docker and Docker Compose installed
- [ ] Java 17+ (for local backend development)
- [ ] Node.js 18+ (for local frontend development)
- [ ] Maven 3.6+ (for backend builds)

## Option 1: Full Docker Deployment (Recommended for Testing)

This option runs everything in containers.

```bash
# 1. Start all services
docker-compose up -d

# 2. Wait for services to be healthy (about 30-60 seconds)
docker-compose ps

# 3. Check logs if needed
docker-compose logs -f

# 4. Access the application
# Frontend: http://localhost:3000
# Backend API: http://localhost:8080/api/v1
# Health Check: http://localhost:8080/actuator/health

# 5. Stop services when done
docker-compose down
```

## Option 2: Local Development (Recommended for Development)

This option runs only PostgreSQL in Docker, while backend and frontend run locally for hot-reload.

### Step 1: Start PostgreSQL

```bash
# Start PostgreSQL container
docker-compose -f docker-compose.dev.yml up -d

# Verify it's running
docker-compose -f docker-compose.dev.yml ps
```

### Step 2: Run Backend

```bash
# Open a new terminal
cd backend

# Build and run (first time)
mvn clean install
mvn spring-boot:run

# Or use the Makefile
make backend-run
```

Backend will start on http://localhost:8080

### Step 3: Run Frontend

```bash
# Open another terminal
cd frontend

# Install dependencies (first time only)
npm install

# Start development server
npm start

# Or use the Makefile
make frontend-run
```

Frontend will start on http://localhost:3000

## Verification Steps

### 1. Check Backend Health

```bash
curl http://localhost:8080/actuator/health
```

Expected response:
```json
{
  "status": "UP"
}
```

### 2. Check Database Connection

```bash
# Connect to PostgreSQL
docker exec -it xholacracy-postgres-dev psql -U holacracy_user -d xholacracy

# List tables (should show organizations table)
\dt

# Exit
\q
```

### 3. Check Frontend

Open http://localhost:3000 in your browser. You should see:
- "XHolacracy Management Platform" heading
- Welcome message

## Using the Makefile

The project includes a Makefile for convenience:

```bash
# Show all available commands
make help

# Start development environment (PostgreSQL only)
make dev

# Start all services (production mode)
make up

# Stop all services
make down

# View logs
make logs

# Run tests
make test-backend
make test-frontend

# Clean everything (including volumes)
make clean
```

## Troubleshooting

### Backend won't start

1. Check if PostgreSQL is running:
   ```bash
   docker-compose -f docker-compose.dev.yml ps
   ```

2. Check PostgreSQL logs:
   ```bash
   docker-compose -f docker-compose.dev.yml logs postgres
   ```

3. Verify database connection in `backend/src/main/resources/application.yml`

### Frontend won't start

1. Clear node_modules and reinstall:
   ```bash
   cd frontend
   rm -rf node_modules package-lock.json
   npm install
   ```

2. Check if port 3000 is already in use:
   ```bash
   lsof -i :3000
   ```

### Docker issues

1. Check Docker is running:
   ```bash
   docker ps
   ```

2. Clean up Docker resources:
   ```bash
   docker-compose down -v
   docker system prune -a
   ```

## Next Steps

Once the infrastructure is running:

1. Review the project structure in `README.md`
2. Check the requirements in `.kiro/specs/holacracy-management/requirements.md`
3. Review the design in `.kiro/specs/holacracy-management/design.md`
4. Follow the implementation tasks in `.kiro/specs/holacracy-management/tasks.md`

## Development Workflow

1. Start PostgreSQL: `make dev`
2. Run backend: `make backend-run` (in one terminal)
3. Run frontend: `make frontend-run` (in another terminal)
4. Make changes - both will hot-reload
5. Run tests before committing:
   - Backend: `make test-backend`
   - Frontend: `make test-frontend`

## Environment Variables

### Backend

Create `backend/src/main/resources/application-local.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/xholacracy
    username: holacracy_user
    password: holacracy_pass
```

### Frontend

Create `frontend/.env.local`:
```
REACT_APP_API_URL=http://localhost:8080/api/v1
```

## Support

If you encounter issues:
1. Check the logs: `make logs`
2. Review the documentation in `.kiro/specs/`
3. Ensure all prerequisites are installed
4. Try cleaning and restarting: `make clean && make dev`
