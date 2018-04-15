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

    private String ticketName;
    private int ticketValue;

    public TicketPrice() {
    }

    public TicketPrice(String ticketName, int ticketValue) {
        this.ticketName = ticketName;
        this.ticketValue = ticketValue;
    }

    public long getTicketPriceId() {
        return ticketPriceId;
    }

    public String getTicketName() {
        return ticketName;
    }

    public int getTicketValue() {
        return ticketValue;
    }
}
