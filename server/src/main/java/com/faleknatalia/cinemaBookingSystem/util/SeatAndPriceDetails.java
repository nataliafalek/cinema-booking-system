package com.faleknatalia.cinemaBookingSystem.util;

import com.faleknatalia.cinemaBookingSystem.model.Seat;
import com.faleknatalia.cinemaBookingSystem.model.TicketPrice;

public class SeatAndPriceDetails {

    private Seat seat;
    private TicketPrice ticketPrice;

    public SeatAndPriceDetails(Seat seat, TicketPrice ticketPrice) {
        this.seat = seat;
        this.ticketPrice = ticketPrice;
    }

    public SeatAndPriceDetails() {
    }

    public Seat getSeat() {
        return seat;
    }

    public TicketPrice getTicketPrice() {
        return ticketPrice;
    }
}
