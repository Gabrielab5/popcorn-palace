package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.dto.BookingRequestDto;
import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingServiceIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void testDuplicateBookingPrevention() {
        // creating a movie
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie = movieRepository.save(movie);

        // creating a showtime
        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setTheater("Theater 1");
        showtime.setStartTime(LocalDateTime.now().plusDays(1));
        showtime.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        showtime.setPrice(10.0);
        showtime = showtimeRepository.save(showtime);

        // creating first booking
        BookingRequestDto dto = new BookingRequestDto();
        dto.setShowtimeId(showtime.getId());
        dto.setSeatNumber(5);
        dto.setCustomerName("Alice");

        Booking booking1 = bookingService.createBooking(dto);
        assertNotNull(booking1);

        // trying to create double booking for the same seat
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            bookingService.createBooking(dto);
        });
        String expectedMessage = "Seat already booked";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
