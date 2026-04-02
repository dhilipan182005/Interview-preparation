# Billing System Application

## Overview

This project is a Spring Boot–based Billing System that provides REST APIs to manage Users, Products, and Billing records. It supports basic CRUD operations and can be tested using tools like Postman or integrated with a frontend interface.

---

## Tech Stack

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* MySQL (or any compatible relational database)
* REST API
* HTML (for basic frontend testing)

---

## Project Structure

```
org.example.billingsystem
│
├── controller       # Handles HTTP requests
├── service          # Business logic layer
├── repo             # Database access layer
├── entity           # JPA entities
└── BillingsystemApplication.java
```

---

## Features

### Product Management

* Add Product
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
* View Bills
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

| Method | Endpoint             | Description   |
| ------ | -------------------- | ------------- |
| GET    | /billing/getbilling  | Get all bills |
| POST   | /billing/postbilling | Create bill   |
| DELETE | /billing/{id}        | Delete bill   |

---

## Setup Instructions

### 1. Clone the Repository

```
git clone <repository-url>
cd billing-system
```

### 2. Configure Database

Update `application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/BillingSystem
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

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

* Send requests to the endpoints listed above
* Use JSON format for POST and PUT requests

### Using HTML Frontend

* Open the HTML file in a browser
* Perform operations using UI buttons

---

## Sample JSON

### Product

```
{
  "productName": "Laptop",
  "quantity": "5"
}
```

### User

```
{
  "name": "John",
  "d_T": "2026-04-02 10:30:00"
}
```

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

* Ensure the database is running before starting the application
* JSON field names must match entity field names exactly
* Billing requires existing User and Product IDs
* Create Product and User before creating Billing

---

## Future Improvements

* Add validation and error handling
* Implement DTO pattern
* Improve UI design
* Add authentication and authorization
* Enhance entity relationships and mapping

---

## License

This project is for educational purposes.
