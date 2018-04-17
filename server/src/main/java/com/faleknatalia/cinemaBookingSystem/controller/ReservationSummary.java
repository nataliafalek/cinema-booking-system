package com.faleknatalia.cinemaBookingSystem.controller;

import com.faleknatalia.cinemaBookingSystem.model.PersonalData;
import com.faleknatalia.cinemaBookingSystem.util.TicketData;

public class ReservationSummary {

    private TicketData ticketData;
    private PersonalData personalData;

    public ReservationSummary(TicketData ticketData, PersonalData personalData) {
        this.ticketData = ticketData;
        this.personalData = personalData;
    }

    public TicketData getTicketData() {
        return ticketData;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }
}
