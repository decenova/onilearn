package com.onilearnapp.onilearnapp.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.onilearnapp.onilearnapp.Fragment.AnalyzeFragment;
import com.onilearnapp.onilearnapp.Fragment.CategoryFragment;
import com.onilearnapp.onilearnapp.Fragment.TimeTableFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CategoryFragment();
            case 1:
                return new TimeTableFragment();
            case 2:
                return new AnalyzeFragment();
            default:
                return null;
        }
//        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SubjectDao";
            case 1:
                return "Timetable";
            case 2:
                return "Analyze";
            default:
                return null;
        }
//        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
