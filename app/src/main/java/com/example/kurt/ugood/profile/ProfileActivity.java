package com.example.kurt.ugood.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kurt.ugood.R;
import com.example.kurt.ugood.firebase.FirebaseFunctions;
import com.example.kurt.ugood.firebase.GlideApp;
import com.example.kurt.ugood.main.HomeFragment;
import com.example.kurt.ugood.profile.Fragments.ProfileFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    TextView userName, favoriteQuote, calenderDays, longestStreak, totalDays;
    Button back, settings, favorites;
    FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        //setting title
        setTitle("Profile");

        //Setting back button up
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Sets the home fragment as the first fragment
        FragmentManager fM = getSupportFragmentManager();
        fM.beginTransaction().replace(R.id.profileContainer,
                new ProfileFragment()).commit();
        fM.executePendingTransactions();

//        userName = (TextView) findViewById(R.id.userNameTextProfile);
//        favoriteQuote = (TextView) findViewById(R.id.favoriteQuote);
//
//        fbAuth = FirebaseAuth.getInstance();
//        FirebaseFunctions.GetUserName(fbAuth, userName);
//        //FirebaseFunctions.GetRandomQuote(fbAuth, favoriteQuote);
//        //FirebaseFunctions.GetUserQuote(fbAuth, favoriteQuote);
//        //FirebaseFunctions.GetUserStats(fbAuth, calenderDays, longestStreak, totalDays);
//
//        back = (Button) findViewById(R.id.backButton);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        settings = (Button) findViewById(R.id.settingsButton);
//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        LoadImageFromFirebase();
    }
    //How to access backButton
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //After the first fragment is created each additional fragment added needs to be add to back stack.
                //We can use back stack to see how deep we are in detail fragments
                //If we are above fragment 1 then pop back to fragment 0, if we are on fragment 0 close activity
                Log.i(TAG, "onBackPressed: " + getSupportFragmentManager().getBackStackEntryCount());
                if (getSupportFragmentManager().getBackStackEntryCount() > 0){
                    getSupportFragmentManager().popBackStack();
                }else{
                    super.onBackPressed();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
       // LoadImageFromFirebase();
    }

//    public void LoadImageFromFirebase()
//    {
//        // Reference to an image file in Cloud Storage
//        String curUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Users").child(curUserID).child("Profile Pic");
//
//        Task<Uri> newTask = storageReference.getDownloadUrl();
//        newTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                ImageView image = findViewById(R.id.profileActivityPic);
//
//                // Download directly from StorageReference using Glide
//                // (See MyAppGlideModule for Loader registration)
//
//                GlideApp.with(getApplicationContext() /* context */)
//                        .load(uri)
//                        .into(image);
//            }
//        });
//    }
}
