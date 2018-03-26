package com.faleknatalia.cinemaBookingSystem.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ScheduledMovieDetails implements Serializable {

    private String movieTitle;
    private int movieDurationInMinutes;
    private LocalDateTime dateOfProjection;
    private long scheduledMovieId;
    private String dayOfProjection;
    private String hourOfProjection;

    public ScheduledMovieDetails(String movieTitle, int movieDurationInMinutes, LocalDateTime dateOfProjection, long scheduledMovieId, String dayOfProjection, String hourOfProjection) {
        this.movieTitle = movieTitle;
        this.movieDurationInMinutes = movieDurationInMinutes;
        this.dateOfProjection = dateOfProjection;
        this.scheduledMovieId = scheduledMovieId;
        this.dayOfProjection = dayOfProjection;
        this.hourOfProjection = hourOfProjection;
    }

    public String getHourOfProjection() {
        return hourOfProjection;
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

    public String getDayOfProjection() {
        return dayOfProjection;
    }
}
