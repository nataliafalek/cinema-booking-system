package com.faleknatalia.cinemaBookingSystem.model;

import com.faleknatalia.cinemaBookingSystem.dto.ChosenSeatAndPrice;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

//Serializable because it is used in the session

@Entity
public class Reservation implements Serializable {
    private long chosenMovieId;

    private PersonalData personalData;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String reservationId;

    @ElementCollection(targetClass = ChosenSeatAndPrice.class, fetch = FetchType.EAGER)
    private List<ChosenSeatAndPrice> chosenSeatsAndPrices;

    public Reservation(long chosenMovieId, PersonalData personalData, List<ChosenSeatAndPrice> chosenSeatsAndPrices) {
        this.chosenMovieId = chosenMovieId;
        this.personalData = personalData;
        this.chosenSeatsAndPrices = chosenSeatsAndPrices;
    }

    public Reservation() {
    }

    public long getChosenMovieId() {
        return chosenMovieId;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }

    public List<ChosenSeatAndPrice> getChosenSeatsAndPrices() {
        return chosenSeatsAndPrices;
    }

    public String getReservationId() {
        return reservationId;
    }


}
