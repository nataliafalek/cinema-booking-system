package com.faleknatalia.cinemaBookingSystem.payment;

import java.util.List;

public class OrderRequest {
    private String extOrderId;
    private String notifyUrl;
    private String customerIp;
    private String merchantPosId;
    private String description;
    private String currencyCode;
    private String totalAmount;
    private Buyer buyer;
    private List<Product> products;
    private String continueUrl;

    public OrderRequest(String extOrderId, String notifyUrl, String customerIp, String merchantPosId, String description, String currencyCode, String totalAmount, Buyer buyer, List<Product> products, String continueUrl) {
        this.extOrderId = extOrderId;
        this.notifyUrl = notifyUrl;
        this.customerIp = customerIp;
        this.merchantPosId = merchantPosId;
        this.description = description;
        this.currencyCode = currencyCode;
        this.totalAmount = totalAmount;
        this.buyer = buyer;
        this.products = products;
        this.continueUrl = continueUrl;
    }

    public OrderRequest() {
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

    public List<Product> getProducts() {
        return products;
    }

    public String getContinueUrl() {
        return continueUrl;
    }

    public String getExtOrderId() {
        return extOrderId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }
}
