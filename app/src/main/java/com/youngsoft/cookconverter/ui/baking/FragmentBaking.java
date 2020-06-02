package com.youngsoft.cookconverter.ui.baking;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.youngsoft.cookconverter.R;

import java.text.DecimalFormat;
import java.text.ParseException;

public class FragmentBaking extends Fragment {

    private ViewModelBaking viewModelBaking;
    private ViewPager viewPagerTabLayoutInput;
    private TabLayout tabLayoutInput;
    private ViewPager viewPagerTabLayoutOutput;
    private TabLayout tabLayoutOutput;
    private TextInputEditText etInputValue;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModelBaking = ViewModelProviders.of(this).get(ViewModelBaking.class);
        View root = inflater.inflate(R.layout.fragment_baking, container, false);
        mapViews(root);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
        setObservers();
        //create adapter for the tab layout
        TabLayoutAdapter adapterTabLayoutInput = new TabLayoutAdapter(getChildFragmentManager(), viewModelBaking);
        TabLayoutAdapter adapterTabLayoutOutput = new TabLayoutAdapter(getChildFragmentManager(), viewModelBaking);

        viewPagerTabLayoutInput.setAdapter(adapterTabLayoutInput);
        viewPagerTabLayoutOutput.setAdapter(adapterTabLayoutOutput);

        tabLayoutInput.setupWithViewPager(viewPagerTabLayoutInput);
        tabLayoutOutput.setupWithViewPager(viewPagerTabLayoutOutput);

    }

    /**
     * set view listeners
     */
    private void setListeners() {

        //set listener for the input pan type
        viewPagerTabLayoutInput.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewModelBaking.setPanTypeInputMutable(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //set listener for the output pan type
        viewPagerTabLayoutOutput.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewModelBaking.setPanTypeOutputMutable(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //set listener for the input edit text
        etInputValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etInputValue.getText().toString().isEmpty()) {
                    viewModelBaking.setInputValueMutable(0.0);
                } else {
                    Double temp = null;
                    //try to catch error associated with leading decimal
                    try {
                        temp = DecimalFormat.getNumberInstance().parse(etInputValue.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        //Can't handle leading decimal
                        e.printStackTrace();
                    }
                    viewModelBaking.setInputValueMutable(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * set livedata observers
     */
    private void setObservers() {
    }


    /**
     * map views from the root fragment view
     * @param root input view used for mapping views
     */
    private void mapViews(View root) {
        viewPagerTabLayoutInput = root.findViewById(R.id.vp_baking_tablayout_input);
        tabLayoutInput = root.findViewById(R.id.tl_baking_input);
        viewPagerTabLayoutOutput = root.findViewById(R.id.vp_baking_tablayout_output);
        tabLayoutOutput = root.findViewById(R.id.tl_baking_output);
        etInputValue = root.findViewById(R.id.tiet_fb_input_value);
    }
}
