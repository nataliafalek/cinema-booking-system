package com.faleknatalia.cinemaBookingSystem.util;

import java.util.List;

public class TicketData {

    private String movieTitle;
    private String projectionDate;
    private String projectionHour;
    private long cinemaHallId;
    private List<Integer> seatNumber;
    private List<Integer> TicketPrice;

    public TicketData(String movieTitle, String projectionDate, String projectionHour, long cinemaHallId, List<Integer> seatNumber, List<Integer> ticketPrice) {
        this.movieTitle = movieTitle;
        this.projectionDate = projectionDate;
        this.projectionHour = projectionHour;
        this.cinemaHallId = cinemaHallId;
        this.seatNumber = seatNumber;
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

    public List<Integer> getSeatNumber() {
        return seatNumber;
    }

    public List<Integer> getTicketPrice() {
        return TicketPrice;
    }
}
