package com.amy.haunt.util;

import android.app.Application;

import java.util.ArrayList;

public class HauntApi extends Application {
    private String userId;
    private String userEmail;
    private String preference;
    private ArrayList<String> genders;
    private ArrayList<String> likes;
    private String kids;
    private String smoking;
    private String drinking;
    private String zodiac;
    private static HauntApi instance;
    private int position;

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    public String getKids() {
        return kids;
    }

    public void setKids(String kids) {
        this.kids = kids;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public String getDrinking() {
        return drinking;
    }

    public void setDrinking(String drinking) {
        this.drinking = drinking;
    }

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
