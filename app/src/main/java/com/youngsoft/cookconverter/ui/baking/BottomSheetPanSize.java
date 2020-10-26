package com.youngsoft.cookconverter.ui.baking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.ui.util.WrapContentHeightViewPager;

public class BottomSheetPanSize extends BottomSheetDialogFragment {

    private ViewModelBaking viewModelBaking;
    private ViewModelPanSize viewModelPanSize;
    private final int launchCase; //1 = input pan size, 2 = output pan size

    private WrapContentHeightViewPager viewPagerTabLayout;
    private TabLayout tabLayout;
    private TabLayoutAdapter adapterTabLayout;

    private TextView tvBottomSheetHeader;

    private Button btSave;
    private Button btCancel;

    /**
     * constructor for the fragment
     * @param launchCase int for the launch case. 1 = input pan, 2 = output pan
     */
    public BottomSheetPanSize (int launchCase) {
        this.launchCase = launchCase;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bottomsheet_pan_size, container, false);

        //set the viewmodel for this bottomsheet fragment
        viewModelPanSize = new ViewModelProvider(this).get(ViewModelPanSize.class);

        //find the viewmodels from the parent fragment
        viewModelBaking = new ViewModelProvider(getParentFragment()).get(ViewModelBaking.class);

        mapViews(root);

        if (launchCase == 1) {
            tvBottomSheetHeader.setText(R.string.title_input_pan_type);
        } else if (launchCase == 2) {
            tvBottomSheetHeader.setText(R.string.title_output_pan_type);
        } else {
            tvBottomSheetHeader.setText(R.string.title_unknown_pan_type);
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //set listeners for the bottom sheet view
        setListeners();

        if (launchCase == 1) {
            adapterTabLayout = new TabLayoutAdapter(getChildFragmentManager(), viewModelPanSize, true);
        } else if (launchCase == 2) {
            adapterTabLayout = new TabLayoutAdapter(getChildFragmentManager(), viewModelPanSize, false);
        } else {
            adapterTabLayout = new TabLayoutAdapter(getChildFragmentManager(), viewModelPanSize, true);
        }

        viewPagerTabLayout.setAdapter(adapterTabLayout);
        viewPagerTabLayout.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(viewPagerTabLayout);

    }

    /**
     * set view listeners
     */
    private void setListeners() {
        //set listener for the pan type
        viewPagerTabLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewModelPanSize.setPanTypeMutable(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //cancel button
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //save button
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (launchCase == 1) {
                    if (viewModelPanSize.saveDataAsInput(viewModelBaking)) {
                        dismiss();
                    }
                } else if (launchCase == 2) {
                    if (viewModelPanSize.saveDataAsOutput(viewModelBaking)) {
                        dismiss();
                    }
                } else {
                    Toast.makeText(getActivity(), "Unknown launchcase", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void mapViews(View root) {

        viewPagerTabLayout = root.findViewById(R.id.vp_baking_tablayout);
        tabLayout = root.findViewById(R.id.tl_baking);
        tvBottomSheetHeader = root.findViewById(R.id.tv_pansize_bottomsheet_header);
        btCancel = root.findViewById(R.id.bt_pansize_cancel);
        btSave = root.findViewById(R.id.bt_pansize_save);

    }

}
