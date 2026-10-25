# Inventory and Order Management System

This project is a Spring Boot REST API for inventory and order management with MySQL persistence, transaction-safe stock updates, customer tracking, order processing, and reporting.

## Features
- Product management (create, read, update, delete, search)
- Inventory tracking with stock checks
- Customer management
- Order creation and status tracking
- Transaction-based stock consistency
- Low-stock and sales summary reports
- Validation and centralized error handling
- OpenAPI / Swagger documentation
- Unit tests for order processing

## Tech Stack
- Java 17
- Spring Boot 3.3.x
- Maven
- MySQL
- JPA / Hibernate
- Swagger UI

## Project Structure
- `src/main/java/com/divine/inventorymanagement` – application code
- `src/main/resources/application.properties` – database and app configuration
- `src/main/resources/data.sql` – sample database data for MySQL
- `src/test/java` – test suite

## Prerequisites
- Java 17+
- Maven 3.9+
- MySQL 8+

## Setup
1. Create a MySQL database named `inventory_order_db`.
2. Update the username/password in `src/main/resources/application.properties` if needed.
3. Start MySQL.
4. Run the application:

```bash
mvn spring-boot:run
```

The API will be available at:
- `http://localhost:8080/api/...`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## API Endpoints

### Products
- `POST /api/products`
- `GET /api/products`
- `GET /api/products/{id}`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`

### Customers
- `POST /api/customers`
- `GET /api/customers`
- `GET /api/customers/{id}`
- `PUT /api/customers/{id}`
- `DELETE /api/customers/{id}`

### Orders
- `POST /api/orders`
- `GET /api/orders`
- `GET /api/orders/{id}`
- `PATCH /api/orders/{id}/status?status=CANCELLED`

### Reports
- `GET /api/reports/low-stock?threshold=5`
- `GET /api/reports/sales-summary?lowStockThreshold=5`

## Example Requests

### Create a product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "SKU-2001",
    "name": "Wireless Mouse",
    "description": "Ergonomic wireless mouse",
    "price": 49.99,
    "stockQuantity": 25,
    "category": "Electronics",
    "active": true
  }'
```

### Create a customer
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Michael",
    "lastName": "Brown",
    "email": "michael@example.com",
    "phone": "555-1111",
    "address": "200 High Street"
  }'
```

### Create an order
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "items": [
      { "productId": 1, "quantity": 2 },
      { "productId": 2, "quantity": 1 }
    ]
  }'
```

### Get low-stock products
```bash
curl http://localhost:8080/api/reports/low-stock?threshold=5
```

## Validation and Error Handling
The application handles:
- invalid input fields
- duplicate product/customer entries
- missing resources
- negative or insufficient stock quantities
- order creation failures caused by unavailable inventory

## Testing
Run tests with:
```bash
mvn test
```

## Notes
This project demonstrates strong backend design patterns using Spring Boot, MySQL, transaction management, validation, DTOs, and layered architecture.
