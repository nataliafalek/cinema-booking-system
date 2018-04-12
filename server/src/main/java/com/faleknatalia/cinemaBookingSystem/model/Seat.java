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

    private int rowNumber;

    private int columnNumber;

    public Seat(int seatNumber, int rowNumber, int columnNumber) {
        this.seatNumber = seatNumber;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
    }

    public Seat() {
    }

    public int getSeatNumber() {
        return seatNumber;
    }


    public long getSeatId() {
        return seatId;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatId=" + seatId +
                ", seatNumber=" + seatNumber +
                ", rowNumber=" + rowNumber +
                ", columnNumber=" + columnNumber +
                '}';
    }
}
