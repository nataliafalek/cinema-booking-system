package com.faleknatalia.cinemaBookingSystem.payment;

public class PropertyNotification {
    private String name;
    private String value;

    public PropertyNotification(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public PropertyNotification() {
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
