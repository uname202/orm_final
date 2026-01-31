# orm_final

Платформа для обучения на Spring Boot + JPA (PostgreSQL) с REST API и простым веб-интерфейсом для проверки сэведенных (seed) данных.

## Требования

- Java 21
- Maven 3.9+
- PostgreSQL 14+ (рекомендуется: 16)
- Docker (опционально — для Testcontainers и docker-compose)

## Установка

1) Клонируйте репозиторий и откройте папку проекта.
2) Убедитесь, что PostgreSQL запущен.
3) Создайте базу данных (например: `orm_final`).

## Конфигурация

Приложение читает настройки базы данных из переменных окружения. Если они не заданы, используются значения по умолчанию из `src/main/resources/application.yml`.

Пошагово:

1) Убедитесь, что PostgreSQL запущен и база данных создана (например: `orm_final`).
2) Установите переменные окружения в вашей оболочке:

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

## Запуск локально с помощью Docker Compose

1) Скопируйте `.env.example` в `.env` и при необходимости измените значения.
2) Запустите:

```
docker compose up --build
```

Docker Compose читает `.env` для подстановки переменных и использует следующие значения по умолчанию:
- `DB_URL=jdbc:postgresql://db:5432/orm_final`
- `DB_USER=postgres`
- `DB_PASSWORD=postgres`
- `POSTGRES_DB=orm_final`
- `POSTGRES_USER=postgres`
- `POSTGRES_PASSWORD=postgres`
- `APP_PORT=8080`

Откройте в браузере:
- UI: `http://localhost:8080`
- API: `http://localhost:8080/api/courses`
- <img width="400" height="400" alt="изображение" src="https://github.com/user-attachments/assets/2615620e-18b2-4af5-89bd-4441b2587d14" />


## API

Базовый URL: `/api`

- `GET /api/courses` — список курсов (сводная информация)
- `GET /api/courses/{id}` — курс с модулями и уроками
- `GET /api/categories` — список категорий
- `GET /api/teachers` — список преподавателей

## Архитектура

- `model/` — JPA-сущности, соответствующие ER-диаграмме (15+ сущностей, связи, lazy loading)
- `repository/` — репозитории Spring Data JPA
- `service/` — бизнес-логика для курсов, зачислений, заданий, тестов
- `web/` — REST-контроллеры + DTO
- `config/` — data seeder, вставляющий начальные примерные данные
- `static/index.html` — простой UI, использующий API

## Тестирование

Интеграционные тесты используют Testcontainers и автоматически запускают контейнер PostgreSQL.

```
mvn test
```

## Структура проекта

```
src/main/java/com/example/orm
  OrmFinalApplication.java
  model/          # JPA-сущности + enum'ы
  repository/     # Репозитории Spring Data JPA
  service/        # Сервисы с бизнес-логикой
  web/            # REST-контроллеры + DTO
  config/         # Data seeder
src/main/resources
  application.yml
  static/index.html
src/test/java/com/example/orm
  OrmFinalIntegrationTest.java
```