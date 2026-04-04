# Hotel Room Booking System

## Overview

This project is a backend application for managing hotel room bookings. It allows users to register, view available rooms, and create bookings. The system calculates the total cost based on the duration of stay and room pricing.

The application is built using Spring Boot and follows a layered architecture with controllers, services, repositories, and entities.

---

## Features

* Create and manage users
* Add and view rooms
* Book rooms for a specific duration
* Automatic total price calculation
* Fetch booking details with user and room information (DTO)

---

## Tech Stack

* Java
* Spring Boot
* Spring Data JPA
* MySQL
* Lombok

---

## Project Structure

```
com.example.hostelbooking
 ├── controller
 ├── services
 ├── repo
 ├── entity
 ├── dto
 └── HostelbookingApplication
```

---

## Database Design

The system uses three main tables:

* **User**
* **Room**
* **Booking**

Relationships:

* One User → Many Bookings
* One Room → Many Bookings

---

## API Endpoints

### User

**Create User**

```
POST /users/postuser
```

**Get All Users**

```
GET /users/getuser
```

---

### Room

**Add Room**

```
POST /rooms/POSTROOM
```

**Get All Rooms**

```
GET /rooms/GETROOM
```

---

### Booking

**Create Booking**

```
POST /booking/{userId}/{roomId}
```

**Get All Bookings (DTO Response)**

```
GET /booking/GETBOOKING
```

---

## Sample Request & Response

### Create Booking Request

```
POST /booking/1/1
```

```json
{
  "checkIn": "2025-04-05",
  "checkOut": "2025-04-07"
}
```

### Response

```json
[
  {
    "bookingId": 1,
    "userName": "John",
    "roomNumber": "101",
    "roomType": "Deluxe",
    "checkIn": "2025-04-05T00:00:00.000+00:00",
    "checkOut": "2025-04-07T00:00:00.000+00:00",
    "totalAmount": 4000.0
  }
]
```

---

## How to Run

1. Clone the repository
2. Configure database in `application.properties`
3. Run the application:

```
mvn spring-boot:run
```

4. Access APIs via Postman or browser at:

```
http://localhost:8080
```

---

## Notes

* Booking total is calculated based on room price and number of days
* User and Room must exist before creating a booking
* Dates must be valid (check-in < check-out)

---

## Future Improvements

* Input validation
* Exception handling
* Authentication and authorization
* Pagination and filtering
* Payment integration

---
