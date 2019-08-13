package com.example.kurt.ugood.calendar.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.kurt.ugood.calendar.Fragments.DayFragment;
import com.example.kurt.ugood.calendar.Fragments.MonthFragment;
import com.example.kurt.ugood.calendar.Fragments.WeekFragment;

public class CalendarPagerAdapter extends FragmentStatePagerAdapter {

    private int noOfTabs;

    public CalendarPagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.noOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                return new DayFragment();
            case 1:
                return new WeekFragment();
            case 2:
                return new MonthFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}