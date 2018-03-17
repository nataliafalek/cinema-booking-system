package com.faleknatalia.cinemaBookingSystem.controller;

import java.util.List;

public class PersonalDataAndReservationInfo {


    private long chosenMovie;
    private List<Long> chosenSeatId;
    private String email;
    private String name;
    private String phoneNumber;
    private String surname;


    public PersonalDataAndReservationInfo() {
    }

    public long getChosenMovie() {
        return chosenMovie;
    }

    public List<Long> getChosenSeatId() {
        return chosenSeatId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSurname() {
        return surname;
    }
}
