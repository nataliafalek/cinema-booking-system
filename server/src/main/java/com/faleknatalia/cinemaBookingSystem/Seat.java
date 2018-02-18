package com.faleknatalia.cinemaBookingSystem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seatId;

    private boolean isFree;
    private int ticketPrice;

    public Seat(boolean isFree, int ticketPrice) {
        this.isFree = isFree;
        this.ticketPrice = ticketPrice;
    }

    public Seat() {
    }

    public boolean isFree() {
        return isFree;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public long getSeatId() {
        return seatId;
    }
}
