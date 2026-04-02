Parking Lot Management System
Overview

This project is a Spring Boot–based backend application for managing a parking lot system. It provides RESTful APIs to perform CRUD operations on vehicles and lays the groundwork for managing parking slots and lots.

The system follows a layered architecture with Controller, Service, Repository, and Entity components.

Tech Stack
Java
Spring Boot
Spring Data JPA
Hibernate
REST API
Lombok
Project Structure
com.example.parkinglot
│
├── Controller
│   └── controller.java
│
├── Service
│   └── service.java
│
├── Repository
│   └── repository.java
│
├── DSO (Entities)
│   ├── vehicle.java
│   ├── slot.java
│   └── lot.java
│
└── ParkinglotApplication.java
Features
Create, update, delete, and fetch vehicle records
RESTful API design
Database integration using JPA
Basic entity relationships (Slot → Vehicle)
API Endpoints
Get All Vehicles
GET /api/vehicle

Returns a list of all vehicles
Implemented in controller layer

Add Vehicle
POST /api/update

Request Body:

{
  "vehiclename": "Car",
  "ownername": "John"
}
Update Vehicle
PUT /api/{id}

Updates vehicle details by ID

Delete Vehicle
DELETE /api/{id}

Deletes vehicle by ID

Core Components
Main Application

Entry point for the Spring Boot application


Controller Layer

Handles HTTP requests and routes them to the service layer


Service Layer

Contains business logic for vehicle operations such as save, update, delete


Repository Layer

Handles database operations using JPA


Entities
Vehicle

Stores vehicle details such as name and owner


Slot

Represents parking slots and links to a vehicle


Lot

Represents parking lot metadata


How to Run
Clone the repository
git clone <your-repo-url>
Navigate to project directory
cd parking-lot
Run the application
mvn spring-boot:run
Access API
http://localhost:8080/api
Improvements Needed
Add validation for inputs
Implement exception handling with proper HTTP responses
Expand slot and lot management logic
Add authentication and authorization
Improve naming conventions (class names should follow Java standards: Controller, Service, Repository)