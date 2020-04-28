package com.keir.ratemypet;

public class UserAccount {

    private static UserAccount userAccount = null;

    public String DisplayName;
    public int Level;

    private UserAccount() {

    }

    public static synchronized UserAccount getUserAccount() {
        if (null == userAccount)
            userAccount = new UserAccount();
        return userAccount;
    }

}
