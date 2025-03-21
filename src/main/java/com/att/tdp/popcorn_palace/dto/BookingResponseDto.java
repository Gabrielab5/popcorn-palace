package com.att.tdp.popcorn_palace.dto;
import java.time.LocalDateTime;
import com.att.tdp.popcorn_palace.entity.Booking;

public class BookingResponseDto {
    private Long id;
    private String movieTitle;
    private String theater;
    private LocalDateTime startTime;
    private int seatNumber;
    private String customerName;

    public BookingResponseDto(Booking booking) {
        this.id = booking.getId();
        this.movieTitle = booking.getShowtime().getMovie().getTitle();
        this.theater = booking.getShowtime().getTheater();
        this.startTime = booking.getShowtime().getStartTime();
        this.seatNumber = booking.getSeatNumber();
        this.customerName = booking.getCustomerName();
    }

    public BookingResponseDto() {}

    public Long getId(){ return id;}
    public String getMovieTitle(){ return movieTitle;}
    public String getTheater(){ return theater;}
    public LocalDateTime getStartTime(){ return startTime;}
    public int getSeatNumber(){ return seatNumber;}
    public String getCustomerName(){ return customerName;}

    public void setId(long l) {
        this.id = l;
    }

    public void setSeatNumber(int seatNum) {
        this.seatNumber = seatNum;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
}
