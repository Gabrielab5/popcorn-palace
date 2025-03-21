package com.att.tdp.popcorn_palace.service;

import java.util.List;

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

import jakarta.validation.Valid;

@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;

    public BookingService(BookingRepository bookingRepository, ShowtimeRepository showtimeRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
    }

    // Create a new booking
    public Booking createBooking(@Valid BookingRequestDto dto) {
        logger.info("Attempting to create a new booking for showtimeId: {}", dto.getShowtimeId());

        validateBookingRequest(dto);
        Showtime showtime = fetchShowtimeById(dto.getShowtimeId());
        checkSeatAvailability(showtime, dto.getSeatNumber());

        Booking booking = buildBookingEntity(dto, showtime);
        return saveBooking(booking);
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        logger.info("Fetching all bookings");
        return bookingRepository.findAll();
    }

    // Get booking by ID
    public Booking getBookingById(Long id) {
        logger.info("Fetching booking with ID: {}", id);
        return bookingRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Booking with ID {} not found", id);
                return new ResourceNotFoundException("Booking not found");
            });
    }

    // Update booking
    public Booking updateBooking(Long id, @Valid BookingRequestDto dto) {
        logger.info("Attempting to update booking with ID: {}", id);

        Booking existingBooking = getBookingById(id);
        validateBookingRequest(dto);

        Showtime showtime = fetchShowtimeById(dto.getShowtimeId());
        if (existingBooking.getSeatNumber() != dto.getSeatNumber()) {
            checkSeatAvailability(showtime, dto.getSeatNumber());
        }

        existingBooking.setShowtime(showtime);
        existingBooking.setSeatNumber(dto.getSeatNumber());
        existingBooking.setCustomerName(dto.getCustomerName());

        return saveBooking(existingBooking);
    }

    // Delete booking by ID
    public void deleteBooking(Long id) {
        logger.info("Attempting to delete booking with ID: {}", id);

        if (!bookingRepository.existsById(id)) {
            logger.error("Booking with ID {} not found", id);
            throw new ResourceNotFoundException("Booking not found");
        }

        bookingRepository.deleteById(id);
        logger.info("Booking with ID {} deleted successfully", id);
    }

    // Validate Booking Request
    private void validateBookingRequest(BookingRequestDto dto) {
        if (dto.getSeatNumber() <= 0) {
            logger.error("Invalid seat number: {}", dto.getSeatNumber());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat number must be positive");
        }

        if (dto.getCustomerName() == null || dto.getCustomerName().trim().isEmpty()) {
            logger.error("Customer name is required");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer name is required");
        }
    }

    // Fetch showtime by ID
    private Showtime fetchShowtimeById(Long showtimeId) {
        return showtimeRepository.findById(showtimeId)
            .orElseThrow(() -> {
                logger.error("Showtime with ID {} not found", showtimeId);
                return new ResourceNotFoundException("Showtime not found");
            });
    }

    // Check if the seat is available
    private void checkSeatAvailability(Showtime showtime, int seatNumber) {
        boolean seatBooked = bookingRepository.existsByShowtimeAndSeatNumber(showtime, seatNumber);
        if (seatBooked) {
            logger.warn("Seat {} for showtime {} is already booked", seatNumber, showtime.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat already booked for this showtime");
        }
    }

    // Build booking entity
    private Booking buildBookingEntity(BookingRequestDto dto, Showtime showtime) {
        Booking booking = new Booking();
        booking.setShowtime(showtime);
        booking.setSeatNumber(dto.getSeatNumber());
        booking.setCustomerName(dto.getCustomerName());
        return booking;
    }

    // Save booking to database
    private Booking saveBooking(Booking booking) {
        try {
            Booking savedBooking = bookingRepository.save(booking);
            logger.info("Booking saved successfully: {}", savedBooking);
            return savedBooking;
        } catch (DataIntegrityViolationException ex) {
            logger.error("Error saving booking: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat already booked");
        }
    }
}
