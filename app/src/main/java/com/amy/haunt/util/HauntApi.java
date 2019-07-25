package com.amy.haunt.util;

import android.app.Application;

import java.util.ArrayList;

public class HauntApi extends Application {
    private String userId;
    private String userEmail;
    private String preference;
    private ArrayList<String> genders;
    private ArrayList<String> likes;
    private static HauntApi instance;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static HauntApi getInstance() {
        if (instance == null)
            instance = new HauntApi();
        return instance;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public ArrayList<String> getGenders() {
        return genders;
    }

    public void setGenders(ArrayList<String> genders) {
        this.genders = genders;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
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
