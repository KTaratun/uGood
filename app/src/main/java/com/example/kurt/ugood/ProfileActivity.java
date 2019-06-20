package com.example.kurt.ugood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.example.kurt.ugood.firebase.FirebaseFunctions;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    TextView userName, favoriteQuote, calenderDays, longestStreak, totalDays;
    Button back, settings, favorites;
    FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        userName = findViewById(R.id.userNameTextProfile);
        favoriteQuote = findViewById(R.id.favoriteQuote);

        fbAuth = FirebaseAuth.getInstance();
        FirebaseFunctions.GetUserName(fbAuth, userName);
        FirebaseFunctions.GetRandomQuote(fbAuth, favoriteQuote);
        //FirebaseFunctions.GetUserQuote(fbAuth, favoriteQuote);
        //FirebaseFunctions.GetUserStats(fbAuth, calenderDays, longestStreak, totalDays);

    }

}
