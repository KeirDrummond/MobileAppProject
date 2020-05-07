package com.keir.ratemypet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends Fragment {

    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            CheckForUser(firebaseUser);
        }
    }

    private void CompleteSignIn() {
        ((MainActivity) getActivity()).UserDisplay(true);
        ((MainActivity) getActivity()).ChangeFragment(new HomeFragment());
    }

    private void CreateUser(FirebaseUser fbUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final UserAccount user = new UserAccount(fbUser);

        db.collection("users").document(fbUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Success
                        new Session(user);
                        CompleteSignIn();
                    }
                });
    }

    private void CheckForUser(final FirebaseUser firebaseUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("users").document(firebaseUser.getUid());
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("muhtag", "Good!");
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (!documentSnapshot.exists()) {
                        CreateUser(firebaseUser);
                        Log.d("muhtag", "Doesn't exist!");
                    }
                    else
                    {
                        UserAccount user = new UserAccount(firebaseUser);
                        new Session(user);
                        CompleteSignIn();
                        Log.d("muhtag", "Does exist!");
                    }
                }
                else
                {
                    Log.d("muhtag", task.getException().toString());
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ((MainActivity) getActivity()).HideTaskbar();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);

        firebaseAuth = FirebaseAuth.getInstance();

        SignInButton signInButton = view.findViewById(R.id.Sign_in_btn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });

        return view;
    }

    private void SignIn()
    {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void SignOut()
    {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);


            } catch (ApiException e) {
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            CheckForUser(user);
                        }
                    }
                });
    }



}
