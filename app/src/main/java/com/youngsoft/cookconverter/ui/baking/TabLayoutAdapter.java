package com.youngsoft.cookconverter.ui.baking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabLayoutAdapter extends FragmentPagerAdapter {

    //3 tabs, 3 fragments
    static final int NUM_ITEMS = 3;

    public TabLayoutAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new FragmentRectangularCake();
        } else if (position == 1) {
            return new FragmentCircularCake();
        } else if (position == 2) {
            return new FragmentBundtCake();
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Rectangular";
        } else if (position == 1) {
            return "Circular";
        } else if (position == 2) {
            return "Bundt";
        }
        return super.getPageTitle(position);
    }
}
