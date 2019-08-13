package com.example.kurt.ugood.calendar.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.kurt.ugood.R;
import com.example.kurt.ugood.calendar.Adapters.WeekAdapter;

import java.util.ArrayList;

public class WeekFragment extends Fragment implements View.OnClickListener{

    ListView listView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_week_fragment, container, false);

        listView = view.findViewById(R.id.week_list);
        InitWeek();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View v) {


    }

    private void InitWeek()
    {
        ArrayList<String[]> arrayList = new ArrayList<>();

        for (int i = 0; i < 7; i++)
        {
            String[] values = new String[4];

            if (i == 0)
                values[0] = "Sun";
            else if (i == 1)
                values[0] = "Mon";
            else if (i == 2)
                values[0] = "Tue";
            else if (i == 3)
                values[0] = "Wed";
            else if (i == 4)
                values[0] = "Thu";
            else if (i == 5)
                values[0] = "Fri";
            else if (i == 6)
                values[0] = "Sat";

            for (int j = 0; j < 3; j++)
                values[j + 1] = "0";

            arrayList.add(values);
        }

        WeekAdapter arrayAdapter = new WeekAdapter(getActivity(), arrayList);

        listView.setAdapter(arrayAdapter);
    }
}
