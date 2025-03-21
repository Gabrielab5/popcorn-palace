package com.att.tdp.popcorn_palace.dto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class MovieRequestDto {
    @NotEmpty private String title;
    @NotEmpty private String genre;
    @NotNull private int duration;
    @NotNull private double rating;
    @NotNull private int releaseYear;

    // Getters & Setters
    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getDuration() {
        return duration;
    }

    public double getRating() {
        return rating;
    }

    public int getReleaseYear() {
        return releaseYear;
    }
    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setRating(double rating) { this.rating = rating; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
}
