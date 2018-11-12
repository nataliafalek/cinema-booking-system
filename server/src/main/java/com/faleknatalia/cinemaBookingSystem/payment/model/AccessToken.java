package com.faleknatalia.cinemaBookingSystem.payment.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessToken that = (AccessToken) o;
        return expires_in == that.expires_in &&
                Objects.equals(access_token, that.access_token) &&
                Objects.equals(token_type, that.token_type) &&
                Objects.equals(grant_type, that.grant_type);
    }

    @Override
    public int hashCode() {

        return Objects.hash(access_token, token_type, expires_in, grant_type);
    }
}
