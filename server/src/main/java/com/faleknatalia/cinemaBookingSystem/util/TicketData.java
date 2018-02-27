package com.faleknatalia.cinemaBookingSystem.util;

import java.time.LocalDateTime;

public class TicketData {

    private String movieTitle;
    private LocalDateTime projectionDate;
    private long cinemaHallId;
    private int seatNumber;
    private int TicketPrice;

    public TicketData(String movieTitle, LocalDateTime projectionDate, long cinemaHallId, int seatNumber, int ticketPrice) {
        this.movieTitle = movieTitle;
        this.projectionDate = projectionDate;
        this.cinemaHallId = cinemaHallId;
        this.seatNumber = seatNumber;
        TicketPrice = ticketPrice;
    }

    public TicketData() {
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public LocalDateTime getProjectionDate() {
        return projectionDate;
    }

    public long getCinemaHallId() {
        return cinemaHallId;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public int getTicketPrice() {
        return TicketPrice;
    }
}
