# Inventory Management Service

## Project Overview
This is a Spring Boot-based E-commerce Inventory Management Service that provides RESTful APIs to manage Categories, Products, and SKUs. It supports CRUD operations, validation, error handling, and is documented with Swagger/OpenAPI. The service uses an in-memory H2 database for easy development and testing.

## Technologies Used
- Java 8
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- springdoc-openapi (Swagger UI)
- JUnit 5, Mockito, MockMvc
- JaCoCo (code coverage)
- Maven

## Features
- **Category APIs**: Create, read, update, delete, and list categories with validation.
- **Product APIs**: CRUD for products, search by name, filter by category, pagination, and validation.
- **SKU APIs**: Add, update, delete, and list SKUs for products, with uniqueness and field validation.
- **Global Error Handling**: Consistent error responses for validation and business logic errors.
- **Swagger/OpenAPI Documentation**: All endpoints are documented and testable via Swagger UI.
- **Unit Tests**: Comprehensive tests for controllers and services.
- **Code Coverage**: JaCoCo integration for coverage reports.

## Setup Instructions
1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd InventoryManagement/inventory-management
   ```
2. **Build the project with Maven**
   ```bash
   mvn clean install
   ```
3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

## Running with H2 Console
- The application uses an in-memory H2 database by default.
- Access the H2 console at: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:inventorydb`
- Username: `sa` (no password)

## Accessing Swagger API Docs
- After starting the application, open:
  - [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
  - or [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- Explore and test all API endpoints interactively.

## Running Unit Tests & Viewing Coverage
1. **Run all unit tests**
   ```bash
   mvn test
   ```
2. **View JaCoCo code coverage report**
   - After running tests, open:
     - `target/site/jacoco/index.html` in your browser
   - Ensure at least 80% coverage for controllers and services.

---

**Happy coding!** 