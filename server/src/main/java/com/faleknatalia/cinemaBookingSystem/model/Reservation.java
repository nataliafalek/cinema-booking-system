package com.faleknatalia.cinemaBookingSystem.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Reservation {
    private long chosenMovieId;

    private long personalDataId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long reservationId;

    @ElementCollection(targetClass = Long.class)
    private List<Long> chosenSeatId;

    public Reservation(long chosenMovieId, long personalDataId, List<Long> chosenSeatId) {
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

    public List<Long> getChosenSeatId() {
        return chosenSeatId;
    }

    public long getReservationId() {
        return reservationId;
    }
}
