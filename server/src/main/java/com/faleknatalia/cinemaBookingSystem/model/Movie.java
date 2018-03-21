package com.faleknatalia.cinemaBookingSystem.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Movie {

    private String title;
    private String description;
    private int durationInMinutes;
    private String imageUrl;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long movieId;

    public Movie(String title, String description, int durationInMinutes, String imageUrl) {
        this.title = title;
        this.description = description;
        this.durationInMinutes = durationInMinutes;
        this.imageUrl = imageUrl;
    }

    public Movie() {
    }

    public String getTitle() {
        return title;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public long getMovieId() {
        return movieId;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
