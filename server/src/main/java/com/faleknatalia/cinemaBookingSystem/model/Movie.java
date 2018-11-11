package com.faleknatalia.cinemaBookingSystem.model;

import javax.persistence.*;

@Entity
public class Movie {

    private String title;
    private String description;
    private int durationInMinutes;
    private String imageUrl;
    private String carouselImageUrl;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long movieId;

    public Movie(String title, String description, int durationInMinutes, String imageUrl, String carouselImageUrl) {
        this.title = title;
        this.description = description;
        this.durationInMinutes = durationInMinutes;
        this.imageUrl = imageUrl;
        this.carouselImageUrl = carouselImageUrl;
    }

    public Movie() {
    }

    public String getCarouselImageUrl() {
        return carouselImageUrl;
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
