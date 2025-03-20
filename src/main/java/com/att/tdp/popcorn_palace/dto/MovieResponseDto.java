package com.att.tdp.popcorn_palace.dto;

import com.att.tdp.popcorn_palace.entity.Movie; 

public class MovieResponseDto {
    public Long id;
    public String title;
    public String genre;
    public int duration;
    public double rating;
    public int releaseYear;

    public MovieResponseDto(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.genre = movie.getGenre();
        this.duration = movie.getDuration();
        this.rating = movie.getRating();
        this.releaseYear = movie.getReleaseYear();
    }

}
