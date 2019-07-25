package com.amy.haunt.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class UserProfile implements Parcelable {
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("first_name")
    private String lastName;
    @SerializedName("last_name")
    private String userId;
    @SerializedName("user_id")
    private String height;
    @SerializedName("profile_photo_url")
    private String profilePhotoUrl;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("likes")
    private ArrayList<String> likes;
    @SerializedName("matches")
    private ArrayList<String> matches;
    @SerializedName("genders")
    private ArrayList<String> genders;
    @SerializedName("age")
    private String age;
    @SerializedName("zodiac")
    private String zodiac;
    @SerializedName("preference")
    private String preference;

    public UserProfile() {
    }

    public UserProfile(String firstName, String lastName,
                       String userId, String height,
                       String profilePhotoUrl, String preference,
                       String birthday, ArrayList<String> likes,
                       ArrayList<String> matches, String age,
                       String zodiac, ArrayList<String> genders) {
        this.genders = genders;
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
        this.preference = preference;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.userId);
        dest.writeString(this.height);
        dest.writeList(this.likes);
        dest.writeString(this.profilePhotoUrl);
        dest.writeString(this.preference);
        dest.writeString(this.birthday);
        dest.writeList(this.matches);
        dest.writeString(this.age);
        dest.writeString(this.zodiac);
        dest.writeList(this.genders);
    }

    protected UserProfile(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.userId = in.readString();
        this.height = in.readString();
        this.profilePhotoUrl = in.readString();
        this.preference = in.readString();
        this.birthday = in.readString();
        this.likes = new ArrayList<String>();
        in.readList(this.likes, String.class.getClassLoader());
        this.matches = new ArrayList<String>();
        in.readList(this.matches, String.class.getClassLoader());
        this.genders = new ArrayList<String>();
        in.readList(this.genders, String.class.getClassLoader());
        this.age = in.readString();
        this.zodiac = in.readString();
    }

    public static final Parcelable.Creator<UserProfile> CREATOR = new Parcelable.Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel source) {
            return new UserProfile(source);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };
}
