# Library Management System

## Overview

This project is a backend-driven Library Management System built using Spring Boot and MySQL. It provides RESTful APIs to manage books and users, supporting full CRUD operations.

The system is designed to demonstrate clean backend architecture, database integration, and API handling without relying on frontend frameworks.

---

## Tech Stack

* Java 17
* Spring Boot
* Spring Data JPA
* MySQL
* Maven

---

## Features

### Book Management

* Add new books
* View all books
* Update existing books
* Delete books

### User Management

* Create users
* Retrieve user list
* Update user details
* Delete users

---

## Project Structure

```
src/main/java/org/example/libraries
│
├── Controller      # REST Controllers
├── Service         # Business Logic
├── Repo            # JPA Repositories
├── Entity          # Database Models
└── LibrariesApplication.java
```

---

## API Endpoints

### Book APIs

| Method | Endpoint         | Description   |
| ------ | ---------------- | ------------- |
| GET    | /books/getbooks  | Get all books |
| POST   | /books/postbooks | Add a book    |
| PUT    | /books/{id}      | Update a book |
| DELETE | /books/{id}      | Delete a book |

### User APIs

| Method | Endpoint        | Description   |
| ------ | --------------- | ------------- |
| GET    | /users/api      | Get all users |
| POST   | /users/postuser | Create user   |
| PUT    | /users/{id}     | Update user   |
| DELETE | /users/{id}     | Delete user   |

---

## Database Configuration

Update `application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/libraries
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Running the Application

1. Start MySQL server
2. Create database:

   ```
   CREATE DATABASE libraries;
   ```
3. Run the application:

   ```
   mvn spring-boot:run
   ```
4. Access APIs at:

   ```
   http://localhost:8080
   ```

---

## Testing APIs

Use Postman or any API client.

Example:

### Create User

```
POST /users/postuser
```

```json
{
  "name": "Dhilip",
  "password": "1234"
}
```

### Update User

```
PUT /users/1
```

```json
{
  "name": "dhilipan",
  "password": "1234"
}
```

---

## Notes

* Ensure MySQL is running before starting the application
* IDs must exist before updating or deleting records
* API follows REST principles but uses custom endpoint naming

---

## Future Improvements

* Standardize REST endpoints
* Add validation and exception handling
* Implement authentication (login system)
* Connect frontend UI

---

## Author

Dhilipan S
