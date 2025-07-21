# Project Structure

This document explains the directory and package structure of the Spring Boot-based Inventory Management Service.

## Main Directory Layout

```
inventory-management/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/inventory/
│   │   │        ├── controller/
│   │   │        ├── service/
│   │   │        ├── service/impl/
│   │   │        ├── repository/
│   │   │        ├── model/
│   │   │        ├── dto/
│   │   │        └── exception/
│   │   └── resources/
│   │        └── application.yml
│   └── test/
│       └── java/
│            └── com/example/inventory/
│                 ├── controller/
│                 └── service/
│                 └── service/impl/
├── pom.xml
├── README.md
└── Project-Structure.md
```

## Package Purposes

### `controller`
- Contains REST controllers for handling HTTP requests and responses for each resource (Category, Product, SKU).
- Maps API endpoints and handles request validation.

### `service`
- Defines service interfaces for business logic related to each domain (CategoryService, ProductService, SkuService).
- Promotes separation of concerns and testability.

### `service.impl`
- Contains concrete implementations of the service interfaces.
- Implements business logic, validation, and interaction with repositories.

### `repository`
- Contains Spring Data JPA repositories for data access (CRUD, queries) for each entity.
- Interfaces extend `JpaRepository` for standard and custom queries.

### `model`
- Contains JPA entity classes representing the database tables (Category, Product, Sku).
- Defines relationships and persistence annotations.

### `dto`
- Contains Data Transfer Objects for API requests and responses.
- Used to decouple API models from database entities and for validation.

### `exception`
- Contains custom exception classes (e.g., `CustomValidationException`) and the global exception handler (`GlobalExceptionHandler`).
- Centralizes error handling and custom business rule exceptions.

## Resource Files

### `application.yml`
- Main Spring Boot configuration file.
- Configures the H2 in-memory database, JPA settings, and enables the H2 console.
- Can be extended for other environment-specific settings.

## Test Structure

- All unit and integration tests are placed under `src/test/java/com/example/inventory/`.
- Controller tests are in `controller/` (e.g., `CategoryControllerTest`).
- Service tests are in `service/` or `service/impl/` (e.g., `ProductServiceImplTest`, `SkuServiceImplTest`).
- Tests use JUnit 5, Mockito, and MockMvc for coverage and validation.

## Additional Notes

- **Config or Util Classes**: If present, these would typically be placed in a `config/` or `util/` package under `com.example.inventory`. (Not present in this project by default.)
- **Swagger/OpenAPI**: API documentation is auto-generated via annotations in controller classes and the main application class.

---

This structure follows best practices for clean, maintainable, and testable Spring Boot applications. 