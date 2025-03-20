package com.att.tdp.popcorn_palace.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.att.tdp.popcorn_palace.dto.ShowtimeRequestDto;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;

@Service
public class ShowtimeService {

    private static final Logger logger = LoggerFactory.getLogger(ShowtimeService.class);

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository, MovieRepository movieRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
    }

    // Creates a new showtime after verifying that there is no overlapping showtime in the same theater.
    public Showtime createShowtime(ShowtimeRequestDto dto) {
        logger.info("Attempting to create a new showtime in theater: {}", dto.theater);

        validateShowtimeRequest(dto);

        Movie movie = movieRepository.findById(dto.movieId)
            .orElseThrow(() -> {
                logger.error("Movie with ID {} not found", dto.movieId);
                return new ResourceNotFoundException("Movie not found");
            });

        // Check for overlapping showtimes in the same theater.
        boolean overlapping = showtimeRepository.existsOverlappingShowtime(dto);
        if (overlapping) {
            logger.warn("Overlapping showtime detected in theater {}", dto.theater);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Overlapping showtime in the same theater");
        }

        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setTheater(dto.theater);
        showtime.setStartTime(dto.startTime);
        showtime.setEndTime(dto.endTime);
        showtime.setPrice(dto.price);

        Showtime savedShowtime = showtimeRepository.save(showtime);
        logger.info("Showtime successfully created: {}", savedShowtime);
        return savedShowtime;
    }

    // Returns all showtimes.
    public List<Showtime> getAllShowtimes() {
        logger.info("Fetching all showtimes");
        return showtimeRepository.findAll();
    }

    // Returns a specific showtime by its id.
    public Showtime getShowtimeById(Long id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Showtime with ID {} not found", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Showtime not found");
                });
    }

    // Updates an existing showtime with new data, checking for overlapping if times or theater change.
    public Showtime updateShowtime(Long id, ShowtimeRequestDto dto) {
        logger.info("Attempting to update showtime ID: {}", id);

        validateShowtimeRequest(dto);

        Showtime existing = getShowtimeById(id);

        // If any of the time or theater fields change, check for overlapping showtimes.
        if (!existing.getTheater().equals(dto.theater) ||
            !existing.getStartTime().equals(dto.startTime) ||
            !existing.getEndTime().equals(dto.endTime)) {

            boolean overlapping = showtimeRepository.existsOverlappingShowtime(dto);
            if (overlapping) {
                logger.warn("Overlapping showtime detected in theater {}", dto.theater);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Overlapping showtime in the same theater");
            }
        }

        // Update fields
        existing.setTheater(dto.theater);
        existing.setStartTime(dto.startTime);
        existing.setEndTime(dto.endTime);
        existing.setPrice(dto.price);

        Showtime updatedShowtime = showtimeRepository.save(existing);
        logger.info("Showtime ID {} updated successfully", id);
        return updatedShowtime;
    }

    // Deletes a showtime by its id.
    public void deleteShowtime(Long id) {
        logger.info("Attempting to delete showtime ID: {}", id);

        if (!showtimeRepository.existsById(id)) {
            logger.error("Showtime with ID {} not found", id);
            throw new ResourceNotFoundException("Showtime not found");
        }
        
        showtimeRepository.deleteById(id);
        logger.info("Showtime ID {} deleted successfully", id);
    }

    // Helper method to validate ShowtimeRequestDto
    private void validateShowtimeRequest(ShowtimeRequestDto dto) {
        if (dto.theater == null || dto.theater.trim().isEmpty()) {
            logger.error("Invalid theater name: {}", dto.theater);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Theater name is required");
        }
        if (dto.startTime == null || dto.endTime == null) {
            logger.error("Invalid showtime: StartTime or EndTime is null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start and end times are required");
        }
        if (!dto.startTime.isBefore(dto.endTime)) {
            logger.error("Invalid showtime: Start time must be before end time");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start time must be before end time");
        }
        if (dto.price <= 0) {
            logger.error("Invalid ticket price: {}", dto.price);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket price must be positive");
        }
    }
}
