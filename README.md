# LEEDTECH ONE-TIME-PAYMENT APP

A Spring Boot 3.4 REST API for managing students, academic levels, enrollments and payments. The application uses JWT-based stateless authentication with role-based access control.

## Prerequisites

- **Java 23** or higher
- **Maven 3.9+** (or use the included Maven wrapper `./mvnw`)
- **Docker & Docker Compose** (optional, for containerized deployment)
- **Node.js & npm** (for running otp-web frontend)
- **PostgreSQL 18** or **MySQL** (if not using Docker)

---

## Running the Application

### Option 1: Docker Compose (Recommended)

This starts both the API and PostgreSQL database in containers.

```bash
# 1. Copy and configure environment variables
cp .env.example .env
# Edit .env and fill in all required values

# 2. Start containers
docker compose up

# 3. The API will be available at http://localhost:8081
#    Swagger UI at http://localhost:8081/swagger-ui/index.html
```

To stop:
```bash
docker compose down
```

To stop and remove volumes (fresh start):
```bash
docker compose down -v
```

---

### Option 2: Maven Package + JAR

Run the packaged JAR directly after building.

```bash
# 1. Configure environment
cp .env.example .env
# Edit .env with your database credentials

# 2. Build (skip tests)
./mvnw clean package -DskipTests

# 3. Run the JAR
java -jar target/leedtech-otp.jar
```

---

### Option 3: Maven Spring Boot Plugin

Run directly without packaging.

```bash
# 1. Configure environment
cp .env.example .env
# Edit .env with your database credentials

# 2. Run with Maven
./mvnw spring-boot:run
```

---

### Option 4: IDE

Import the project as a Maven project in your IDE (IntelliJ IDEA, VS Code, Eclipse) and run the main class `OtpApplication.java`.

---

## Running Tests

```bash
# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=ClassName

# Run tests with coverage (if configured)
./mvnw test jacoco:report
```

---

## Running the Frontend (otp-web)

The Angular frontend is located in the `otp-web/` directory.

```bash
# Navigate to otp-web directory
cd otp-web

# Install dependencies (first time only)
npm install

# Development server (runs on http://localhost:4200 by default)
npm start

# Production build
npm run build

# Run tests
npm test
```

---

## Configuration

Copy `.env.example` to `.env` and configure:

| Variable | Description | Required |
|----------|-------------|----------|
| `DATABASE_TYPE` | `postgresql` or `mysql` | Yes |
| `DATASOURCE_URL` | JDBC connection URL | Yes |
| `DATASOURCE_USERNAME` | Database username | Yes |
| `DATASOURCE_PASSWORD` | Database password | Yes |
| `JWT_SECRET_KEY` | Secret key for JWT signing | Yes |
| `SECRET_KEY` | Application secret | Yes |
| `MAIL_*` | SMTP configuration for emails | Optional |
| `CORS_ALLOWED_ORIGINS_*` | CORS allowed origins | Yes |

### Database Setup (Without Docker)

1. Comment out the Docker PostgreSQL variables
2. Uncomment either MySQL or PostgreSQL config
3. Create an empty database matching `DATASOURCE_URL`

---

## Key Business Rules

### Academic Levels
- **Academic levels can only be created via API** using `POST /api/v1/secure/admin/academic-levels`
- **Only ADMIN role can create academic levels**
- The web application does not provide a UI for creating academic levels

### Enrollment
- **A user must enroll before making any payments**
- A user can enroll into an academic level **only once**
- Enrollment is done via `POST /api/v1/secure/student/enrollments`

### Payments
- **studentNumber is required to make payments**
- Get your studentNumber from the user profile using `GET /api/v1/secure/student/users/{id}`
- incentiveAmount, totalAmount and newBalance is gotten as: `incentiveAmount=incentiveRate*paymentAmount`), `totalPayment=paymentAmount+incentiveAmount` and `newBalance=oldBalance-totalPayment`
- If `currentBalance<=paymentAmount`, then `incentiveAmount=0`, `totalPayment=balance`  and `newBalance=0` 
- If `balance<=totalPayment` (payment+incentiveAmount) then set `incentive=balance-paymentAmount`,  `totalPayment=balance` and `newBalance=0`

### Getting User Profile (studentNumber)
```http
GET /api/v1/secure/student/users/{userId}
Authorization: Bearer <jwt_token>
```

The response contains the `studentNumber` field needed for payment operations.

---

## API Endpoints Overview

| Endpoint | Method | Auth          | Description |
|----------|--------|---------------|-------------|
| `/api/v1/secure/admin/academic-levels` | POST | ADMIN         | Create academic level |
| `/api/v1/academic-levels` | GET | Public        | List all academic levels |
| `/api/v1/secure/student/enrollments` | POST | STUDENT       | Enroll in academic level |
| `/api/v1/secure/student/users/{id}` | GET | STUDENT/ADMIN | Get user profile (includes studentNumber) |
| `/api/v1/auth/**` | Various | Public        | Authentication endpoints |

Full API documentation available at `/swagger-ui/index.html` when running.

---

## Assumptions & Trade-offs

### Architecture
- **Single mono-repo**: Contains both API (`src/`) and frontend (`otp-web/`) in one repository
- **UUID primary keys**: All entities use UUIDs for IDs
- **Lombok**: Heavy use of Lombok for reducing boilerplate code
- **MapStruct**: For DTO-to-Entity mapping

### Database
- **JPA dialect hardcoded**: Currently set to `PostgreSQLDialect` in `application.properties` (see `src/main/resources/application.properties`)
- **Schema migrations**: Managed via JPA `ddl-auto` in dev; Liquibase commented out but available
- **No migrations for otp-web**: Frontend is pre-built and served statically

### Security
- **JWT stateless authentication**: No server-side session storage
- **Role-based access**: Roles include USER, STUDENT, ADMIN, SUPER_ADMIN
- **Super admin seeding**: A super admin is seeded on first startup via `SetupDataLoader`

### Frontend Limitations
- **otp-web is pre-built**: The `dist/otp-web/` folder contains compiled assets
- **API-only academic level creation**: Admin must use Swagger or direct API calls to create academic levels
- **studentNumber retrieval**: Users must call the profile endpoint to obtain their studentNumber

### Testing
- **Test exclusion**: The Maven surefire plugin excludes `otp-web/**` tests (configured in `pom.xml`)
- **Integration tests**: Tests require a running database or H2 in-memory database

---

## Project Structure

```
leedtech-otp-api/
├── src/
│   ├── main/java/org/leedtech/otp/
│   │   ├── controller/    # REST endpoints
│   │   ├── service/       # Business logic interfaces
│   │   ├── service/impl/  # Business logic implementations
│   │   ├── repository/    # Data access
│   │   ├── entity/        # JPA entities
│   │   ├── domain/        # Domain objects (DTOs)
│   │   ├── mapper/        # MapStruct mappers
│   │   ├── config/        # Security, CORS, etc.
│   │   └── exceptions/    # Exceptions
│   │   └── utils/         # Helpers, 
│   └── test/              # Unit/integration tests
├── otp-web/               # Angular frontend (pre-built)
│   ├── src/               # Angular source
│   └── dist/otp-web/      # Compiled output
├── docker-compose.yml     # Container orchestration
├── Dockerfile            # API container image
├── pom.xml               # Maven configuration
└── .env.example          # Environment template
```

---

## Troubleshooting

### Port already in use
```bash
# Find and kill process using port 8081
lsof -i :8081
kill -9 <PID>
```

### Database connection issues
- Ensure PostgreSQL/MySQL is running
- Verify `DATASOURCE_URL`, username, and password are correct
- Check Docker container is healthy: `docker compose ps`

### JWT token expired
- Tokens expire based on `ACCESS_TOKEN_DURATION_IN_MINUTES` in `.env`
- Re-authenticate to get a new token

### otp-web won't start
```bash
cd otp-web
rm -rf node_modules
npm install
npm start
```
