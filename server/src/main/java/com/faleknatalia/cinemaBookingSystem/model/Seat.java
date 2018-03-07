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

    public Seat(int seatNumber) {
        this.seatNumber = seatNumber;
    }


    public Seat() {
    }

    public int getSeatNumber() {
        return seatNumber;
    }


    public long getSeatId() {
        return seatId;
    }

}
