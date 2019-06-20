package com.example.kurt.ugood.main;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.kurt.ugood.firebase.FirebaseFunctions;
import com.example.kurt.ugood.ProfileActivity;
import com.example.kurt.ugood.main.Fragments.HomeFragment;
import com.example.kurt.ugood.main.Fragments.MindFragment;
import com.example.kurt.ugood.main.Fragments.BodyFragment;
import com.example.kurt.ugood.main.Fragments.SpiritFragment;
import com.example.kurt.ugood.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener{

    TextView name;
    FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        name = findViewById(R.id.username);

        fbAuth = FirebaseAuth.getInstance();
        FirebaseFunctions.GetUserName(fbAuth, name);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Sets the home fragment as the first fragment
        FragmentManager fM = getSupportFragmentManager();
        fM.beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

        fM.executePendingTransactions();

        // Profile Bar button
        ConstraintLayout profileBarButton = findViewById(R.id.status_bar);
        profileBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_mind:
                            selectedFragment = new MindFragment();
                            break;
                        case R.id.nav_body:
                            selectedFragment = new BodyFragment();
                            break;
                        case R.id.nav_spirit:
                            selectedFragment = new SpiritFragment();
                            break;
                    }

                    getSupportFragmentManager().executePendingTransactions();

                    if (selectedFragment != null)
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
