package com.att.tdp.popcorn_palace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowtimeRepository<Movie> extends JpaRepository<Movie, Long> {}

