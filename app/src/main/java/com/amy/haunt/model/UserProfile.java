package com.amy.haunt.model;


import java.util.ArrayList;


public class UserProfile {
    private String firstName;
    private String lastName;
    private String userId;
    private String height;
    private String profilePhotoUrl;
    private String birthday;
    private ArrayList<String> likes;
    private ArrayList<String> matches;
    private String age;
    private String zodiac;

    public UserProfile() {
    }

    public UserProfile(String firstName, String lastName,
                       String userId, String height,
                       String profilePhotoUrl,
                       String birthday, ArrayList<String> likes,
                       ArrayList<String> matches, String age,
                       String zodiac) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.height = height;
        this.profilePhotoUrl = profilePhotoUrl;
        this.birthday = birthday;
        this.likes = likes;
        this.matches = matches;
        this.age = age;
        this.zodiac = zodiac;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public ArrayList<String> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<String> matches) {
        this.matches = matches;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
