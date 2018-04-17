package com.faleknatalia.cinemaBookingSystem.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Reservation implements Serializable {
    private long chosenMovieId;

    private long personalDataId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long reservationId;

    @ElementCollection(targetClass = ChosenSeatAndPrice.class, fetch = FetchType.EAGER)
    private List<ChosenSeatAndPrice> chosenSeatsAndPrices;

    public Reservation(long chosenMovieId, long personalDataId, List<ChosenSeatAndPrice> chosenSeatsAndPrices) {
        this.chosenMovieId = chosenMovieId;
        this.personalDataId = personalDataId;
        this.chosenSeatsAndPrices = chosenSeatsAndPrices;
    }

    public Reservation() {
    }

    public long getChosenMovieId() {
        return chosenMovieId;
    }

    public long getPersonalDataId() {
        return personalDataId;
    }

    public List<ChosenSeatAndPrice> getChosenSeatsAndPrices() {
        return chosenSeatsAndPrices;
    }

    public long getReservationId() {
        return reservationId;
    }

    public void setPersonalDataId(long personalDataId) {
        this.personalDataId = personalDataId;
    }
}
