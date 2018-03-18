package com.faleknatalia.cinemaBookingSystem.payment;

import java.util.List;

public class NotificationResponse {

    private OrderResponseNotification order;
    private String localReceiptDateTime;
    private List<PropertyNotifcation> properties;

    public NotificationResponse() {
    }

    public NotificationResponse(OrderResponseNotification order, String localReceiptDateTime, List<PropertyNotifcation> properties) {
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

    public List<PropertyNotifcation> getProperties() {
        return properties;
    }
}
