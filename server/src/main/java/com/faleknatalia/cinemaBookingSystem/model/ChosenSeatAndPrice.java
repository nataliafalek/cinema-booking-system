package com.faleknatalia.cinemaBookingSystem.model;

import java.io.Serializable;

public class ChosenSeatAndPrice implements Serializable {

    private long seatId;
    private long ticketPriceId;

    public ChosenSeatAndPrice() {
    }

    public ChosenSeatAndPrice(long seatId, long ticketPriceId) {
        this.seatId = seatId;
        this.ticketPriceId = ticketPriceId;
    }

    public long getSeatId() {
        return seatId;
    }

    public long getTicketPriceId() {
        return ticketPriceId;
    }
}
