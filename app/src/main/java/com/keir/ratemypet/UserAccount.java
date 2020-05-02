package com.keir.ratemypet;

import com.google.firebase.auth.FirebaseUser;

public class UserAccount {

    private static UserAccount userAccount = null;

    private String userID;
    private String displayName;
    private String email;
    private int level;

    private UserAccount() {

    }

    public static synchronized UserAccount getUserAccount() {
        return userAccount;
    }

    public UserAccount(FirebaseUser user)
    {
        userID = user.getUid();
        displayName = user.getDisplayName();
        email = user.getEmail();
        level = 1;
    }

    public static void setUserAccount(UserAccount userAccount) {
        UserAccount.userAccount = userAccount;
    }
}
