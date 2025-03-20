package com.att.tdp.popcorn_palace.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.att.tdp.popcorn_palace.dto.BookingRequestDto;
import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;

@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;

    public BookingService(BookingRepository bookingRepository, ShowtimeRepository showtimeRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
    }

    public Booking createBooking(BookingRequestDto dto) {
        logger.info("Attempting to create a new booking for showtimeId: {}", dto.showtimeId);

        // Validate seat number
        if (dto.seatNumber <= 0) {
            logger.error("Invalid seat number: {}", dto.seatNumber);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat number must be positive");
        }

        // Validate customer name
        if (dto.customerName == null || dto.customerName.trim().isEmpty()) {
            logger.error("Customer name is required");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer name is required");
        }

        Showtime showtime = showtimeRepository.findById(dto.showtimeId)
            .orElseThrow(() -> {
                logger.error("Customer name is required");
                throw new ResourceNotFoundException("Showtime not found");
            });

        // Check if the seat is already booked for this showtime
        boolean seatBooked = bookingRepository.existsByShowtimeAndSeatNumber(showtime, dto.seatNumber);
        if (seatBooked) {
            logger.warn("Seat {} for showtime {} is already booked", dto.seatNumber, dto.showtimeId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat already booked for this showtime");
        }
        
        Booking booking = new Booking();
        booking.setShowtime(showtime);
        booking.setSeatNumber(dto.seatNumber);
        booking.setCustomerName(dto.customerName);

        try {
            Booking savedBooking = bookingRepository.save(booking);
            logger.info("Booking created successfully: {}", savedBooking);
            return savedBooking;
        } catch (DataIntegrityViolationException ex) {
            logger.error("Error saving booking: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat already booked");
        }
    }

    public List<Booking> getAllBookings() {
        logger.info("Fetching all bookings");
        return bookingRepository.findAll();
    }

    public void deleteBooking(Long id) {
        logger.info("Attempting to delete booking with ID: {}", id);
        
        if (!bookingRepository.existsById(id)){
            logger.error("Booking with ID {} not found", id);
            throw new ResourceNotFoundException("Booking not found");
        }
        
        bookingRepository.deleteById(id);
        logger.info("Booking with ID {} deleted successfully", id);
    }
}
