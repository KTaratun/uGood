package com.example.kurt.ugood.main;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.kurt.ugood.calendar.Fragments.CalenderFragment;
import com.example.kurt.ugood.explore.ExploreFragment;
import com.example.kurt.ugood.R;

public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("home");
        setContentView(R.layout.main_activity);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Sets the home fragment as the first fragment
        FragmentManager fM = getSupportFragmentManager();
        fM.beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

        fM.executePendingTransactions();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            getSupportActionBar().setTitle("home");
                            //getActionBar().setTitle("Home");
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_calendar:
                            getSupportActionBar().setTitle("Calendar");
                            //getActionBar().setTitle("Calendar");
                            selectedFragment = new CalenderFragment();
                            break;
                        case R.id.nav_explore:
                            selectedFragment = new ExploreFragment();
                            break;
                        //case R.id.nav_activities:
                        //    selectedFragment = new ScheduleFragment();
                        //    break;
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

    public void setNavBarTitle(String Title){
        getSupportActionBar().setTitle(Title);
    }
}
