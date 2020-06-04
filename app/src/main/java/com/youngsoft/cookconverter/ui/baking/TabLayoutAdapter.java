package com.youngsoft.cookconverter.ui.baking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModel;

public class TabLayoutAdapter extends FragmentPagerAdapter {

    //3 tabs, 3 fragments
    private static final int NUM_ITEMS = 3;
    private ViewModelBaking viewModelBaking; //viewmodel for the baking fragment
    private boolean isInput; //is this an input tabLayout or output

    public TabLayoutAdapter(@NonNull FragmentManager fm, ViewModelBaking viewModel, boolean isInput) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewModelBaking = viewModel;
        this.isInput = isInput;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new FragmentRectangularCake(viewModelBaking, isInput);
        } else if (position == 1) {
            return new FragmentCircularCake(viewModelBaking, isInput);
        } else if (position == 2) {
            return new FragmentBundtCake(viewModelBaking, isInput);
        } else {
            return new FragmentRectangularCake(viewModelBaking, isInput);
        }
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
