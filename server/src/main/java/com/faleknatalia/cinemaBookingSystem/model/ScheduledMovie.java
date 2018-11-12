package com.faleknatalia.cinemaBookingSystem.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ScheduledMovie {
    private LocalDateTime dateOfProjection;
    private long cinemaHallId;
    private long movieId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long scheduledMovieId;

    public ScheduledMovie(LocalDateTime dateOfProjection, long cinemaHallId, long movieId) {
        this.dateOfProjection = dateOfProjection;
        this.cinemaHallId = cinemaHallId;
        this.movieId = movieId;
    }

    public ScheduledMovie() {
    }

    public LocalDateTime getDateOfProjection() {
        return dateOfProjection;
    }

    public long getCinemaHallId() {
        return cinemaHallId;
    }

    public long getScheduledMovieId() {
        return scheduledMovieId;
    }

    public long getMovieId() {
        return movieId;
    }
}
