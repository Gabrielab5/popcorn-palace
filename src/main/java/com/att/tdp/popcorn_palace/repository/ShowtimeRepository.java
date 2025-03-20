package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.dto.ShowtimeRequestDto;
import com.att.tdp.popcorn_palace.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    // Checks if an overlapping showtime exists in a given theater using a DTO
    @Query("SELECT COUNT(s) > 0 FROM Showtime s WHERE s.theater = :#{#dto.theater} " +
            "AND ((s.startTime <= :#{#dto.endTime} AND s.endTime >= :#{#dto.startTime}))")
    boolean existsOverlappingShowtime(@Param("dto") ShowtimeRequestDto dto);
}
