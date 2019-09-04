package com.example.kurt.ugood.calendar.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.example.kurt.ugood.calendar.Adapters.ExpandableListAdapter;

import com.example.kurt.ugood.R;
import com.example.kurt.ugood.calendar.HomeCollection;
import com.example.kurt.ugood.diagnostic.Classes.Question;
import com.example.kurt.ugood.diagnostic.Classes.QuestionAdapter;
import com.example.kurt.ugood.diagnostic.DiagnosticActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class DayFragment extends Fragment implements View.OnClickListener{

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String[]>> listHash;
    private FirebaseAuth FBAuth;
    private DocumentReference dR;
    private List<String[]> beforeSchool, school, afterSchool, eveningActivities;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_day_fragment, container, false);

        FBAuth = FirebaseAuth.getInstance();

        listView = view.findViewById(R.id.scheduleLV);
        InitData();
        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listHash);
        listView.setAdapter(listAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    private void InitData()
    {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("Before School");
        listDataHeader.add("School");
        listDataHeader.add("After School");
        listDataHeader.add("Evening Activities");

        beforeSchool = new ArrayList<>();
        beforeSchool.add(new String[] {"6:30 AM : Eat Breakfast", "ic_local_dining_black_24dp"});
        beforeSchool.add(new String[] {"7:00 AM : Shower", "ic_beach_access_black_24dp"});

        afterSchool = new ArrayList<>();
        afterSchool.add(new String[] {"3:00 PM : Take trash out", "ic_delete_black_24dp"});
        afterSchool.add(new String[] {"3:15 PM : Homework", "ic_create_black_24dp"});

        eveningActivities = new ArrayList<>();
        eveningActivities.add(new String[] {"4:00 PM : Play game", "ic_videogame_asset_black_24dp"});
        eveningActivities.add(new String[] {"6:00 PM : Hang out with friends", "ic_people_black_24dp"});

        school = new ArrayList<>();
        SetSchoolSchedule();
    }

    private void SetSchoolSchedule()
    {
        if (FBAuth.getCurrentUser() != null)
        {
            dR = FirebaseFirestore.getInstance().collection("Schools").document("Edison");

            dR.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        school.add(new String[] {task.getResult().get("Period 1").toString(), "ic_invert_colors_black_24dp"});
                        school.add(new String[] {task.getResult().get("Period 2").toString(), "ic_record_voice_over_black_24dp"});
                        school.add(new String[] {task.getResult().get("Period 3").toString(), "ic_assignment_black_24dp"});
                        school.add(new String[] {task.getResult().get("Period 4").toString(), "ic_local_dining_black_24dp"});
                        school.add(new String[] {task.getResult().get("Period 5").toString(), "ic_gesture_black_24dp"});
                        school.add(new String[] {task.getResult().get("Period 6").toString(), "ic_record_voice_over_black_24dp"});
                    }

                    ContinueCreate();
                }
            });
        }
    }

    private void ContinueCreate()
    {
        listHash.put(listDataHeader.get(0), beforeSchool);
        listHash.put(listDataHeader.get(1), school);
        listHash.put(listDataHeader.get(2), afterSchool);
        listHash.put(listDataHeader.get(3), eveningActivities);
    }

    @Override
    public void onClick(View v) {

    }
}
