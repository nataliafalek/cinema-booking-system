package com.faleknatalia.cinemaBookingSystem.payment.model;


import java.util.Objects;

public class OrderResponse {


    private String orderId;
    private String extOrderId;
    private String statusCode;
    private String redirectUri;

    public OrderResponse(String orderId, String extOrderId, String statusCode, String redirectUri) {
        this.orderId = orderId;
        this.extOrderId = extOrderId;
        this.statusCode = statusCode;
        this.redirectUri = redirectUri;
    }

    public OrderResponse() {
    }

    public String getOrderId() {
        return orderId;
    }

    public String getExtOrderId() {
        return extOrderId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderResponse that = (OrderResponse) o;
        return Objects.equals(orderId, that.orderId) &&
                Objects.equals(extOrderId, that.extOrderId) &&
                Objects.equals(statusCode, that.statusCode) &&
                Objects.equals(redirectUri, that.redirectUri);
    }

    @Override
    public int hashCode() {

        return Objects.hash(orderId, extOrderId, statusCode, redirectUri);
    }
}
