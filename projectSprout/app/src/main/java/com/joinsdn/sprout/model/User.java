package com.joinsdn.sprout.model;

import android.content.Context;
import android.media.Image;
import android.provider.MediaStore;
import android.provider.Settings;

import com.joinsdn.sprout.profile.Profile;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String firstname, lastname;
    private String profilePicture;
    private String bio;

    private transient Profile profile;
    private transient List<User> matches;
    private String device_ID;

    public User(String firstname, String lastname, String profilePicture, String bio, Context ctx) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.profilePicture = profilePicture;
        this.bio = bio;
        this.device_ID = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<User> getMatches() {
        return this.matches;
    }

    public void setMatches(List<User> matches) {
        this.matches = matches;
    }

    public void addMatch(User user) {
        if (!this.matches.contains(user))
            this.matches.add(user);
    }
}
