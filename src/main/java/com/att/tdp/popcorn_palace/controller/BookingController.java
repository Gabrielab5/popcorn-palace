package com.att.tdp.popcorn_palace.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.att.tdp.popcorn_palace.dto.BookingRequestDto;
import com.att.tdp.popcorn_palace.dto.BookingResponseDto;
import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.service.BookingService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    // Create new booking
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto addBooking(@Valid @RequestBody BookingRequestDto dto) {
        Booking savedBooking = service.createBooking(dto);
        return new BookingResponseDto(savedBooking);
    }

     // Get booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable Long id) {
        Booking booking = service.getBookingById(id);
        return ResponseEntity.ok(new BookingResponseDto(booking));
    }

    // Update booking by ID
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDto> updateBooking(@PathVariable Long id, @Valid @RequestBody BookingRequestDto dto) {
        Booking updatedBooking = service.updateBooking(id, dto);
        return ResponseEntity.ok(new BookingResponseDto(updatedBooking));
    }

    // Get all bookings
    @GetMapping
    public List<BookingResponseDto> getAllBookings() {
        return service.getAllBookings().stream()
            .map(BookingResponseDto::new)
            .collect(Collectors.toList());
    }

    // Delete booking by ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable Long id) {
        service.deleteBooking(id);
    }
}
