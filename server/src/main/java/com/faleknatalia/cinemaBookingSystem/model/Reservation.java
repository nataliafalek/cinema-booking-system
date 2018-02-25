package com.faleknatalia.cinemaBookingSystem.model;

import javax.persistence.*;

@Entity
public class Reservation {
    private long chosenMovieId;

    private long personalDataId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long reservationId;

    private long chosenSeatId;

    public Reservation(long chosenMovieId, long personalDataId, long chosenSeatId) {
        this.chosenMovieId = chosenMovieId;
        this.personalDataId = personalDataId;
        this.chosenSeatId = chosenSeatId;
    }

    public Reservation() {
    }

    public long getChosenMovieId() {
        return chosenMovieId;
    }

    public long getPersonalDataId() {
        return personalDataId;
    }

    public long getChosenSeatId() {
        return chosenSeatId;
    }

    public long getReservationId() {
        return reservationId;
    }
}
