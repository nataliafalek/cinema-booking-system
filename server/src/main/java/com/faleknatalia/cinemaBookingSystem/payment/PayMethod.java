package com.faleknatalia.cinemaBookingSystem.payment;

public class PayMethod {

    private String type;

    public PayMethod(String type) {
        this.type = type;
    }

    public PayMethod() {
    }

    public String getType() {
        return type;
    }

}