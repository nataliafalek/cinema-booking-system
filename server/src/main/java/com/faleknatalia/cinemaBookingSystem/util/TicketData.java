package com.faleknatalia.cinemaBookingSystem.util;

import com.faleknatalia.cinemaBookingSystem.model.Seat;

import java.util.List;

public class TicketData {

    private String movieTitle;
    private String projectionDate;
    private String projectionHour;
    private long cinemaHallId;
    private List<Seat> chosenSeats;
    private List<Integer> TicketPrice;

    public TicketData(String movieTitle, String projectionDate, String projectionHour, long cinemaHallId, List<Seat> chosenSeats, List<Integer> ticketPrice) {
        this.movieTitle = movieTitle;
        this.projectionDate = projectionDate;
        this.projectionHour = projectionHour;
        this.cinemaHallId = cinemaHallId;
        this.chosenSeats = chosenSeats;
        TicketPrice = ticketPrice;
    }

    public TicketData() {
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getProjectionDate() {
        return projectionDate;
    }

    public String getProjectionHour() {
        return projectionHour;
    }

    public long getCinemaHallId() {
        return cinemaHallId;
    }

    public List<Seat> getChosenSeats() {
        return chosenSeats;
    }

    public List<Integer> getTicketPrice() {
        return TicketPrice;
    }
}
