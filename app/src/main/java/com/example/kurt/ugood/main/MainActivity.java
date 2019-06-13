package com.example.kurt.ugood.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kurt.ugood.diagnostic.DiagnosticActivity;
import com.example.kurt.ugood.main.Fragments.HomeFragment;
import com.example.kurt.ugood.main.Fragments.MentalFragment;
import com.example.kurt.ugood.main.Fragments.PhysicalFragment;
import com.example.kurt.ugood.main.Fragments.SocialFragment;
import com.example.kurt.ugood.main.Fragments.SuccessFragment;
import com.example.kurt.ugood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView name;
    FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        fbAuth = FirebaseAuth.getInstance();
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference userNameRef;
        if (fbAuth.getCurrentUser() != null)
        {
            userNameRef = DBRef.child("Users").child(fbAuth.getCurrentUser().getUid()).child("name");

            userNameRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    name = findViewById(R.id.username);
                    name.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

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

                    if (selectedFragment != null)
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}
