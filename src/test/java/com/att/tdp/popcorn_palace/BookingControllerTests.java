package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.controller.BookingController;
import com.att.tdp.popcorn_palace.dto.BookingRequestDto;
import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @Autowired
    private ObjectMapper objectMapper;

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

        when(bookingService.createBooking(any(BookingRequestDto.class))).thenReturn(booking);
        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("John Doe"));
    }


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
        showtime.setPrice(10.0);

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

    @Test
    public void testDeleteBooking() throws Exception {
        Long bookingId = 1L;
        doNothing().when(bookingService).deleteBooking(bookingId);

        mockMvc.perform(delete("/api/bookings/" + bookingId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testAddBookingInvalidSeatNumber() throws Exception {
        // Negative seat number should trigger validation error (HTTP 400)
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setShowtimeId(1L);
        requestDto.setSeatNumber(-5); //INVALID NEGATIVE NUMBER
        requestDto.setCustomerName("John Doe");

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddBookingEmptyCustomerName() throws Exception {
        // Empty customer name should trigger validation error (HTTP 400)
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
