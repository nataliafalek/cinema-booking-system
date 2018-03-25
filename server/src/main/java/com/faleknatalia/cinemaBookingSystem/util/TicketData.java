package com.faleknatalia.cinemaBookingSystem.util;

import java.time.LocalDateTime;
import java.util.List;

public class TicketData {

    private String movieTitle;
    private LocalDateTime projectionDate;
    private long cinemaHallId;
    private List<Integer> seatNumber;
    private List<Integer> TicketPrice;

    public TicketData(String movieTitle, LocalDateTime projectionDate, long cinemaHallId, List<Integer> seatNumber, List<Integer> ticketPrice) {
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

    public List<Integer> getSeatNumber() {
        return seatNumber;
    }

    public List<Integer> getTicketPrice() {
        return TicketPrice;
    }
}
