package com.faleknatalia.cinemaBookingSystem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduledMovieRepository extends JpaRepository<ScheduledMovie, Long> {

    List<ScheduledMovie> findAllByCinemaHallId(long cinemaHallId);

}
