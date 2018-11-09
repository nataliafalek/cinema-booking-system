package com.faleknatalia.cinemaBookingSystem.model;

import javax.persistence.*;

@Entity
public class Movie {

    private String title;
    private String description;
    private int durationInMinutes;
    private String imageUrl;

    @Lob
    private byte[] carouselImage;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long movieId;

    public Movie(String title, String description, int durationInMinutes, String imageUrl, byte[] carouselImage) {
        this.title = title;
        this.description = description;
        this.durationInMinutes = durationInMinutes;
        this.imageUrl = imageUrl;
        this.carouselImage = carouselImage;
    }

    public Movie() {
    }

    public byte[] getCarouselImage() {
        return carouselImage;
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
