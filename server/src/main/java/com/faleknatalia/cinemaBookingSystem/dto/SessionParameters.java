package com.faleknatalia.cinemaBookingSystem.dto;

import com.faleknatalia.cinemaBookingSystem.model.PersonalData;
import com.faleknatalia.cinemaBookingSystem.model.Seat;
import com.faleknatalia.cinemaBookingSystem.model.SeatReservationByScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.model.TicketPrice;

import java.util.List;

public class SessionParameters {

    private long chosenMovieId;
    private String dayOfProjection;
    private List<SeatReservationByScheduledMovie> chosenSeats;
    private List<TicketPrice> chosenPriceList;
    private PersonalData personalData;

    public SessionParameters(long chosenMovieId, String dayOfProjection, List<SeatReservationByScheduledMovie> chosenSeats, List<TicketPrice> chosenPriceList, PersonalData personalData) {
        this.chosenMovieId = chosenMovieId;
        this.dayOfProjection = dayOfProjection;
        this.chosenSeats = chosenSeats;
        this.chosenPriceList = chosenPriceList;
        this.personalData = personalData;
    }

    public List<SeatReservationByScheduledMovie> getChosenSeats() {
        return chosenSeats;
    }

    public String getDayOfProjection() {
        return dayOfProjection;
    }

    public long getChosenMovieId() {
        return chosenMovieId;
    }

    public List<TicketPrice> getChosenPriceList() {
        return chosenPriceList;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }
}
