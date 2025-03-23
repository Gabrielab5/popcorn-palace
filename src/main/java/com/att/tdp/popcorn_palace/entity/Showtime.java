package com.att.tdp.popcorn_palace.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a Showtime entity in the database.
 * This class will be mapped to a table named "showtime" by default.
 */
@Entity
@Table(name = "showtime")
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The associated movie for this showtime.
     * Many showtimes can be linked to one movie.
     */
    @ManyToOne
    private Movie movie;

    private String theater;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double price;

    //Default constructor (required by JPA).
    public Showtime() { }

    //Constructor with all fields 
    public Showtime(Long id, Movie movie, String theater, LocalDateTime startTime, LocalDateTime endTime, double price) {
        this.id = id;
        this.movie = movie;
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    // ----------- Getters and Setters -----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
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
