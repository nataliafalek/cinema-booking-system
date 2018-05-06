package com.faleknatalia.cinemaBookingSystem.dto;

import java.time.LocalDateTime;

public class ScheduledMovieDetailsDto {

    private String movieTitle;
    private int movieDurationInMinutes;
    private LocalDateTime dateOfProjection;
    private long scheduledMovieId;
    private String dayOfProjection;
    private String hourOfProjection;
    private String movieDescription;
    private String movieImageUrl;

    public ScheduledMovieDetailsDto(String movieTitle, int movieDurationInMinutes, LocalDateTime dateOfProjection, long scheduledMovieId, String dayOfProjection, String hourOfProjection, String movieDescription, String movieImageUrl) {
        this.movieTitle = movieTitle;
        this.movieDurationInMinutes = movieDurationInMinutes;
        this.dateOfProjection = dateOfProjection;
        this.scheduledMovieId = scheduledMovieId;
        this.dayOfProjection = dayOfProjection;
        this.hourOfProjection = hourOfProjection;
        this.movieDescription = movieDescription;
        this.movieImageUrl = movieImageUrl;
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

    public String getMovieImageUrl() {
        return movieImageUrl;
    }

    public long getScheduledMovieId() {
        return scheduledMovieId;
    }

    public String getDayOfProjection() {
        return dayOfProjection;
    }

    public String getMovieDescription() {
        return movieDescription;
    }
}
