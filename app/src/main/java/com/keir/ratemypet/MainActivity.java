package com.keir.ratemypet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FrameLayout fragmentContainer;
    ImageView loadingOverlay;
    BottomNavigationView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = this;

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            final String firebaseUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("users").document(firebaseUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            UserAccount user = task.getResult().toObject(UserAccount.class);
                            new Session(user, context);
                            SetupActivity();
                        }
                        else {
                            final UserAccount user = new UserAccount(firebaseUser);
                            firestore.collection("users").document(user.getUserId())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //Success
                                            new Session(user, context);
                                            SetupActivity();
                                        }
                                    });
                        }
                    }
                }
            });
        }
        else {
            Intent intent = new Intent(this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    private void SetupActivity() {
        ItemFinder.getInstance(this);

        setContentView(R.layout.activity_main);

        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
        loadingOverlay = (ImageView) findViewById(R.id.loading);

        navBar = (BottomNavigationView) findViewById(R.id.NavBar);
        navBar.setOnNavigationItemSelectedListener(navListener);

        UserDisplay(true);
        TaskbarDisplay(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_gallery:
                    selectedFragment = new GalleryFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = ProfileFragment.newInstance(Session.getInstance().getCurrentUser().getUserId());
                    break;
            }
            ChangeFragment(selectedFragment);
            return true;
        }
    };

    public void ChangeFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void UserDisplay(boolean state) {
        LinearLayout userDisplayLayout = findViewById(R.id.userDisplay);
        TextView usernameText = findViewById(R.id.username);
        if (state) {
            userDisplayLayout.setVisibility(View.VISIBLE);
            usernameText.setText(Session.getInstance().getCurrentUser().getDisplayName());
        } else { userDisplayLayout.setVisibility(View.INVISIBLE); }
    }

    public void Loading(boolean loading) {
        if (loading) {
            loadingOverlay.setVisibility(View.VISIBLE);
            fragmentContainer.setVisibility(View.INVISIBLE);
        }
        else {
            loadingOverlay.setVisibility(View.INVISIBLE);
            fragmentContainer.setVisibility(View.VISIBLE);
        }
    }

    public void OpenItems(Intent intent) {
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Loading(false);

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void TaskbarDisplay(boolean state)
    {
        if (state) {
            navBar.setVisibility(View.VISIBLE);
        }
        else {
            navBar.setVisibility(View.INVISIBLE);
        }

    }

}
