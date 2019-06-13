package com.example.kurt.ugood.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.kurt.ugood.login.Fragments.LoginFragment;
import com.example.kurt.ugood.login.Fragments.SignupFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int noOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs)
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
                return new SignupFragment();
            case 1:
                return new LoginFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}