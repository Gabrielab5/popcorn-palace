package com.att.tdp.popcorn_palace.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.att.tdp.popcorn_palace.dto.MovieRequestDto;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;

@Service
public class MovieService {
    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    // Create movie
    public Movie createMovie(MovieRequestDto dto) {
        return saveMovie(new Movie(), dto);
    }
    
    // Get all movies
    public List<Movie> getAllMovies() {
        return repository.findAll();
    }

    // Get movie by ID 
    public Movie getMovieById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + id));
    }

    // Update movie
    public Movie updateMovie(Long id, MovieRequestDto dto) {
        Movie movie = getMovieById(id);
        return saveMovie(movie, dto);
    }

    // Delete movie
    public void deleteMovie(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found with ID: " + id);
        }
        repository.deleteById(id);
    }

    // Helper method to save/update movie details
    private Movie saveMovie(Movie movie, MovieRequestDto dto) {
        movie.setTitle(dto.getTitle());
        movie.setGenre(dto.getGenre());
        movie.setDuration(dto.getDuration());
        movie.setRating(dto.getRating());
        movie.setReleaseYear(dto.getReleaseYear());
        return repository.save(movie);
    }
}
