package com.faleknatalia.cinemaBookingSystem.payment;


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

}
