package com.example.kurt.ugood.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.kurt.ugood.Fragments.HomeFragment;
import com.example.kurt.ugood.Fragments.MentalFragment;
import com.example.kurt.ugood.Fragments.PhysicalFragment;
import com.example.kurt.ugood.R;
import com.example.kurt.ugood.Fragments.SocialFragment;
import com.example.kurt.ugood.Fragments.SuccessFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportActionBar().hide();

        // Sets the home fragment as the first fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

        Button checkInStartButton = findViewById(R.id.button_diagnostic);
        checkInStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiagnosticActivity.class);
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
                        case R.id.nav_mental:
                            selectedFragment = new MentalFragment();
                            break;
                        case R.id.nav_success:
                            selectedFragment = new SuccessFragment();
                            break;
                        case R.id.nav_physical:
                            selectedFragment = new PhysicalFragment();
                            break;
                        case R.id.nav_social:
                            selectedFragment = new SocialFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}
