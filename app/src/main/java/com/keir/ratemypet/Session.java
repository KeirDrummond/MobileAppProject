package com.keir.ratemypet;

public class Session {

    private static Session instance = null;
    private static UserAccount currentUser = null;

    private Session() {
    }

    public Session(UserAccount newUser) {
        currentUser = newUser;
        instance = this;
    }

    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance; }

    public UserAccount getCurrentUser() { return currentUser; }

}
