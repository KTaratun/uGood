package com.example.kurt.ugood.explore;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.kurt.ugood.R;
import com.example.kurt.ugood.firebase.FirebaseFunctions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExploreActivity extends AppCompatActivity {

    Button back;
    ListView list;
    FirebaseAuth FBAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore_activity);

        FBAuth = FirebaseAuth.getInstance();
        GetAllContent(FBAuth);

        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void GetAllContent(FirebaseAuth FBAuth)
    {
        final List<String> Content = new ArrayList<String>();

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

                    for (int i = 0; i < num; i++)
                        Content.add(dbSnap.next().getValue(String.class));

                    SetAdapter(Content);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void SetAdapter(List<String> strContent)
    {
        ArrayList<ExploreContent> expContent = new ArrayList<ExploreContent>();

        for (int i = 0; i < strContent.size(); i++)
            expContent.add(new ExploreContent(strContent.get(i)));

        ListView listView = findViewById(R.id.contentList);
        listView.setAdapter(new ExploreContentAdapter(this, R.layout.explore_new_content_list, expContent));
    }
}
