package com.faleknatalia.cinemaBookingSystem.model;

import javax.persistence.*;

@Entity
public class SeatReservationByScheduledMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seatReservationId;

    private long scheduledMovieId;

    private long cinemaHallId;

    private boolean isFree;

    private int ticketPrice;

    private long seatId;

    private int seatNumber;

    private int seatRow;

    public SeatReservationByScheduledMovie() {
    }

    public SeatReservationByScheduledMovie(long scheduledMovieId, long cinemaHallId, boolean isFree, int ticketPrice, long seatId, int seatNumber, int seatRow) {
        this.scheduledMovieId = scheduledMovieId;
        this.cinemaHallId = cinemaHallId;
        this.isFree = isFree;
        this.ticketPrice = ticketPrice;
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.seatRow = seatRow;
    }

    public long getSeatReservationId() {
        return seatReservationId;
    }

    public long getScheduledMovieId() {
        return scheduledMovieId;
    }

    public long getCinemaHallId() {
        return cinemaHallId;
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

    public int getSeatNumber() {
        return seatNumber;
    }

    public int getSeatRow() {
        return seatRow;
    }
}
