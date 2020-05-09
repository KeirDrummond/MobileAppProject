package com.keir.ratemypet;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class UserAccount implements Serializable {

    private String userId;
    private String displayName;
    private String email;
    private long userLevel;
    private long userExp;
    private long ratingScore;
    private long uploadScore;

    public UserAccount(FirebaseUser user) {
        userId = user.getUid();
        displayName = user.getDisplayName();
        email = user.getEmail();
        userLevel = 1;
        userExp = 0;
        ratingScore = 0;
        uploadScore = 0;
    }

    public UserAccount() {

    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public long getUserExp() { return userExp; }

    public long getRatingScore() { return ratingScore; }

    public long getUploadScore() { return uploadScore; }

    public long getUserLevel() { return userLevel; }
}
