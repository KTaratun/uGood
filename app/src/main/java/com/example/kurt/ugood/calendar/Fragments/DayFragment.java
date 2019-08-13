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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DayFragment extends Fragment implements View.OnClickListener{

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String[]>> listHash;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_day_fragment, container, false);

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

        List<String[]> beforeSchool = new ArrayList<>();
        beforeSchool.add(new String[] {"6:30 AM : Eat Breakfast", "ic_local_dining_black_24dp"});
        beforeSchool.add(new String[] {"7:00 AM : Shower", "ic_beach_access_black_24dp"});

        List<String[]> school = new ArrayList<>();
        school.add(new String[] {"7:30 AM : Chemistry", "ic_invert_colors_black_24dp"});
        school.add(new String[] {"9:00 AM : Spanish", "ic_record_voice_over_black_24dp"});
        school.add(new String[] {"10:30 AM : History", "ic_assignment_black_24dp"});
        school.add(new String[] {"12:00 PM : Lunch", "ic_local_dining_black_24dp"});
        school.add(new String[] {"12:30 PM : Physics", "ic_gesture_black_24dp"});
        school.add(new String[] {"2:00 PM : English", "ic_record_voice_over_black_24dp"});

        List<String[]> afterSchool = new ArrayList<>();
        afterSchool.add(new String[] {"3:00 PM : Take trash out", "ic_delete_black_24dp"});
        afterSchool.add(new String[] {"3:15 PM : Homework", "ic_create_black_24dp"});

        List<String[]> eveningActivities = new ArrayList<>();
        eveningActivities.add(new String[] {"4:00 PM : Play game", "ic_videogame_asset_black_24dp"});
        eveningActivities.add(new String[] {"6:00 PM : Hang out with friends", "ic_people_black_24dp"});

        listHash.put(listDataHeader.get(0), beforeSchool);
        listHash.put(listDataHeader.get(1), school);
        listHash.put(listDataHeader.get(2), afterSchool);
        listHash.put(listDataHeader.get(3), eveningActivities);
    }

    @Override
    public void onClick(View v) {

    }
}
