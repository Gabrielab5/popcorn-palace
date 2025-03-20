// DTO for creating a booking.

package com.att.tdp.popcorn_palace.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookingRequestDto {
    
    @NotNull(message = "Showtime ID is required")
    public Long showtimeId;
    
    @Min(value = 1, message = "Seat number must be positive")
    public int seatNumber;
    
    @NotBlank(message = "Customer name is required")
    public String customerName;
}
