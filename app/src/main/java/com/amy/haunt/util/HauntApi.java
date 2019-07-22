package com.amy.haunt.util;

import android.app.Application;

public class HauntApi extends Application {
    private String userId;
    private String userEmail;
    private static HauntApi instance;

    public static HauntApi getInstance() {
        if (instance == null)
            instance = new HauntApi();
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
