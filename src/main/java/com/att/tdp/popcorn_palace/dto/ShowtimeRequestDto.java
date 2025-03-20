// DTO for creating or updating a showtime.
package com.att.tdp.popcorn_palace.dto;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ShowtimeRequestDto {
    @NotNull(message = "Movie ID is required")
    public Long movieId;

    @NotBlank(message = "Theater is required")
    public String theater;

    @NotNull(message = "Start time is required")
    public LocalDateTime startTime;

    @NotNull(message = "End time is required")
    public LocalDateTime endTime;

    @NotNull(message = "Price is required")
    public double price;

    public ShowtimeRequestDto(String theater, LocalDateTime startTime, LocalDateTime endTime) {
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTheater() { return theater; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
}
