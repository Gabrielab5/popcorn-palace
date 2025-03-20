package com.att.tdp.popcorn_palace.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;

import com.att.tdp.popcorn_palace.dto.MovieRequestDto;
import com.att.tdp.popcorn_palace.dto.MovieResponseDto;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.service.MovieService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    
    private final MovieService service;

    // Constructor Injection of MovieService
    public MovieController(MovieService service) {
        this.service = service;
    }

    
    // Create new movie
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponseDto addMovie(@Valid @RequestBody MovieRequestDto dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.title);
        movie.setGenre(dto.genre);
        movie.setDuration(dto.duration);
        movie.setRating(dto.rating);
        movie.setReleaseYear(dto.releaseYear);

        Movie savedMovie = service.createMovie(movie);
        return new MovieResponseDto(savedMovie);
    }

    // Get all movies
    @GetMapping
    public List<MovieResponseDto> getAllMovies() {
        List<Movie> movies = service.getAllMovies();
        return movies.stream()
            .map(MovieResponseDto::new)
            .collect(Collectors.toList());
    }

    // Delete movie by ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable Long id) {
        service.deleteMovie(id);
    }

    // Exception handler for movie not found
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(Exception ex) {
        return ex.getMessage();
    }
}
