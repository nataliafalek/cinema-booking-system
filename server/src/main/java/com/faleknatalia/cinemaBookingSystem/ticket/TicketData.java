package com.faleknatalia.cinemaBookingSystem.ticket;

import java.util.List;

public class TicketData {

    private String movieTitle;
    private String projectionDate;
    private String projectionHour;
    private long cinemaHallId;
    private List<SeatAndPriceDetails> seatAndPriceDetails;

    public TicketData(String movieTitle, String projectionDate, String projectionHour, long cinemaHallId, List<SeatAndPriceDetails> seatAndPriceDetails) {
        this.movieTitle = movieTitle;
        this.projectionDate = projectionDate;
        this.projectionHour = projectionHour;
        this.cinemaHallId = cinemaHallId;
        this.seatAndPriceDetails = seatAndPriceDetails;
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

    public List<SeatAndPriceDetails> getSeatAndPriceDetails() {
        return seatAndPriceDetails;
    }
}
