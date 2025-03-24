package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.dto.BookingRequestDto;
import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Integration tests for the BookingService layer to ensure booking-related business logic
// functions as expected when interacting with the database.
@SpringBootTest
public class BookingServiceIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    // Utility method to create and persist a movie for use in tests.
    private Movie createTestMovie(String title) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setGenre("Action");
        movie.setDuration(120);
        movie.setRating(8);
        movie.setReleaseYear(2020);
        return movieRepository.save(movie);
    }

    // Utility method to create and persist a showtime linked to a movie for use in tests.
    private Showtime createTestShowtime(Movie movie, String theater, LocalDateTime start, LocalDateTime end) {
        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setTheater(theater);
        showtime.setStartTime(start);
        showtime.setEndTime(end);
        showtime.setPrice(15.0);
        return showtimeRepository.save(showtime);
    }

    // Verifies that the service prevents booking the same seat in a showtime more than once.
    @SuppressWarnings("null")
    @Test
    public void testPreventDuplicateSeatBooking() {
        Movie movie = createTestMovie("Inception");
        Showtime showtime = createTestShowtime(movie, "Theater A",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2));

        BookingRequestDto dto = new BookingRequestDto();
        dto.setShowtimeId(showtime.getId());
        dto.setSeatNumber(5);
        dto.setCustomerName("Alice");

        Booking booking1 = bookingService.createBooking(dto);
        assertNotNull(booking1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            bookingService.createBooking(dto);
        });
        assertTrue(exception.getReason().contains("already booked"));
    }

    // Tests that a booking can be retrieved by its ID correctly.
    @Test
    public void testFetchBookingById() {
        Movie movie = createTestMovie("Interstellar");
        Showtime showtime = createTestShowtime(movie, "Theater B",
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(3));

        BookingRequestDto dto = new BookingRequestDto();
        dto.setShowtimeId(showtime.getId());
        dto.setSeatNumber(7);
        dto.setCustomerName("Bob");

        Booking created = bookingService.createBooking(dto);
        Booking fetched = bookingService.getBookingById(created.getId());

        assertNotNull(fetched);
        assertEquals("Bob", fetched.getCustomerName());
        assertEquals(7, fetched.getSeatNumber());
    }

    // Tests updating an existing booking with new seat and name values.
    @Test
    public void testUpdateBookingSuccessfully() {
        Movie movie = createTestMovie("The Matrix");
        Showtime showtime = createTestShowtime(movie, "Theater C",
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(3).plusHours(2));

        BookingRequestDto dto = new BookingRequestDto();
        dto.setShowtimeId(showtime.getId());
        dto.setSeatNumber(8);
        dto.setCustomerName("Charlie");

        Booking booking = bookingService.createBooking(dto);

        BookingRequestDto update = new BookingRequestDto();
        update.setShowtimeId(showtime.getId());
        update.setSeatNumber(9);
        update.setCustomerName("Charlie Updated");

        Booking updated = bookingService.updateBooking(booking.getId(), update);

        assertEquals("Charlie Updated", updated.getCustomerName());
        assertEquals(9, updated.getSeatNumber());
    }

    // Tests deletion of a booking and ensures it is no longer retrievable.
    @SuppressWarnings("null")
    @Test
    public void testDeleteBookingSuccessfully() {
        Movie movie = createTestMovie("Avatar");
        Showtime showtime = createTestShowtime(movie, "Theater D",
                LocalDateTime.now().plusDays(4),
                LocalDateTime.now().plusDays(4).plusHours(2));

        BookingRequestDto dto = new BookingRequestDto();
        dto.setShowtimeId(showtime.getId());
        dto.setSeatNumber(3);
        dto.setCustomerName("Dave");

        Booking booking = bookingService.createBooking(dto);

        bookingService.deleteBooking(booking.getId());

         Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.getBookingById(booking.getId());
        });

        assertTrue(exception.getMessage().contains("not found"));
    }

    // Tests that the service can fetch all bookings without error.
    @Test
    public void testGetAllBookingsIntegration() {
        List<Booking> all = bookingService.getAllBookings();
        assertNotNull(all);
        assertTrue(all.size() >= 0); // Confirms query is functional
    }
}
