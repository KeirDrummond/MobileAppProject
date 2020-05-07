package com.keir.ratemypet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ItemFinder.getInstance(this);

        setContentView(R.layout.activity_main);

        navBar = (BottomNavigationView) findViewById(R.id.NavBar);
        navBar.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SignIn()).commit();
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
                    selectedFragment = ProfileFragment.newInstance(Session.getInstance().getCurrentUser());
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

    public void HideTaskbar()
    {
        navBar.setVisibility(View.INVISIBLE);
    }

    public void ShowTaskbar()
    {
        navBar.setVisibility(View.VISIBLE);
    }

}
