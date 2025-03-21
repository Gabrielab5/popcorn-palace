// DTO for returning showtime details.
package com.att.tdp.popcorn_palace.dto;
import java.time.LocalDateTime;
import com.att.tdp.popcorn_palace.entity.Showtime;

public class ShowtimeResponseDto {
    private Long id;
    private String movieTitle;
    private String theater;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double price;

    public ShowtimeResponseDto(Showtime showtime) {
        this.id = showtime.getId();
        this.movieTitle = (showtime.getMovie() != null) ? showtime.getMovie().getTitle() : "Unknown";
        this.theater = showtime.getTheater();
        this.startTime = showtime.getStartTime();
        this.endTime = showtime.getEndTime();
        this.price = showtime.getPrice();
    }

     // Getters & Setters
     public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getTheater() {
        return theater;
    }

    public void setTheater(String theater) {
        this.theater = theater;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}