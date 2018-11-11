package com.faleknatalia.cinemaBookingSystem.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    @Value("${dev_mode}")
    private boolean devMode;

    @Value("${server.port}")
    private int serverPort;

    @Value("${clientId}")
    private String clientId;

    @Value("${clientSecret}")
    private String clientSecret;

    @Value("${notify_url}")
    private String notifyUrl;

    @Value("${redirect_url}")
    private String redirectUrl;

    @Value("${paymentAuthorizationUrl}")
    private String paymentAuthorizationUrl;

    @Value("${createOrderUrl}")
    private String createOrderUrl;

    @Value("${paymentCustomerIp}")
    private String customerIp;

    public Constants() {
    }

    public boolean isDevMode() {
        return devMode;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getPaymentAuthorizationUrl() {
        return paymentAuthorizationUrl;
    }

    public String getCreateOrderUrl() {
        return createOrderUrl;
    }

    public String getCustomerIp() {
        return customerIp;
    }

    public int getServerPort() {
        return serverPort;
    }
}
