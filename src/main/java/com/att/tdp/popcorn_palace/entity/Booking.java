package com.att.tdp.popcorn_palace.entity;

import jakarta.persistence.*;

/**
 * Represents a Booking entity in the database.
 * This class will be mapped to a table named "booking" by default.
 * It also has a unique constraint to prevent duplicate seat reservations
 * for the same showtime.
 */
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"showtime_id", "seatNumber"})
    }
)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     //The showtime this booking is associated with, Many bookings can be linked to one showtime.
    @ManyToOne
    private Showtime showtime;
    private int seatNumber;
    private String customerName;

    //Default constructor (required by JPA)
    public Booking() {  }
 
    //Constructor with all fields 
    public Booking(Long id, Showtime showtime, int seatNumber, String customerName) {
        this.id = id;
        this.showtime = showtime;
        this.seatNumber = seatNumber;
        this.customerName = customerName;
    }

    // ----------- Getters and Setters -----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
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
