package com.att.tdp.popcorn_palace.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.att.tdp.popcorn_palace.dto.ShowtimeRequestDto;
import com.att.tdp.popcorn_palace.dto.ShowtimeResponseDto;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.service.ShowtimeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/showtimes")
public class ShowtimeController {

    private final ShowtimeService service;

    public ShowtimeController(ShowtimeService service) {
        this.service = service;
    }

    // Creates a new showtime
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShowtimeResponseDto addShowtime(@Valid @RequestBody ShowtimeRequestDto dto) {
        Showtime savedShowtime = service.createShowtime(dto);
        return new ShowtimeResponseDto(savedShowtime);
    }

    // Get all showtimes
    @GetMapping
    public List<ShowtimeResponseDto> getAllShowtimes() {
        return service.getAllShowtimes().stream()
            .map(ShowtimeResponseDto::new)
            .collect(Collectors.toList());
    }

    // Get showtime by ID
    @GetMapping("/{id}")
    public ResponseEntity<ShowtimeResponseDto> getShowtimeById(@PathVariable Long id) {
        Showtime showtime = service.getShowtimeById(id);
        return ResponseEntity.ok(new ShowtimeResponseDto(showtime));
    }

    // Update a showtime by ID
    @PutMapping("/{id}")
    public ResponseEntity<ShowtimeResponseDto> updateShowtime(@PathVariable Long id, @Valid @RequestBody ShowtimeRequestDto dto) {
        Showtime updated = service.updateShowtime(id, dto);
        return ResponseEntity.ok(new ShowtimeResponseDto(updated));
    }

    // Delete a showtime
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShowtime(@PathVariable Long id) {
        service.deleteShowtime(id);
    }
}
