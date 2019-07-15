package com.amy.haunt.model;


import com.google.firebase.firestore.auth.User;


public class UserProfile {
    private String firstName;
    private String lastName;
    private String userId;
    private String height;
    private String gender;
    private String profilePhotoUrl;
    private String birthday;

    public UserProfile() {
    }

    public UserProfile(String firstName, String lastName, String userId, String height, String gender, String profilePhotoUrl, String birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.height = height;
        this.gender = gender;
        this.profilePhotoUrl = profilePhotoUrl;
        this.birthday = birthday;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
