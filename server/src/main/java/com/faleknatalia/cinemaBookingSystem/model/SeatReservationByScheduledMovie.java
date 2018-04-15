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

    private long ticketPriceId;

    @OneToOne
    private Seat seat;

    public SeatReservationByScheduledMovie() {
    }

    public SeatReservationByScheduledMovie(long scheduledMovieId, long cinemaHallId, boolean isFree, long ticketPriceId, Seat seat) {
        this.scheduledMovieId = scheduledMovieId;
        this.cinemaHallId = cinemaHallId;
        this.isFree = isFree;
        this.ticketPriceId = ticketPriceId;
        this.seat = seat;
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

    public long getTicketPriceId() {
        return ticketPriceId;
    }

    public Seat getSeat() {
        return seat;
    }
}
