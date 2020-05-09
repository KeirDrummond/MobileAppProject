package com.keir.ratemypet;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Session {

    private static Session instance = null;
    private static UserAccount currentUser = null;
    GoogleSignInClient signInClient;
    Context context;

    public Session(UserAccount newUser, Context context) {
        currentUser = newUser;
        this.context = context;
        firebaseListener();
        instance = this;
    }

    public static synchronized Session getInstance() { return instance; }

    private void firebaseListener() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(context, SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    private boolean Authentication(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getUid() == currentUser.getUserId()) { return true; }
        return false;
    }

    public UserAccount getCurrentUser() { return currentUser; }

}
