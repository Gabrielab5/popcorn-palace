package com.att.tdp.popcorn_palace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository<Movie> extends JpaRepository<Movie, Long> {}

