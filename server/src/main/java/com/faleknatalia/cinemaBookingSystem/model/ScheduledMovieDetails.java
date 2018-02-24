package com.faleknatalia.cinemaBookingSystem.model;

import java.time.LocalDateTime;

public class ScheduledMovieDetails {

    private String movieTitle;
    private int movieDurationInMinutes;
    private LocalDateTime dateOfProjection;
    private long scheduledMovieId;


    public ScheduledMovieDetails(String movieTitle, int movieDurationInMinutes, LocalDateTime dateOfProjection, long scheduledMovieId) {
        this.movieTitle = movieTitle;
        this.movieDurationInMinutes = movieDurationInMinutes;
        this.dateOfProjection = dateOfProjection;
        this.scheduledMovieId = scheduledMovieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public int getMovieDurationInMinutes() {
        return movieDurationInMinutes;
    }

    public LocalDateTime getDateOfProjection() {
        return dateOfProjection;
    }

    public long getScheduledMovieId() {
        return scheduledMovieId;
    }
}
