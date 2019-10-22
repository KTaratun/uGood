package com.example.kurt.ugood.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.kurt.ugood.calendar.Fragments.CalenderFragment;
import com.example.kurt.ugood.classes.User;
import com.example.kurt.ugood.classes.Interfaces.UserUpdatedDataHandler;
import com.example.kurt.ugood.explore.ExploreFragment;
import com.example.kurt.ugood.R;
import com.example.kurt.ugood.firebase.GlideApp;
import com.example.kurt.ugood.profile.ProfileActivity;
import com.google.firebase.firestore.ListenerRegistration;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener, View.OnClickListener, UserUpdatedDataHandler<User> {

    //Action Bar UI
    CircleImageView mProfilePic;
    TextView mUserName;


    //user Data
    User mCurrentUser;
    ListenerRegistration mUserInfoListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setTitle("home");
        setContentView(R.layout.main_activity);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Sets the home fragment as the first fragment
        FragmentManager fM = getSupportFragmentManager();
        fM.beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

        fM.executePendingTransactions();

        mCurrentUser = User.retreiveUserObjectFromUserDefaults(this);
        createActionBar();

        mUserInfoListener = mCurrentUser.CheckForUserDataUpdates(this, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mUserInfoListener == null){
            mUserInfoListener = mCurrentUser.CheckForUserDataUpdates(this, this);
        }

        updateCurrentActionBarWithUserData();

    }

    private void updateCurrentActionBarWithUserData(){
        mUserName.setText(mCurrentUser.getUsername());
        GlideApp.with(this /* context */)
                .load(mCurrentUser.getProfilePic())
                .into(mProfilePic);
    }

    private void createActionBar(){
        //Setting up action bar custom view
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.homepage_actionbar_layout, null);

        //Init UI
        mProfilePic = (CircleImageView) v.findViewById(R.id.actionBarProfilePic);
        mUserName = v.findViewById(R.id.actionBarUsername);

        //attaching listener to
        mProfilePic.setOnClickListener(this);
        mUserName.setOnClickListener(this);

        updateCurrentActionBarWithUserData();
        getSupportActionBar().setCustomView(v);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            //getSupportActionBar().setTitle("home");
                            //getActionBar().setTitle("Home");
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_calendar:
                            //getSupportActionBar().setTitle("Calendar");
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
    protected void onPause() {
        super.onPause();
        mUserInfoListener.remove();
        mUserInfoListener = null;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View view) {
        if(view == mProfilePic || view == mUserName){
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
    }

    public void setNavBarTitle(String Title){
        getSupportActionBar().setTitle(Title);
    }

    @Override
    public void updateUserData(User user) {
        mCurrentUser = user;
        updateCurrentActionBarWithUserData();
    }
}
