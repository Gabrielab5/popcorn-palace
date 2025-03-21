package com.att.tdp.popcorn_palace.repository;
import com.att.tdp.popcorn_palace.entity.Showtime;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    // Checks if an overlapping showtime exists in a given theater using a DTO
    @Query("SELECT COUNT(s) > 0 FROM Showtime s WHERE s.theater = :theater " +
       "AND (s.startTime < :endTime AND s.endTime > :startTime)")
    boolean existsOverlappingShowtime(@Param("theater") String theater, 
    @Param("startTime") LocalDateTime startTime, 
    @Param("endTime") LocalDateTime endTime);
}
