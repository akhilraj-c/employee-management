# Employee Management System REST API

[![Java 21](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![OpenAPI 3.0](https://img.shields.io/badge/OpenAPI-3.0-blue.svg)](https://swagger.io/specification/)
[![MySQL](https://img.shields.io/badge/Database-MySQL-blue.svg)](https://www.mysql.com/)

A production-grade RESTful API service built with **Java 21** and **Spring Boot** to manage organizational departments, employee lifecycles, reporting manager hierarchies, relational expansions, and payroll analytics.

---

## 📌 Features & Key Capabilities

- **Employee Lifecycle Management**: Onboard employees, update details, assign roles/titles, manage reporting manager hierarchies, and reassign departments.
- **Department Operations**: Create, view, update, and safely delete departments (with strict relational integrity checks preventing deletion if active employees are assigned).
- **Relational Expansion**: Query department details with inline child employee data via `expand=employee`.
- **Lightweight Employee Lookup**: Optimized query endpoint (`lookup=true`) returning minimal employee metadata (ID & Name) for UI dropdowns/select fields.
- **Department Payroll Analytics**: Compute aggregated organizational metrics per department, including active headcount, total payroll expenditure, average salary, and department head details.
- **Pagination Standard**: Default pagination (20 items/page) with metadata (`page`, `size`, `totalElements`, `totalPages`).
- **Interactive OpenAPI Documentation**: Embedded Swagger UI console and OpenAPI 3.0 JSON specification.
- **Software Design Document (SDD)**: Comprehensive architectural specification, sequence diagrams, ER models, and API contracts documented in [DESIGN.md](DESIGN.md).

---

## 🏗️ Architecture & Technology Stack

- **Framework**: Spring Boot 3.x (Spring WebMVC, Spring Data JPA, Validation)
- **Language**: Java 21
- **Database**: MySQL 8.x
- **ORM & Persistence**: JPA / Hibernate
- **API Documentation**: Springdoc OpenAPI UI (`springdoc-openapi-starter-webmvc-ui:3.1.0`)
- **Build Tool**: Apache Maven

### Code Design & Tiered Architecture
```
com.example.employeemanagement
 ├── config          # OpenAPI & Application Configurations
 ├── controller      # REST Controllers (API Endpoints & Documentation)
 ├── dto             # Data Transfer Objects
 │    ├── request    # Input payloads & Validation annotations
 │    └── response   # Standardized API Envelopes & Response Payloads
 ├── entity          # JPA Entities (Employee & Department)
 ├── exception       # Global Exception Handler & Error Schemas
 ├── mapper          # Entity <-> DTO Mapping Layer
 ├── repository      # Spring Data JPA Repositories
 ├── service         # Business Logic & Analytics Interfaces
 │    └── impl       # Service Implementations
 └── utils           # Pagination Helper Utilities
```

---

## 🚀 Getting Started

### Prerequisites
- **JDK 21** installed and configured in system path.
- **MySQL 8.x** server running locally or accessible remotely.

### Database Setup
1. Create a MySQL database named `employee_management`:
   ```sql
   CREATE DATABASE employee_management;
   ```
2. The repository includes DDL scripts in `src/main/resources`:
   - `schema.sql`: Table structure creation for `departments` and `employees`.
   - `data.sql`: Seed data with **3 departments** and **25 employees**.

---

## ⚙️ Configuration

Application settings can be configured in `src/main/resources/application.yaml` or overridden via environment variables:

| Property | Default Value | Description |
| :--- | :--- | :--- |
| `server.port` | `8081` | Embedded HTTP server port |
| `DB_URL` | `jdbc:mysql://localhost:3306/employee_management` | JDBC Connection URL |
| `DB_USERNAME` | `root` | Database Username |
| `DB_PASSWORD` | *(empty)* | Database Password |

---

## 🛠️ Build & Execution Commands

### Build Project
```bash
./mvnw clean compile
```

### Run Application

Provide your MySQL database password dynamically via environment variables or Maven JVM flags:

#### PowerShell (Windows)
```powershell
$env:DB_PASSWORD="your_password"; ./mvnw spring-boot:run
```

#### Bash / Zsh (Linux / macOS)
```bash
DB_PASSWORD=your_password ./mvnw spring-boot:run
```

#### Windows Command Prompt (CMD)
```cmd
set DB_PASSWORD=your_password && mvnw spring-boot:run
```

#### Via Maven JVM Parameter
```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-DDB_PASSWORD=your_password"
```

### Run Tests
```bash
./mvnw test
```

---

## 📖 API Documentation & Swagger UI

Once the application is running, access the interactive Swagger UI and OpenAPI documentation:

- **Swagger UI Console**: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- **OpenAPI v3 Spec (JSON)**: [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

---

## 📑 REST API Endpoint Summary

### Department Endpoints (`/api/v1/departments`)

| Method | Endpoint | Query Parameters | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/departments` | None | Create a new department with optional department head. |
| `GET` | `/api/v1/departments` | `page` (def: 1), `size` (def: 20) | Fetch paginated list of departments. |
| `GET` | `/api/v1/departments/{id}` | `expand=employee` (optional) | Fetch department by ID. Pass `expand=employee` to expand assigned employees inline. |
| `PUT` | `/api/v1/departments/{id}` | None | Update existing department details. |
| `DELETE` | `/api/v1/departments/{id}` | None | Delete department. *Fails if active employees are assigned.* |
| `GET` | `/api/v1/departments/analytics` | `page` (def: 1), `size` (def: 20) | Fetch aggregated department metrics (headcount, payroll cost, average salary). |

### Employee Endpoints (`/api/v1/employees`)

| Method | Endpoint | Query Parameters | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/employees` | None | Create/Onboard a new employee profile. |
| `GET` | `/api/v1/employees` | `lookup=true` (optional), `page`, `size` | Fetch paginated list of employees. Pass `lookup=true` for lightweight list (ID & Name). |
| `GET` | `/api/v1/employees/{id}` | None | Fetch full employee profile details by ID. |
| `PUT` | `/api/v1/employees/{id}` | None | Update employee information and reporting hierarchy. |
| `PATCH` | `/api/v1/employees/{id}/department` | None | Reassign / move employee to another department. |

---

## ⚡ Example Requests & Usage

### 1. Create a Department
```bash
curl -X POST http://localhost:8081/api/v1/departments \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Quality Assurance"
  }'
```

### 2. Fetch Department with Expanded Employees (`expand=employee`)
```bash
curl -X GET "http://localhost:8081/api/v1/departments/1?expand=employee"
```

### 3. Fetch Employee Lookup List (`lookup=true`)
```bash
curl -X GET "http://localhost:8081/api/v1/employees?lookup=true&page=1&size=20"
```

### 4. Move Employee to Another Department
```bash
curl -X PATCH http://localhost:8081/api/v1/employees/3/department \
  -H "Content-Type: application/json" \
  -d '{
    "departmentId": 2
  }'
```

### 5. Fetch Department Payroll Analytics
```bash
curl -X GET "http://localhost:8081/api/v1/departments/analytics?page=1&size=20"
```

---

## 📄 License
This project is licensed under the Apache 2.0 License.