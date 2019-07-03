package com.example.kurt.ugood.firebase;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import java.util.Iterator;
import java.util.Random;

import javax.annotation.Nullable;

public class FirebaseFunctions {

    static public void GetUserName(FirebaseAuth FBAuth, final TextView name)
    {
        DocumentReference dR;
        String userId = FBAuth.getCurrentUser().getUid();

        dR = FirebaseFirestore.getInstance().collection("Users").document(userId);

        dR.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    //Toast.makeText(MainActivity.this, "Error while loading!", Toast.LENGTH_SHORT);
                    //Log.d("tag", e.toString());
                    //return;
                }

                if (documentSnapshot.exists())
                    name.setText(documentSnapshot.get("name").toString());
            }
        });
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

    static public void GetContentByIndex(FirebaseAuth FBAuth, final TextView targetContent, final int Index)
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

                    Iterator<DataSnapshot> dbSnap = dataSnapshot.getChildren().iterator();

                    for (int i = 0; i < Index; i++)
                        dbSnap.next();

                    targetContent.setText(dbSnap.next().getValue(String.class));
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
