package com.example.kurt.ugood.explore;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.kurt.ugood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ExploreFragment extends Fragment implements View.OnClickListener {
    FirebaseAuth FBAuth;
    private CollectionReference cR;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.explore_fragment, container, false);

        FBAuth = FirebaseAuth.getInstance();
        GetAllContent(FBAuth);

        // Inflate the layout for this fragment
        return view;
    }

    private void GetAllContent(FirebaseAuth FBAuth)
    {
        final List<String> Content = new ArrayList<String>();

        cR = FirebaseFirestore.getInstance().collection("Quotes");

        cR.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                    Collections.reverse(myListOfDocuments);
                    Iterator<DocumentSnapshot> it = myListOfDocuments.iterator();

                    DocumentSnapshot currDoc;
                    while (true)
                    {
                        if (it.hasNext())
                            currDoc = it.next();
                        else
                            break;

                        Content.add(currDoc.getReference().getId().toString());
                    }
                }
                SetAdapter(Content);
            }
        });
    }

    private void SetAdapter(List<String> strContent)
    {
        ArrayList<ExploreContent> expContent = new ArrayList<ExploreContent>();

        for (int i = 0; i < strContent.size(); i++)
            expContent.add(new ExploreContent(strContent.get(i)));

        ListView listView = getView().findViewById(R.id.contentList);
        listView.setAdapter(new ExploreContentAdapter(getActivity(), R.layout.explore_new_content_list, expContent));
    }

    @Override
    public void onClick(View v) {

    }
}
