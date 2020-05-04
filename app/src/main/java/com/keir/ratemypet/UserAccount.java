package com.keir.ratemypet;

import com.google.firebase.auth.FirebaseUser;

public class UserAccount {

    private String userID;
    private String displayName;
    private String email;
    private int level;

    public UserAccount(FirebaseUser user)
    {
        userID = user.getUid();
        displayName = user.getDisplayName();
        email = user.getEmail();
        level = 1;
    }

    public String getUserID() {
        return userID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public int getLevel() {
        return level;
    }
}
