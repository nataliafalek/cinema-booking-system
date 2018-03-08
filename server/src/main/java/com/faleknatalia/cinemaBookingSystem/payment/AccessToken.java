package com.faleknatalia.cinemaBookingSystem.payment;

public class AccessToken {

    private String access_token;
    private String token_type;
    private int expires_in;
    private String grant_type;

    public AccessToken() {
    }

    public AccessToken(String access_token, String token_type, int expires_in, String grant_type) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.grant_type = grant_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getGrant_type() {
        return grant_type;
    }
}
