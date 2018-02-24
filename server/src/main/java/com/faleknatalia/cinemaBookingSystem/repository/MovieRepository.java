package com.faleknatalia.cinemaBookingSystem.repository;

import com.faleknatalia.cinemaBookingSystem.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {


}
