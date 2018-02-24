package com.faleknatalia.cinemaBookingSystem.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seatId;

    private int seatNumber;

    public Seat(int seatNumber, boolean isFree, int ticketPrice) {
        this.seatNumber = seatNumber;
        this.isFree = isFree;
        this.ticketPrice = ticketPrice;
    }

    private boolean isFree;
    private int ticketPrice;


    public Seat() {
    }

    public int getSeatNumber() {
        return seatNumber;
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

    public void setFree(boolean free) {
        isFree = free;
    }
}
