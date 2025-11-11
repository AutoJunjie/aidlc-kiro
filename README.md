# XHolacracy MVP

A digital platform for Holacracy self-organizing management systems, supporting organizational governance processes, role management, and decision-making mechanisms.

## Overview

XHolacracy MVP enables organizations to practice Holacracy by providing tools for:
- Managing organizational structure (circles and roles)
- Facilitating governance meetings
- Processing proposals through integrative decision-making
- Visualizing organizational hierarchy and role relationships

## Architecture

- **Frontend**: React 18 + TypeScript + Ant Design
- **Backend**: Spring Boot 3 + Java 17 + PostgreSQL
- **Architecture Pattern**: Domain-Driven Design (DDD) with frontend-backend separation

## Project Structure

```
xholacracy-mvp/
├── backend/                # Spring Boot backend
│   ├── src/
│   │   └── main/
│   │       ├── java/com/xholacracy/
│   │       │   ├── domain/          # Domain layer (business logic)
│   │       │   ├── application/     # Application layer (use cases)
│   │       │   ├── infrastructure/  # Infrastructure layer (technical)
│   │       │   └── interfaces/      # Interface layer (API)
│   │       └── resources/
│   │           ├── application.yml
│   │           └── db/migration/    # Flyway migrations
│   ├── pom.xml
│   └── Dockerfile
├── frontend/               # React frontend
│   ├── src/
│   │   ├── components/    # Reusable components
│   │   ├── features/      # Feature modules
│   │   ├── services/      # API services
│   │   ├── types/         # TypeScript types
│   │   └── utils/         # Utility functions
│   ├── package.json
│   └── Dockerfile
├── docs/                  # Documentation
│   ├── adr/              # Architecture Decision Records
│   └── c4models/         # C4 diagrams
├── .kiro/                # Kiro configuration
│   ├── specs/            # Feature specifications
│   └── steering/         # Development guidelines
├── docker-compose.yml     # Production deployment
├── docker-compose.dev.yml # Development environment
└── README.md
```

## Getting Started

### Prerequisites

- **Java 17+** (for backend development)
- **Node.js 18+** (for frontend development)
- **Docker & Docker Compose** (for containerized deployment)
- **PostgreSQL 15+** (if running locally without Docker)
- **Maven 3.6+** (for backend builds)

### Quick Start with Docker

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd xholacracy-mvp
   ```

2. **Start all services**
   ```bash
   docker-compose up -d
   ```

3. **Access the application**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080/api/v1
   - API Health: http://localhost:8080/actuator/health

4. **Stop services**
   ```bash
   docker-compose down
   ```

### Development Setup

For local development with hot-reload:

1. **Start PostgreSQL only**
   ```bash
   docker-compose -f docker-compose.dev.yml up -d
   ```

2. **Run backend**
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   Backend will be available at http://localhost:8080

3. **Run frontend** (in a new terminal)
   ```bash
   cd frontend
   npm install
   npm start
   ```
   Frontend will be available at http://localhost:3000

### Environment Configuration

#### Backend
Create `backend/src/main/resources/application-local.yml` for local overrides:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/xholacracy
    username: holacracy_user
    password: holacracy_pass
```

#### Frontend
Create `frontend/.env.local`:
```
REACT_APP_API_URL=http://localhost:8080/api/v1
```

## Development Commands

### Backend

```bash
cd backend

# Build
mvn clean install

# Run tests
mvn test

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Run database migrations
mvn flyway:migrate
```

### Frontend

```bash
cd frontend

# Install dependencies
npm install

# Start dev server
npm start

# Build for production
npm run build

# Run tests
npm test

# Run linter
npm run lint
```

### Docker

```bash
# Build and start all services
docker-compose up --build

# Start in detached mode
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Remove volumes (clean database)
docker-compose down -v
```

## Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## Documentation

- **Requirements**: `.kiro/specs/holacracy-management/requirements.md`
- **Design**: `.kiro/specs/holacracy-management/design.md`
- **Tasks**: `.kiro/specs/holacracy-management/tasks.md`
- **ADRs**: `docs/adr/` - Architecture Decision Records
- **Business Processes**: `docs/c4models/` - C4 diagrams
- **Glossary**: `holacracy-glossary.md`

## Key Features (MVP Scope)

1. **Organization & Circle Management**
   - Create and manage hierarchical circle structures
   - Visualize circle hierarchy

2. **Role Management**
   - Define roles with purpose, accountabilities, and domains
   - Assign roles to partners
   - Visualize role relationships

3. **Proposal Management**
   - Create proposals to resolve tensions
   - Full integrative decision-making process (6 stages)
   - Track proposal history

4. **Governance Meeting Management**
   - Schedule and facilitate governance meetings
   - Manage meeting agendas
   - Record meeting outcomes

## Technology Decisions

See `docs/adr/` for detailed architecture decisions:
- ADR-001: DDD Architecture
- ADR-002: PostgreSQL Database
- ADR-003: Frontend-Backend Separation
- ADR-004: Aggregate Root Pattern
- ADR-005: React Query for State Management

## Contributing

This is an MVP project. Development follows the task list in `.kiro/specs/holacracy-management/tasks.md`.

## License

Proprietary - XHolacracy MVP

## Support

For questions or issues, please refer to the project documentation in the `.kiro/specs/` directory.
