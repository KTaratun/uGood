package com.example.kurt.ugood.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kurt.ugood.R;
import com.example.kurt.ugood.firebase.FirebaseFunctions;
import com.example.kurt.ugood.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    TextView userName, favoriteQuote, calenderDays, longestStreak, totalDays;
    Button back, settings, favorites;
    FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        userName = (TextView) findViewById(R.id.userNameTextProfile);
        favoriteQuote = (TextView) findViewById(R.id.favoriteQuote);

        fbAuth = FirebaseAuth.getInstance();
        FirebaseFunctions.GetUserName(fbAuth, userName);
        FirebaseFunctions.GetRandomQuote(fbAuth, favoriteQuote);
        //FirebaseFunctions.GetUserQuote(fbAuth, favoriteQuote);
        //FirebaseFunctions.GetUserStats(fbAuth, calenderDays, longestStreak, totalDays);

        back = (Button) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        settings = (Button) findViewById(R.id.settingsButton);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
