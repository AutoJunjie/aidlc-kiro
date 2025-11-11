# XHolacracy Backend

Backend service for the XHolacracy Management Platform, built with Spring Boot and following Domain-Driven Design (DDD) principles.

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Spring Security + JWT**
- **Lombok**
- **MapStruct**
- **Maven**

## Project Structure

```
src/main/java/com/xholacracy/
├── domain/                    # Domain Layer (business logic)
│   ├── model/                # Domain models (aggregates, entities, value objects)
│   ├── service/              # Domain services
│   └── event/                # Domain events
├── application/              # Application Layer (use cases)
│   ├── dto/                  # Data Transfer Objects
│   ├── service/              # Application services
│   └── mapper/               # DTO mappers
├── infrastructure/           # Infrastructure Layer (technical details)
│   ├── persistence/          # Repository implementations
│   ├── security/             # Security configuration
│   └── config/               # Application configuration
└── interfaces/               # Interface Layer (API)
    ├── rest/                 # REST controllers
    └── exception/            # Exception handlers
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 15+

### Build

```bash
mvn clean install
```

### Run

```bash
# Development mode
mvn spring-boot:run

# With specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Test

```bash
mvn test
```

## Configuration

Configuration files are located in `src/main/resources/`:

- `application.yml` - Base configuration
- `application-dev.yml` - Development environment
- `application-prod.yml` - Production environment

### Environment Variables

- `SPRING_DATASOURCE_URL` - Database URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `JWT_SECRET` - JWT secret key
- `SPRING_PROFILES_ACTIVE` - Active profile (dev/prod)

## API Documentation

Once the application is running, API documentation is available at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Health Check

- Health endpoint: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`

## Database Migrations

Database migrations are managed by Flyway. Migration scripts are located in `src/main/resources/db/migration/`.

## Development Guidelines

1. Follow DDD principles - keep business logic in the domain layer
2. Use aggregates to maintain consistency boundaries
3. Repository interfaces are defined in the domain layer
4. Application services orchestrate use cases but don't contain business logic
5. Use DTOs for API communication
6. Write unit tests for domain logic
7. Write integration tests for API endpoints

## License

Proprietary - XHolacracy MVP
