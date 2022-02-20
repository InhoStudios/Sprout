package com.joinsdn.sprout.model;

import android.media.Image;
import android.provider.MediaStore;

import com.joinsdn.sprout.profile.Profile;

import java.util.List;

public class User {

    private String firstname, lastname;
    private Image profilePicture;
    private MediaStore.Images pictures;
    private String bio;

    private Profile profile;
    private List<User> matches;

    // PRE:
    // POST:
    public User() {

    }

}
