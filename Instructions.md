# Popcorn Palace API - Documentation

# Instructions.md

## Overview
This document explains the structure of the Popcorn Palace project, its architectural choices, how CRUD operations are implemented, and provides instructions on how to run, build, and test the project. The project is built using Spring Boot and follows a layered architecture for better separation of concerns and maintainability.

---

## Project Structure

The project is divided into the following main directories:

- **src/main/java/com/att/tdp/popcorn_palace**
  - **entity/**  
    Contains JPA entity classes (e.g., `Movie.java`, `Showtime.java`, `Booking.java`).  
    *Purpose:* Each entity represents a table in the database and holds the data structure along with necessary JPA annotations (like `@Entity`, `@Id`).

  - **repository/**  
    Contains interfaces extending `JpaRepository` (e.g., `MovieRepository.java`, `ShowtimeRepository.java`, `BookingRepository.java`).  
    *Purpose:* These repositories provide built-in CRUD operations and query methods for interacting with the database.

  - **service/**  
    Contains service classes (e.g., `MovieService.java`, `ShowtimeService.java`, `BookingService.java`).  
    *Purpose:* Service layer encapsulates business logic, performs input validation, handles exceptions, and uses repositories to perform CRUD operations.

  - **controller/**  
    Contains REST controllers (e.g., `MovieController.java`, `ShowtimeController.java`, `BookingController.java`).  
    *Purpose:* Controllers expose the API endpoints, handle HTTP requests/responses, and delegate operations to the service layer.

  - **dto/**  
    Contains Data Transfer Objects used for input/output operations (e.g., `MovieRequestDto.java`, `MovieResponseDto.java`, `ShowtimeRequestDto.java`, `ShowtimeResponseDto.java`, `BookingRequestDto.java`).  
    *Purpose:* DTOs help decouple the API layer from the persistence layer and allow validation of input data using annotations like `@NotNull`, `@NotBlank`, `@Min`, etc.

  - **exception/**  
    Contains custom exception classes (e.g., `ResourceNotFoundException.java`).  
    *Purpose:* These are used to provide clear error messages and HTTP status codes when business rules are violated.

  - **PopcornPalaceApplication.java**  
    The main entry point of the Spring Boot application.  
    *Purpose:* Contains the `main` method that launches the Spring Boot application.

- **src/test/java/com/att/tdp/popcorn_palace**
  - Contains test classes for unit and integration tests (using JUnit 5 and MockMvc).  
  *Purpose:* Tests ensure that each part of the application (controllers, services, repositories) works as expected.

---

## Architectural Choices and CRUD Implementation

### Why the Project is Divided This Way?
- **Separation of Concerns:**  
  Each layer (Controller, Service, Repository) has its distinct responsibility, making the code easier to manage, test, and extend.
  
- **Maintainability and Scalability:**  
  Using DTOs and services ensures that any changes in business logic or API specifications do not directly affect the database layer.
  
- **Testability:**  
  The separation allows for isolated testing of controllers (via MockMvc) and services, leading to more robust code.

### How CRUD Works in the Project
- **Create:**  
  - **Controller:** Receives a POST request with a DTO, validates the input.
  - **Service:** Converts the DTO to an Entity, performs any business validations (e.g., checking for overlapping showtimes, duplicate bookings), then saves the entity using the Repository.
  - **Repository:** Uses Spring Data JPA to persist the entity in the database.
  
- **Read:**  
  - **Controller:** Handles GET requests (all or by id).
  - **Service:** Retrieves the data using the Repository.
  - **Repository:** Executes queries (e.g., `findAll()`, `findById()`) to fetch data.
  
- **Update:**  
  - **Controller:** Receives a PUT request with updated DTO data and the entity identifier.
  - **Service:** Retrieves the existing entity, updates its fields, applies business logic validations, and then saves the changes via the Repository.
  
- **Delete:**  
  - **Controller:** Handles DELETE requests by id.
  - **Service:** Verifies the existence of the entity and then deletes it using the Repository.

---

## Running the Project

### 1. Running the Database

The project uses PostgreSQL. You can run PostgreSQL using Docker.

**Using Docker Run (Windows CMD):**

docker run --name popcorn-palace-db -e POSTGRES_USER=popcorn-palace -e POSTGRES_PASSWORD=popcorn-palace -e POSTGRES_DB=popcorn-palace -p 5432:5432 -d postgres

Verify the container is running with -
   docker ps
To connect to PostgreSQL for debugging -    
    docker exec -it popcorn-palace-db psql -U popcorn-palace -d popcorn-palace

## Running the application

mvnw.cmd clean package -DskipTests
mvnw.cmd spring-boot:run


## Endpoints

### Movies:
- `POST /api/movies`
- `GET /api/movies`
- `PUT /api/movies/{id}`
- `DELETE /api/movies/{id}`

### Showtimes:
- `POST /api/Showtimes`
- `GET /api/Showtimes`
- `PUT /api/Showtimes/{id}`
- `DELETE /api/Showtimes/{id}`

### Bookings:
- `POST /api/Bookings`
- `GET /api/Bookings`
- `PUT /api/Bookings/{id}`
- `DELETE /api/Bookings/{id}`

## Testing
mvnw.cmd test

