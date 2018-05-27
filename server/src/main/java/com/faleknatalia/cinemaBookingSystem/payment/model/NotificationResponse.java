package com.faleknatalia.cinemaBookingSystem.payment.model;

import java.util.List;

public class NotificationResponse {

    private OrderResponseNotification order;
    private String localReceiptDateTime;
    private List<PropertyNotification> properties;

    public NotificationResponse() {
    }

    public NotificationResponse(OrderResponseNotification order, String localReceiptDateTime, List<PropertyNotification> properties) {
        this.order = order;
        this.localReceiptDateTime = localReceiptDateTime;
        this.properties = properties;
    }

    public OrderResponseNotification getOrder() {
        return order;
    }

    public String getLocalReceiptDateTime() {
        return localReceiptDateTime;
    }

    public List<PropertyNotification> getProperties() {
        return properties;
    }
}
