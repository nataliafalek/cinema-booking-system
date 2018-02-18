package com.faleknatalia.cinemaBookingSystem;

public class Reservation {
    private Movie chosenMovie;
    private boolean isBuying;
    private int numberOfchosenSeat;

    public Reservation(Movie chosenMovie, boolean isBuying) {
        this.chosenMovie = chosenMovie;
        this.isBuying = isBuying;
    }

    public Movie getChoosenMovie() {
        return chosenMovie;
    }

    public boolean isBuying() {
        return isBuying;
    }
}
