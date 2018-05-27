package com.faleknatalia.cinemaBookingSystem.payment.model;

import java.util.List;

public class OrderResponseNotification {

    private String orderId;
    private String extOrderId;
    private String notifyUrl;

    private String customerIp;
    private String merchantPosId;
    private String description;
    private String currencyCode;
    private String totalAmount;
    private Buyer buyer;
    private PayMethod payMethod;
    private List<Product> products = null;
    private String status;

    public OrderResponseNotification(String orderId, String extOrderId, String notifyUrl, String customerIp, String merchantPosId, String description, String currencyCode, String totalAmount, Buyer buyer, PayMethod payMethod, List<Product> products, String status) {
        this.orderId = orderId;
        this.extOrderId = extOrderId;
        this.notifyUrl = notifyUrl;
        this.customerIp = customerIp;
        this.merchantPosId = merchantPosId;
        this.description = description;
        this.currencyCode = currencyCode;
        this.totalAmount = totalAmount;
        this.buyer = buyer;
        this.payMethod = payMethod;
        this.products = products;
        this.status = status;
    }

    public OrderResponseNotification() {
    }

    public String getOrderId() {
        return orderId;
    }

    public String getExtOrderId() {
        return extOrderId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getCustomerIp() {
        return customerIp;
    }

    public String getMerchantPosId() {
        return merchantPosId;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public PayMethod getPayMethod() {
        return payMethod;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getStatus() {
        return status;
    }
}
