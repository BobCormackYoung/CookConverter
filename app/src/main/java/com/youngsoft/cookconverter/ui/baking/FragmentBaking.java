package com.youngsoft.cookconverter.ui.baking;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.youngsoft.cookconverter.R;

public class FragmentBaking extends Fragment {

    private ViewModelBaking viewModelBaking;
    private ViewPager viewPagerTabLayoutInput;
    private TabLayout tabLayoutInput;
    private ViewPager viewPagerTabLayoutOutput;
    private TabLayout tabLayoutOutput;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModelBaking = ViewModelProviders.of(this).get(ViewModelBaking.class);
        View root = inflater.inflate(R.layout.fragment_baking, container, false);
        mapViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //create adapter for the tab layout
        TabLayoutAdapter adapterTabLayoutInput = new TabLayoutAdapter(getChildFragmentManager());
        TabLayoutAdapter adapterTabLayoutOutput = new TabLayoutAdapter(getChildFragmentManager());

        viewPagerTabLayoutInput.setAdapter(adapterTabLayoutInput);
        viewPagerTabLayoutOutput.setAdapter(adapterTabLayoutOutput);

        tabLayoutInput.setupWithViewPager(viewPagerTabLayoutInput);
        tabLayoutOutput.setupWithViewPager(viewPagerTabLayoutOutput);

    }

    private void mapViews(View root) {
        viewPagerTabLayoutInput = root.findViewById(R.id.vp_baking_tablayout_input);
        tabLayoutInput = root.findViewById(R.id.tl_baking_input);
        viewPagerTabLayoutOutput = root.findViewById(R.id.vp_baking_tablayout_output);
        tabLayoutOutput = root.findViewById(R.id.tl_baking_output);
    }
}
