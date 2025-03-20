// DTO for returning showtime details.
package com.att.tdp.popcorn_palace.dto;
import java.time.LocalDateTime;
import com.att.tdp.popcorn_palace.entity.Showtime;

public class ShowtimeResponseDto {
    public Long id;
    public String movieTitle;
    public String theater;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public double price;

    public ShowtimeResponseDto(Showtime showtime) {
        this.id = showtime.getId();
        // Assuming the associated Movie has a non-null title.
        this.movieTitle = showtime.getMovie().getTitle();
        this.theater = showtime.getTheater();
        this.startTime = showtime.getStartTime();
        this.endTime = showtime.getEndTime();
        this.price = showtime.getPrice();
    }
}
