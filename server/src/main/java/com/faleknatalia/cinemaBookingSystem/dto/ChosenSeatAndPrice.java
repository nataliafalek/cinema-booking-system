package com.faleknatalia.cinemaBookingSystem.dto;

import java.io.Serializable;
import java.util.Objects;

//Serializable because it is used in the session
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChosenSeatAndPrice that = (ChosenSeatAndPrice) o;
        return seatId == that.seatId &&
                ticketPriceId == that.ticketPriceId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatId, ticketPriceId);
    }
}
