package com.example.kurt.ugood.calendar.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kurt.ugood.R;
import com.example.kurt.ugood.calendar.Adapters.CalendarPagerAdapter;

public class CalenderFragment extends Fragment implements View.OnClickListener{

    private Button graph;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);

        graph = view.findViewById(R.id.graphButton);
        graph.setOnClickListener(this);

        SetUpTabs(view);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == graph && getActivity().getSupportFragmentManager() != null)
        {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new GraphFragment()).commit();
        }
    }

    private void SetUpTabs(View v)
    {
        TabLayout tabLayout = v.findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Day"));
        tabLayout.addTab(tabLayout.newTab().setText("Week"));
        tabLayout.addTab(tabLayout.newTab().setText("Month"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = v.findViewById(R.id.calender_pager);

        CalendarPagerAdapter adapter = null;
        if (getActivity().getSupportFragmentManager() != null)
            adapter = new CalendarPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
