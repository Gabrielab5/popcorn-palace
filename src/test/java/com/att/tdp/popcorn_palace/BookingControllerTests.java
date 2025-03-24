package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.dto.BookingRequestDto;
import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.Arrays;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Integration tests for BookingController endpoints.
@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    // Test for creating a valid booking.
    @Test
    public void testAddBooking() throws Exception {
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setShowtimeId(1L);
        requestDto.setSeatNumber(10);
        requestDto.setCustomerName("John Doe");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setSeatNumber(10);
        booking.setCustomerName("John Doe");
        
        // Setup showtime and movie for the response DTO.
        Showtime showtime = new Showtime();
        showtime.setId(1L);
        Movie movie = new Movie();
        movie.setTitle("Inception");
        showtime.setMovie(movie);
        showtime.setTheater("Theater 1");
        showtime.setStartTime(LocalDateTime.now().plusDays(1));
        booking.setShowtime(showtime);

        when(bookingService.createBooking(any(BookingRequestDto.class))).thenReturn(booking);
        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.movieTitle").value("Inception"));
    }

    // Test for retrieving all bookings.
    @Test
    public void testGetAllBookings() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        Showtime showtime = new Showtime();
        showtime.setId(1L);
        showtime.setMovie(movie);
        showtime.setTheater("Theater 1");
        showtime.setStartTime(LocalDateTime.now().plusDays(1));
        showtime.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setShowtime(showtime);
        booking1.setSeatNumber(10);
        booking1.setCustomerName("John Doe");

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setShowtime(showtime);
        booking2.setSeatNumber(11);
        booking2.setCustomerName("Jane Smith");

        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(booking1, booking2));

        mockMvc.perform(get("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // Test for retrieving a booking by its ID.
    @Test
    public void testGetBookingById() throws Exception {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setSeatNumber(15);
        booking.setCustomerName("Alice");

        Showtime showtime = new Showtime();
        showtime.setId(1L);
        Movie movie = new Movie();
        movie.setTitle("Inception");
        showtime.setMovie(movie);
        showtime.setTheater("Theater 2");
        showtime.setStartTime(LocalDateTime.now().plusDays(2));
        booking.setShowtime(showtime);

        when(bookingService.getBookingById(1L)).thenReturn(booking);

        mockMvc.perform(get("/api/bookings/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("Alice"))
                .andExpect(jsonPath("$.movieTitle").value("Inception"));
    }

    
    // Test for updating an existing booking.   
    @Test
    public void testUpdateBooking() throws Exception {
        Long bookingId = 1L;
        BookingRequestDto updateDto = new BookingRequestDto();
        updateDto.setShowtimeId(1L);
        updateDto.setSeatNumber(12);
        updateDto.setCustomerName("Bob");

        Booking updatedBooking = new Booking();
        updatedBooking.setId(bookingId);
        updatedBooking.setSeatNumber(12);
        updatedBooking.setCustomerName("Bob");

        Showtime showtime = new Showtime();
        showtime.setId(1L);
        Movie movie = new Movie();
        movie.setTitle("Inception");
        showtime.setMovie(movie);
        showtime.setTheater("Theater 1");
        showtime.setStartTime(LocalDateTime.now().plusDays(1));
        updatedBooking.setShowtime(showtime);

        when(bookingService.updateBooking(eq(1L), any(BookingRequestDto.class))).thenReturn(updatedBooking);

        mockMvc.perform(put("/api/bookings/" + bookingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId))
                .andExpect(jsonPath("$.seatNumber").value(12))
                .andExpect(jsonPath("$.customerName").value("Bob"));
    }

    //  Test for deleting a booking.
    @Test
    public void testDeleteBooking() throws Exception {
        Long bookingId = 1L;
        doNothing().when(bookingService).deleteBooking(bookingId);

        mockMvc.perform(delete("/api/bookings/" + bookingId))
                .andExpect(status().isNoContent());
    }

    // Test for booking creation with an invalid (negative) seat number.
    // Expect a 400 Bad Request.
    @Test
    public void testAddBookingInvalidSeatNumber() throws Exception {
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setShowtimeId(1L);
        requestDto.setSeatNumber(-5); //INVALID NEGATIVE NUMBER
        requestDto.setCustomerName("John Doe");

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    // Test for booking creation with an empty customer name.
    // Expect a 400 Bad Request.
    @Test
    public void testAddBookingEmptyCustomerName() throws Exception {
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setShowtimeId(1L);
        requestDto.setSeatNumber(10);
        requestDto.setCustomerName("");

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}
