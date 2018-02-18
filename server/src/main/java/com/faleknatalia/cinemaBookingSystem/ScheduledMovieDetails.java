package com.faleknatalia.cinemaBookingSystem;

import java.time.LocalDateTime;

public class ScheduledMovieDetails {

    private String movieTitle;
    private int movieDurationInMinutes;
    private LocalDateTime dateOfProjection;
    //private long cinemaHallId;

    public ScheduledMovieDetails(String movieTitle, int movieDurationInMinutes, LocalDateTime dateOfProjection) {
        this.movieTitle = movieTitle;
        this.movieDurationInMinutes = movieDurationInMinutes;
        this.dateOfProjection = dateOfProjection;
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

}
