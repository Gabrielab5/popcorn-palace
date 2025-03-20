package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    // Checks if there is an existing showtime in the given theater that overlaps with the given time range.
    // return true if an overlapping showtime exists, false otherwise.
    boolean overlappingShowtime(String theater, LocalDateTime endTime, LocalDateTime startTime);
}
