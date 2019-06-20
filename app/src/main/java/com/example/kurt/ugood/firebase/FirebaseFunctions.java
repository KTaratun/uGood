package com.example.kurt.ugood.firebase;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Random;

public class FirebaseFunctions {

    static public void GetUserName(FirebaseAuth FBAuth, final TextView name)
    {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userNameRef;

        if (FBAuth.getCurrentUser() != null)
        {
            userNameRef = DBRef.child("Users").child(FBAuth.getCurrentUser().getUid()).child("name");

            userNameRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    name.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    static public void GetUserQuote(FirebaseAuth FBAuth, final TextView favQuote)
    {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference quoteRef;

        if (FBAuth.getCurrentUser() != null)
        {
            quoteRef = DBRef.child("Users").child(FBAuth.getCurrentUser().getUid()).child("favorite quote");

            quoteRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    favQuote.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    static public void GetRandomQuote(FirebaseAuth FBAuth, final TextView randQuote)
    {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference quoteRef;

        if (FBAuth.getCurrentUser() != null)
        {
            quoteRef = DBRef.child("Quotes");

            quoteRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int num = (int) dataSnapshot.getChildrenCount();
                    int randNum = new Random().nextInt(num);

                    Iterator<DataSnapshot> dbSnap = dataSnapshot.getChildren().iterator();

                    for (int i = 0; i < randNum; i++)
                        dbSnap.next();

                    randQuote.setText(dbSnap.next().getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    static public void GetUserStats(FirebaseAuth FBAuth, final TextView calDays, final TextView streak, final TextView totDays)
    {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference calDaysRef;
        DatabaseReference streakRef;
        DatabaseReference totDaysRef;

        if (FBAuth.getCurrentUser() != null)
        {
            calDaysRef = DBRef.child("Users").child(FBAuth.getCurrentUser().getUid()).child("calender days");
            streakRef = DBRef.child("Users").child(FBAuth.getCurrentUser().getUid()).child("longest streak");
            totDaysRef = DBRef.child("Users").child(FBAuth.getCurrentUser().getUid()).child("total Days");

            calDaysRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    calDays.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            streakRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    streak.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            totDaysRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    totDays.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
