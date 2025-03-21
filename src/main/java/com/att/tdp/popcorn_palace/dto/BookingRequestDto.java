// DTO for creating a booking.

package com.att.tdp.popcorn_palace.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookingRequestDto {
    
    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;
    
    @Min(value = 1, message = "Seat number must be positive")
    private int seatNumber;
    
    @NotBlank(message = "Customer name is required")
    private String customerName;
    
   
    // Getters & Setters
    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
