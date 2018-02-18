package com.faleknatalia.cinemaBookingSystem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledMovieRepository extends JpaRepository<ScheduledMovie, Long> {
}
