# Popcorn Palace API - Documentation

## Overview

Popcorn Palace is a Spring Boot RESTful API that manages:

- Movies
- Showtimes
- Bookings

The system ensures:

- No overlapping showtimes per theater
- Unique seat bookings per showtime
- Full CRUD operations with validation and error handling

This document explains the structure of the Popcorn Palace project, its architectural choices, how CRUD operations are implemented, and provides instructions on how to build, run, and test the project.

## Prerequisites

Make sure you have the following installed:

- Java 17+
- Maven 3.6+
- Spring Boot 3.0+
- IDE (e.g., IntelliJ, VSCode)
- PostgreSQL (or Docker to run it easily)

## Project Structure

The project follows a layered architecture:

- `src/main/java/com/att/tdp/popcorn_palace`

  - `entity/`  
    Contains JPA entity classes (`Movie.java`, `Showtime.java`, `Booking.java`).  
    These classes map to the database tables and are annotated with JPA annotations like `@Entity`, `@Id`.

  - `repository/`  
    Contains interfaces extending `JpaRepository` (`MovieRepository.java`, `ShowtimeRepository.java`, `BookingRepository.java`).  
    These provide built-in methods to perform database operations.

  - `service/`  
    Contains service classes (`MovieService.java`, `ShowtimeService.java`, `BookingService.java`).  
    Responsible for business logic, validation, and communication with repositories.

  - `controller/`  
    Contains REST controllers (`MovieController.java`, `ShowtimeController.java`, `BookingController.java`, `HomeController.java`).  
    These expose HTTP endpoints and map requests to service methods.

  - `dto/`  
    Contains Data Transfer Objects like `MovieRequestDto.java`, `MovieResponseDto.java`, `ShowtimeRequestDto.java`, `ShowtimeResponseDto.java`, `BookingRequestDto.java`, and `BookingResponseDto.java`.  
    DTOs isolate the API layer from the database layer and enforce validation constraints using annotations like `@NotBlank`, `@NotNull`, `@Min`.

  - `exception/`  
    Contains custom exceptions such as `ResourceNotFoundException.java`.  
    Used to throw domain-specific errors when resources are not found or validations fail.

  - `PopcornPalaceApplication.java`  
    The entry point of the application that runs the Spring Boot context.

- `src/test/java/com/att/tdp/popcorn_palace`  
  Contains unit and integration tests. Use JUnit and Spring’s MockMvc for testing controllers and services.

---

## Architectural Choices and CRUD Implementation

### Why This Structure?

- **Separation of Concerns**  
  Different layers handle different responsibilities. Controllers manage HTTP logic, Services handle business logic, Repositories handle persistence logic.

- **Scalability and Maintainability**  
  Each component can evolve independently. DTOs decouple the internal model from the API contracts.

- **Testability**  
  Each layer can be tested in isolation. For example, you can mock repositories to test services.

### How CRUD Works

- **Create**  
  POST request with a request DTO → Validated → Converted to Entity → Persisted via repository

- **Read**  
  GET request → Service fetches entity/entities → Converted to response DTO(s)

- **Update**  
  PUT request with DTO and ID → Service fetches entity → Updates fields → Saves changes

- **Delete**  
  DELETE request with ID → Service checks existence → Deletes using repository

---

## Running the Project

### 1. Start the Database with Docker

You can use Docker to run PostgreSQL with the correct configuration:

docker run --name popcorn-palace-db \
  -e POSTGRES_USER=popcorn-palace \
  -e POSTGRES_PASSWORD=popcorn-palace \
  -e POSTGRES_DB=popcorn-palace \
  -p 5432:5432 \
  -d postgres

Verify it's running:
docker ps

Connect to it (optional):
docker exec -it popcorn-palace-db psql -U popcorn-palace -d popcorn-palace

### 2. Configure application.properties

In src/main/resources/application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/popcorn-palace
spring.datasource.username=popcorn-palace
spring.datasource.password=popcorn-palace
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

### 3. Build the Project

From the project root:

mvnw.cmd clean package -DskipTests

Or (if you're on Mac/Linux):

./mvnw clean package -DskipTests

### 4. Run the Application

From the project root:

mvnw.cmd spring-boot:run

Or (if you're on Mac/Linux):

./mvnw spring-boot:run

## API Endpoints

# Movies

POST /api/movies – Create a new movie

GET /api/movies – Get all movies

GET /api/movies/{id} – Get a movie by ID

PUT /api/movies/{id} – Update a movie

DELETE /api/movies/{id} – Delete a movie

# Showtimes

POST /api/showtimes – Create a new showtime

GET /api/showtimes – Get all showtimes

GET /api/showtimes/{id} – Get a showtime by ID

PUT /api/showtimes/{id} – Update a showtime

DELETE /api/showtimes/{id} – Delete a showtime

# Bookings

POST /api/bookings – Create a new booking

GET /api/bookings – Get all bookings

GET /api/bookings/{id} – Get a booking by ID

PUT /api/bookings/{id} – Update a booking

DELETE /api/bookings/{id} – Delete a booking

# Testing

To run all unit and integration tests:

mvnw.cmd test  or   ./mvnw test
