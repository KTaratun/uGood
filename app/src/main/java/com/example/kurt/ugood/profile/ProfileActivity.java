package com.example.kurt.ugood.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kurt.ugood.R;
import com.example.kurt.ugood.firebase.FirebaseFunctions;
import com.example.kurt.ugood.firebase.GlideApp;
import com.example.kurt.ugood.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;

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

        LoadImageFromFirebase();
    }

    private void LoadImageFromFirebase()
    {
        // Reference to an image file in Cloud Storage
        String curUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Users").child(curUserID).child("Profile Pic");

        Task<Uri> newTask = storageReference.getDownloadUrl();
        newTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageView image = (ImageView)findViewById(R.id.profilePic);

                // Download directly from StorageReference using Glide
                // (See MyAppGlideModule for Loader registration)
                GlideApp.with(getApplicationContext() /* context */)
                        .load(uri)
                        .into(image);
            }
        });
    }
}
