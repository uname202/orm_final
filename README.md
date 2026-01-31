# orm_final

Spring Boot + JPA (PostgreSQL) learning platform with REST endpoints and a simple UI to verify seeded data.

## Requirements

- Java 21
- Maven 3.9+
- PostgreSQL 14+ (recommended: 16)
- Docker (optional, for Testcontainers and docker-compose)

## Installation

1) Clone the repository and open the project folder.
2) Make sure PostgreSQL is running.
3) Create a database (example: `orm_final`).

## Configuration

The app reads database settings from environment variables. If not set, defaults from `src/main/resources/application.yml` are used.

Step-by-step:

1) Ensure PostgreSQL is running and the database exists (example: `orm_final`).
2) Set the environment variables in your shell:

Bash/Zsh:

```
export DB_URL=jdbc:postgresql://localhost:5432/orm_final
export DB_USER=postgres
export DB_PASSWORD=postgres
```

PowerShell:

```
$env:DB_URL="jdbc:postgresql://localhost:5432/orm_final"
$env:DB_USER="postgres"
$env:DB_PASSWORD="postgres"
```

## Running locally

```
mvn spring-boot:run
```

Open:
- UI: `http://localhost:8080`
- API: `http://localhost:8080/api/courses`
- <img width="400" height="400" alt="изображение" src="https://github.com/user-attachments/assets/2615620e-18b2-4af5-89bd-4441b2587d14" />


## Docker Compose

1) Copy `.env.example` to `.env` and adjust values if needed.
2) Run:

```
docker compose up --build
```

Docker Compose reads `.env` for variable substitution and falls back to the defaults below:
- `DB_URL=jdbc:postgresql://db:5432/orm_final`
- `DB_USER=postgres`
- `DB_PASSWORD=postgres`
- `POSTGRES_DB=orm_final`
- `POSTGRES_USER=postgres`
- `POSTGRES_PASSWORD=postgres`
- `APP_PORT=8080`

## API

Base URL: `/api`

- `GET /api/courses` — list courses (summary)
- `GET /api/courses/{id}` — course with modules and lessons
- `GET /api/categories` — list categories
- `GET /api/teachers` — list teachers

## Architecture

- `model/` — JPA entities matching the ER diagram (15+ entities, relations, lazy loading)
- `repository/` — Spring Data JPA repositories
- `service/` — business logic for courses, enrollments, assignments, quizzes
- `web/` — REST controllers + DTOs
- `config/` — data seeder that inserts initial sample data
- `static/index.html` — simple UI consuming the API

## Testing

Integration tests use Testcontainers and start a PostgreSQL container automatically.

```
mvn test
```

## Project structure

```
src/main/java/com/example/orm
  OrmFinalApplication.java
  model/          # JPA entities + enums
  repository/     # Spring Data JPA repositories
  service/        # Business logic services
  web/            # REST controllers + DTOs
  config/         # Data seeder
src/main/resources
  application.yml
  static/index.html
src/test/java/com/example/orm
  OrmFinalIntegrationTest.java
```

