package com.att.tdp.popcorn_palace.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
        Movie savedMovie = service.createMovie(dto);
        return new MovieResponseDto(savedMovie);
    }

     // Update movie by ID
    @PutMapping("/{id}")
    public ResponseEntity<MovieResponseDto> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequestDto dto) {
        Movie updatedMovie = service.updateMovie(id, dto);
        return ResponseEntity.ok(new MovieResponseDto(updatedMovie));
    }
    
    // Get all movies
    @GetMapping
    public List<MovieResponseDto> getAllMovies() {
        List<Movie> movies = service.getAllMovies();
        return movies.stream()
            .map(MovieResponseDto::new)
            .collect(Collectors.toList());
    }

     // Get movie by ID
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDto> getMovieById(@PathVariable Long id) {
        Movie movie = service.getMovieById(id);
        return ResponseEntity.ok(new MovieResponseDto(movie));
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
