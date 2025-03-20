package com.att.tdp.popcorn_palace.dto;
import jakarta.validation.constraints.NotEmpty;

public class MovieRequestDto {
    @NotEmpty public String title;
    @NotEmpty public String genre;
    public int duration;
    public double rating;
    public int releaseYear;
}
