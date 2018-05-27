package com.faleknatalia.cinemaBookingSystem.dto;

import com.faleknatalia.cinemaBookingSystem.model.PersonalData;
import com.faleknatalia.cinemaBookingSystem.ticket.TicketData;

public class ReservationSummaryDto {

    private TicketData ticketData;
    private PersonalData personalData;

    public ReservationSummaryDto(TicketData ticketData, PersonalData personalData) {
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
