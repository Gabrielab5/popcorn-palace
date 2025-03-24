package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.dto.ShowtimeRequestDto;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// Integration and unit tests for ShowtimeController endpoints.
// This class validates the functionality and input handling of showtime-related API endpoints.
@SpringBootTest
@AutoConfigureMockMvc
public class ShowtimeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private ObjectMapper objectMapper;

    private Long movieId;

    // Clears relevant repositories and sets up a default movie before each test.
    // Ensures each test is isolated and starts with a clean slate.
    @BeforeEach
    void setup() {
         // Clear dependencies first to avoid foreign key violations
        bookingRepository.deleteAll();     
        showtimeRepository.deleteAll();  
        movieRepository.deleteAll();

        Movie movie = new Movie();
        movie.setTitle("Interstellar");
        movie.setGenre("Sci-Fi");
        movie.setDuration(169);
        movie.setRating(8);
        movie.setReleaseYear(2014);
        movieId = movieRepository.save(movie).getId();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    //  Test full integration flow for creating a showtime with valid data.
    @Test
    public void testCreateShowtimeIntegration() throws Exception {
        ShowtimeRequestDto request = new ShowtimeRequestDto();
        request.setMovieId(movieId);
        request.setTheater("Cinema Hall 1");
        request.setStartTime(LocalDateTime.now().plusHours(1));
        request.setEndTime(LocalDateTime.now().plusHours(3));
        request.setPrice(50.0);

        mockMvc.perform(post("/api/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.theater").value("Cinema Hall 1"));
    }

    // Unit test: validates successful showtime creation using mocked service.
    @Test
    public void testAddShowtime() throws Exception {
        ShowtimeRequestDto requestDto = new ShowtimeRequestDto();
        requestDto.setMovieId(1L);
        requestDto.setTheater("Theater 1");
        requestDto.setStartTime(LocalDateTime.now().plusDays(1));
        requestDto.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        requestDto.setPrice(10.0);

        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        Showtime showtime = new Showtime();
        showtime.setId(1L);
        showtime.setMovie(movie);
        showtime.setTheater(requestDto.getTheater());
        showtime.setStartTime(requestDto.getStartTime());
        showtime.setEndTime(requestDto.getEndTime());
        showtime.setPrice(requestDto.getPrice());

        when(showtimeService.createShowtime(any(ShowtimeRequestDto.class))).thenReturn(showtime);

        mockMvc.perform(post("/api/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.movieTitle").value("Inception"));
    }

    // Unit test: retrieves all showtimes and validates response size.
    @Test
    public void testGetAllShowtimes() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        Showtime showtime1 = new Showtime();
        showtime1.setId(1L);
        showtime1.setMovie(movie);
        showtime1.setTheater("Theater 1");
        showtime1.setStartTime(LocalDateTime.now().plusDays(1));
        showtime1.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        showtime1.setPrice(10.0);

        Showtime showtime2 = new Showtime();
        showtime2.setId(2L);
        showtime2.setMovie(movie);
        showtime2.setTheater("Theater 2");
        showtime2.setStartTime(LocalDateTime.now().plusDays(2));
        showtime2.setEndTime(LocalDateTime.now().plusDays(2).plusHours(2));
        showtime2.setPrice(12.0);

        when(showtimeService.getAllShowtimes()).thenReturn(Arrays.asList(showtime1, showtime2));

        mockMvc.perform(get("/api/showtimes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // Unit test: retrieves a showtime by ID and validates its content.
    @Test
    public void testGetShowtimeById() throws Exception {
        Long showtimeId = 1L;
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        Showtime showtime = new Showtime();
        showtime.setId(showtimeId);
        showtime.setMovie(movie);
        showtime.setTheater("Theater 1");
        showtime.setStartTime(LocalDateTime.now().plusDays(1));
        showtime.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        showtime.setPrice(10.0);

        when(showtimeService.getShowtimeById(showtimeId)).thenReturn(showtime);

        mockMvc.perform(get("/api/showtimes/" + showtimeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(showtimeId))
                .andExpect(jsonPath("$.movieTitle").value("Inception"));
    }

    // Unit test: updates an existing showtime and verifies new values.
    @Test
    public void testUpdateShowtime() throws Exception {
        Long showtimeId = 1L;
        ShowtimeRequestDto updateDto = new ShowtimeRequestDto();
        updateDto.setMovieId(1L);
        updateDto.setTheater("Updated Theater");
        updateDto.setStartTime(LocalDateTime.now().plusDays(2));
        updateDto.setEndTime(LocalDateTime.now().plusDays(2).plusHours(3));
        updateDto.setPrice(15.0);

        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        Showtime updatedShowtime = new Showtime();
        updatedShowtime.setId(showtimeId);
        updatedShowtime.setMovie(movie);
        updatedShowtime.setTheater(updateDto.getTheater());
        updatedShowtime.setStartTime(updateDto.getStartTime());
        updatedShowtime.setEndTime(updateDto.getEndTime());
        updatedShowtime.setPrice(updateDto.getPrice());

        when(showtimeService.updateShowtime(eq(showtimeId), any(ShowtimeRequestDto.class))).thenReturn(updatedShowtime);

        mockMvc.perform(put("/api/showtimes/" + showtimeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(showtimeId))
                .andExpect(jsonPath("$.theater").value("Updated Theater"));
    }

    // Unit test: deletes a showtime successfully.
    @Test
    public void testDeleteShowtime() throws Exception {
        Long showtimeId = 1L;
        doNothing().when(showtimeService).deleteShowtime(showtimeId);

        mockMvc.perform(delete("/api/showtimes/" + showtimeId))
                .andExpect(status().isNoContent());
    }

    // Unit test: validates rejection of invalid time ranges (startTime after endTime).
    @Test
    public void testAddShowtimeInvalidTimeRange() throws Exception {
        ShowtimeRequestDto requestDto = new ShowtimeRequestDto();
        requestDto.setMovieId(1L);
        requestDto.setTheater("Theater 1");
        requestDto.setStartTime(LocalDateTime.of(2025, 4, 1, 22, 0));
        requestDto.setEndTime(LocalDateTime.of(2025, 4, 1, 20, 0));
        requestDto.setPrice(10.0);

        when(showtimeService.createShowtime(any(ShowtimeRequestDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start time must be before end time"));

        mockMvc.perform(post("/api/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    // Unit test: validates rejection of overlapping showtimes in the same theater.
    @Test
    public void testAddOverlappingShowtime() throws Exception {
        ShowtimeRequestDto requestDto = new ShowtimeRequestDto();
        requestDto.setMovieId(1L);
        requestDto.setTheater("Theater 1");
        requestDto.setStartTime(LocalDateTime.of(2025, 4, 1, 20, 0));
        requestDto.setEndTime(LocalDateTime.of(2025, 4, 1, 22, 0));
        requestDto.setPrice(12.0);

        when(showtimeService.createShowtime(any(ShowtimeRequestDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Overlapping showtime in the same theater"));

        mockMvc.perform(post("/api/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}
