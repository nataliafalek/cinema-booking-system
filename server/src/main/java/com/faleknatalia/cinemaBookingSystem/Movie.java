package com.faleknatalia.cinemaBookingSystem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Movie {

    private String title;
    private String description;
    private int durationInMinutes;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long movieId;

    public Movie(String title, String description, int durationInMinutes) {
        this.title = title;
        this.description = description;
        this.durationInMinutes = durationInMinutes;
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
}
