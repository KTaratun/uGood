package com.example.kurt.ugood.explore;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.kurt.ugood.R;
import com.example.kurt.ugood.classes.Interfaces.ExploreContentButtonClickListener;
import com.example.kurt.ugood.explore.ExploreContentRecyclerViewStuff.ExploreContentRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

public class ExploreFragment extends Fragment implements View.OnClickListener, ExploreContentButtonClickListener {

    private static final String TAG = "ExploreFragment";

    //Firebase
    FirebaseAuth FBAuth;

    //collectionReference
    private CollectionReference cR;

    //From what i remember list views are fine for a small amount of data, but if you plan on having
    // enough data to scroll you should use a reyclerview because it recycles views that are not currently being shown
    //For example in a list once that cell leaves the screen the cell is saved in memory then the new cell appearing from the bottom is also created in memory.
    //This is where recycler view comes in, when a cell leaves the screen in a recycler view it is the reused for the cell that is about to appear at the bottom.
    RecyclerView mExploreRecyclerView;
    //adapter
    ExploreContentRecyclerViewAdapter mExploreContentRecyclerAdapter;

    //data
    ArrayList<ExploreContent> mAllContent = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.explore_fragment, container, false);
        mExploreRecyclerView = view.findViewById(R.id.exploreContentRecyclerView);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpRecylerView();
    }

    private void setUpRecylerView(){
        //Layout manager is used to change recycler layout from a linear table to a vertical collectionview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mExploreRecyclerView.setLayoutManager(layoutManager);

        //creating adapter
        mExploreContentRecyclerAdapter = new ExploreContentRecyclerViewAdapter(mAllContent, getContext(),this);
        mExploreRecyclerView.setAdapter(mExploreContentRecyclerAdapter);

        FBAuth = FirebaseAuth.getInstance();
        GetAllContent(FBAuth);


    }

    private void GetAllContent(FirebaseAuth FBAuth)
    {
        final List<String> Content = new ArrayList<String>();

        cR = FirebaseFirestore.getInstance().collection("Quotes");

        cR.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    //Log.w(TAG, "listen:error", e);
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            ExploreContent content =  dc.getDocument().toObject(ExploreContent.class);
                            content.setiD(dc.getDocument().getId());
                            if(!mAllContent.contains(content)){
                                mAllContent.add(content);
                            }


                            //Log.d(TAG, "New content: " + dc.getDocument().getData());
                            break;
                        case MODIFIED:
                            //Log.d(TAG, "Modified content: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                           // Log.d(TAG, "Removed content: " + dc.getDocument().getData());
                            break;
                    }
                }

                mExploreContentRecyclerAdapter.notifyDataSetChanged();

            }
        });

//        cR.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
//                    Collections.reverse(myListOfDocuments);
//                    Iterator<DocumentSnapshot> it = myListOfDocuments.iterator();
//
//                    DocumentSnapshot currDoc;
//                    while (true)
//                    {
//                        if (it.hasNext())
//                            currDoc = it.next();
//                        else
//                            break;
//
//                        Content.add(currDoc.getReference().getId().toString());
//                    }
//                }
//                SetAdapter(Content);
//            }
//        });
    }

//    private void SetAdapter(List<String> strContent)
//    {
//        ArrayList<ExploreContent> expContent = new ArrayList<ExploreContent>();
//
//        for (int i = 0; i < strContent.size(); i++)
//            expContent.add(new ExploreContent(strContent.get(i)));
//
//        ListView listView = getView().findViewById(R.id.contentList);
//        listView.setAdapter(new ExploreContentAdapter(getActivity(), R.layout.explore_new_content_list, expContent));
//    }

    @Override
    public void onClick(View v) {

    }

    //Explore Content Button Click listener
    @Override
    public void onPositionHeartClicked(int Position) {
        Log.i(TAG, "onPositionHeartClicked: " + Position);
    }

    @Override
    public void onPositionCrownClicked(int Position) {
        Log.i(TAG, "onPositionCrownClicked: " + Position);
    }
}
