package com.faleknatalia.cinemaBookingSystem.payment;

public class PropertyNotifcation {
    private String name;
    private String value;

    public PropertyNotifcation(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public PropertyNotifcation() {
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
