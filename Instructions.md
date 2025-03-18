# Popcorn Palace API - Documentation

## Running the application

docker-compose up -d
./mvnw spring-boot:run


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
./mvnw test

