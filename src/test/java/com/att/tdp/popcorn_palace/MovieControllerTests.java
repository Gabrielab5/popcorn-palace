package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.controller.MovieController;
import com.att.tdp.popcorn_palace.dto.MovieRequestDto;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(MovieController.class)
public class MovieControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void contextLoads() {
        // Test to ensure the application context loads successfully
    }

    @Test
    public void testAddMovie() throws Exception {
        MovieRequestDto requestDto = new MovieRequestDto();
        requestDto.setTitle("Inception");
        requestDto.setGenre("Sci-Fi");
        requestDto.setDuration(148);
        requestDto.setRating(8.8);
        requestDto.setReleaseYear(2010);
    
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle(requestDto.getTitle());
        movie.setGenre(requestDto.getGenre());
        movie.setDuration(requestDto.getDuration());
        movie.setRating(requestDto.getRating());
        movie.setReleaseYear(requestDto.getReleaseYear());
    
        when(movieService.createMovie(any(MovieRequestDto.class))).thenReturn(movie);
    
        mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Inception"));
    }
    

    @Test
    public void testGetAllMovies() throws Exception {
        Movie movie1 = new Movie();
        movie1.setId(1L);
        movie1.setTitle("Inception");
        movie1.setGenre("Sci-Fi");
        movie1.setDuration(148);
        movie1.setRating(8.8);
        movie1.setReleaseYear(2010);

        Movie movie2 = new Movie();
        movie2.setId(2L);
        movie2.setTitle("Interstellar");
        movie2.setGenre("Sci-Fi");
        movie2.setDuration(169);
        movie2.setRating(9.0);
        movie2.setReleaseYear(2014);

        when(movieService.getAllMovies()).thenReturn(Arrays.asList(movie1, movie2));

        mockMvc.perform(get("/api/movies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Inception"))
                .andExpect(jsonPath("$[1].title").value("Interstellar"));
    }

    @Test
    public void testUpdateMovie() throws Exception {
        Long movieId = 1L;
        MovieRequestDto updateDto = new MovieRequestDto();
        updateDto.setTitle("UPDATE Inception");
        updateDto.setGenre("Sci-Fi");
        updateDto.setDuration(148);
        updateDto.setRating(8.9);
        updateDto.setReleaseYear(2010);

        Movie updatedMovie = new Movie();
        updatedMovie.setId(movieId);
        updatedMovie.setTitle(updateDto.getTitle());
        updatedMovie.setGenre(updateDto.getGenre());
        updatedMovie.setDuration(updateDto.getDuration());
        updatedMovie.setRating(updateDto.getRating());
        updatedMovie.setReleaseYear(updateDto.getReleaseYear());

        when(movieService.updateMovie(eq(movieId), any(MovieRequestDto.class)))
                .thenReturn(updatedMovie);

        mockMvc.perform(put("/api/movies/" + movieId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception Updated"));
    }

    @Test
    public void testDeleteMovie() throws Exception {
        Long movieId = 1L;
        doNothing().when(movieService).deleteMovie(movieId);

        mockMvc.perform(delete("/api/movies/" + movieId))
                .andExpect(status().isNoContent());
    }
}
