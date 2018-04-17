package com.faleknatalia.cinemaBookingSystem.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TicketPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ticketPriceId;

    private String ticketType;
    private int ticketValue;

    public TicketPrice() {
    }

    public TicketPrice(String ticketType, int ticketValue) {
        this.ticketType = ticketType;
        this.ticketValue = ticketValue;
    }

    public long getTicketPriceId() {
        return ticketPriceId;
    }

    public String getTicketType() {
        return ticketType;
    }

    public int getTicketValue() {
        return ticketValue;
    }
}
