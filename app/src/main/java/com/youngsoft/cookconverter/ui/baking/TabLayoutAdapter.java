package com.youngsoft.cookconverter.ui.baking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabLayoutAdapter extends FragmentPagerAdapter {

    //3 tabs, 3 fragments
    private static final int NUM_ITEMS = 3;
    //private final ViewModelBaking viewModelBaking; //viewmodel for the baking fragment
    private final ViewModelPanSize viewModelPanSize; //viewmodel for the baking fragment
    private final boolean isInput; //is this an input tabLayout or output

    public TabLayoutAdapter(@NonNull FragmentManager fm, ViewModelPanSize viewModel, boolean isInput) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewModelPanSize = viewModel;
        this.isInput = isInput;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new FragmentRectangularCake(viewModelPanSize, isInput);
        } else if (position == 1) {
            return new FragmentCircularCake(viewModelPanSize, isInput);
        } else if (position == 2) {
            return new FragmentBundtCake(viewModelPanSize, isInput);
        } else {
            return new FragmentRectangularCake(viewModelPanSize, isInput);
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
