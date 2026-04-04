# Billing System Application

## Overview

This project is a Spring Boot–based Billing System that provides REST APIs to manage Users, Products, and Billing records. It supports full CRUD operations and includes business logic to calculate product pricing and generate structured billing responses.

The system returns billing details in a clean format including user name, product name, price, and total price.

---

## Tech Stack

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* MySQL
* REST API
* HTML (for frontend testing)

---

## Project Structure

```
org.example.billingsystem
│
├── controller       # Handles HTTP requests
├── service          # Business logic layer
├── repo             # Database access layer
├── entity           # JPA entities
├── dto              # Response DTOs
└── BillingsystemApplication.java
```

---

## Features

### Product Management

* Add Product with price
* View Products
* Update Product
* Delete Product

### User Management

* Add User
* View Users
* Update User
* Delete User

### Billing Management

* Create Bill
* View Bills with formatted output
* Delete Bill

---

## API Endpoints

### Product APIs

| Method | Endpoint             | Description      |
| ------ | -------------------- | ---------------- |
| GET    | /product/getproduct  | Get all products |
| POST   | /product/postproduct | Add product      |
| PUT    | /product/{id}        | Update product   |
| DELETE | /product/{id}        | Delete product   |

---

### User APIs

| Method | Endpoint        | Description   |
| ------ | --------------- | ------------- |
| GET    | /users/api      | Get all users |
| POST   | /users/postuser | Add user      |
| PUT    | /users/{id}     | Update user   |
| DELETE | /users/{id}     | Delete user   |

---

### Billing APIs

| Method | Endpoint             | Description               |
| ------ | -------------------- | ------------------------- |
| GET    | /billing/getbilling  | Get all bills (formatted) |
| POST   | /billing/postbilling | Create bill               |
| DELETE | /billing/{id}        | Delete bill               |

---

## Billing Response Format

Billing API returns structured data:

```
{
  "billingId": 1,
  "billerName": "Admin",
  "userName": "John",
  "productName": "Pen",
  "price": 20.0,
  "totalPrice": 20.0
}
```

---

## Setup Instructions

### 1. Clone the Repository

```
git clone <repository-url>
cd billing-system
```

---

### 2. Configure Database

Update `application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/BillingSystem
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

---

### 3. Run the Application

```
mvn spring-boot:run
```

Application runs at:

```
http://localhost:8080
```

---

## Testing the Application

### Using Postman

1. Add Product
2. Add User
3. Create Billing

Use JSON format for POST and PUT requests.

---

## Sample JSON

### Product

```
{
  "productName": "Pen",
  "quantity": "10",
  "price": 20.0
}
```

---

### User

```
{
  "name": "John",
  "d_T": "2026-04-02 10:30:00"
}
```

---

### Billing

```
{
  "billerName": "Admin",
  "user": {
    "userId": 1
  },
  "product": {
    "productId": 1
  }
}
```

---

## Notes

* Ensure database is running before starting the application
* Product and User must exist before creating Billing
* JSON field names must match entity fields
* Billing automatically calculates total price based on product price

---

## Future Improvements

* Add quantity-based billing (price × quantity)
* Add validation and exception handling
* Implement DTO for all entities
* Improve UI design
* Add authentication and authorization
* Generate invoice reports

---

## License

This project is for educational purposes.
