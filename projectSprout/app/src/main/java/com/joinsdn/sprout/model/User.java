package com.joinsdn.sprout.model;

import android.media.Image;
import android.provider.MediaStore;

import com.joinsdn.sprout.profile.Profile;

import java.util.List;

public class User {

    private String firstname, lastname;
    private String profilePicture;
    private String bio;

    private Profile profile;
    private List<User> matches;

    public User(String firstname, String lastname, String profilePicture, String bio) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.profilePicture = profilePicture;
        this.bio = bio;
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
        return matches;
    }

    public void setMatches(List<User> matches) {
        this.matches = matches;
    }
}
