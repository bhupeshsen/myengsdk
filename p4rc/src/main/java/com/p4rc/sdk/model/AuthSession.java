package com.p4rc.sdk.model;

import org.json.JSONObject;

public class AuthSession {
    private String sessionToken;
    private User user;

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public static AuthSession fromJSON(JSONObject json) {
        AuthSession authSession = new AuthSession();
        authSession.setSessionToken(json.optString("sessionToken"));
        authSession.setUser(User.fromJSON(json.optJSONObject("user")));
        return authSession;
    }
}
