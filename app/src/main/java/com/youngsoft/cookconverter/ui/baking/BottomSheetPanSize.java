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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.ui.util.WrapContentHeightViewPager;

public class BottomSheetPanSize extends BottomSheetDialogFragment {

    private ViewModelBaking viewModelBaking;
    private ViewModelPanSize viewModelPanSize;
    private final int launchCase; //1 = input pan size, 2 = output pan size

    private WrapContentHeightViewPager viewPagerTabLayout;
    private TabLayout tabLayout;

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

        TabLayoutAdapter adapterTabLayout;
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


        setInputObservers();

    }

    private void setInputObservers() {
        final Double[] dimension1 = {0.0};
        final Double[] dimension2 = {0.0};
        final ConversionFactorsRecord[] conversionFactors = new ConversionFactorsRecord[1];

        if (launchCase == 1) {
            //input value is require
            final LiveData<Double> inputDimension1 = viewModelBaking.getInputPanDimension1();
            inputDimension1.observe(getViewLifecycleOwner(), new Observer<Double>() {
                @Override
                public void onChanged(Double aDouble) {
                    inputDimension1.removeObserver(this);
                    dimension1[0] = aDouble;
                }
            });
            final LiveData<Double> inputDimension2 = viewModelBaking.getInputPanDimension2();
            inputDimension2.observe(getViewLifecycleOwner(), new Observer<Double>() {
                @Override
                public void onChanged(Double aDouble) {
                    inputDimension2.removeObserver(this);
                    dimension2[0] = aDouble;
                }
            });
            final LiveData<ConversionFactorsRecord> inputConversionFactor = viewModelBaking.getInputConversionFactor();
            inputConversionFactor.observe(getViewLifecycleOwner(), new Observer<ConversionFactorsRecord>() {
                @Override
                public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                    inputConversionFactor.removeObserver(this);
                    conversionFactors[0] = conversionFactorsRecord;
                }
            });
            final LiveData<Integer> inputPanType = viewModelBaking.getPanTypeInputMutable();
            inputPanType.observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    inputPanType.removeObserver(this);
                    tabLayout.getTabAt(integer).select();
                    updateLiveData(integer, dimension1, dimension2, conversionFactors);
                }
            });
        } else if (launchCase == 2) {
            //output value required
            final LiveData<Double> outputDimension1 = viewModelBaking.getOutputPanDimension1();
            outputDimension1.observe(getViewLifecycleOwner(), new Observer<Double>() {
                @Override
                public void onChanged(Double aDouble) {
                    outputDimension1.removeObserver(this);
                    dimension1[0] = aDouble;
                }
            });
            final LiveData<Double> outputDimension2 = viewModelBaking.getOutputPanDimension2();
            outputDimension2.observe(getViewLifecycleOwner(), new Observer<Double>() {
                @Override
                public void onChanged(Double aDouble) {
                    outputDimension2.removeObserver(this);
                    dimension2[0] = aDouble;
                }
            });
            final LiveData<ConversionFactorsRecord> outputConversionFactor = viewModelBaking.getOutputConversionFactor();
            outputConversionFactor.observe(getViewLifecycleOwner(), new Observer<ConversionFactorsRecord>() {
                @Override
                public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                    outputConversionFactor.removeObserver(this);
                    conversionFactors[0] = conversionFactorsRecord;
                }
            });
            final LiveData<Integer> outputPanType = viewModelBaking.getPanTypeOutputMutable();
            outputPanType.observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    outputPanType.removeObserver(this);
                    tabLayout.getTabAt(integer).select();
                    updateLiveData(integer, dimension1, dimension2, conversionFactors);
                }
            });
        } else {
            //input value as default
            final LiveData<Double> inputDimension1 = viewModelBaking.getInputPanDimension1();
            inputDimension1.observe(getViewLifecycleOwner(), new Observer<Double>() {
                @Override
                public void onChanged(Double aDouble) {
                    inputDimension1.removeObserver(this);
                    dimension1[0] = aDouble;
                }
            });
            final LiveData<Double> inputDimension2 = viewModelBaking.getInputPanDimension2();
            inputDimension2.observe(getViewLifecycleOwner(), new Observer<Double>() {
                @Override
                public void onChanged(Double aDouble) {
                    inputDimension2.removeObserver(this);
                    dimension2[0] = aDouble;
                }
            });
            final LiveData<ConversionFactorsRecord> inputConversionFactor = viewModelBaking.getInputConversionFactor();
            inputConversionFactor.observe(getViewLifecycleOwner(), new Observer<ConversionFactorsRecord>() {
                @Override
                public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                    inputConversionFactor.removeObserver(this);
                    conversionFactors[0] = conversionFactorsRecord;
                }
            });
            final LiveData<Integer> inputPanType = viewModelBaking.getPanTypeInputMutable();
            inputPanType.observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    inputPanType.removeObserver(this);
                     tabLayout.getTabAt(integer).select();
                    updateLiveData(integer, dimension1, dimension2, conversionFactors);
                }
            });
        }
    }

    private void updateLiveData(Integer inputCase, Double[] dimension1, Double[] dimension2, ConversionFactorsRecord[] conversionFactors) {

        if (inputCase == 0) {
            viewModelPanSize.setRectangularPanDimension1(dimension1[0]);
            viewModelPanSize.setRectangularPanDimension2(dimension2[0]);
            viewModelPanSize.setRectangularConversionFactor(conversionFactors[0]);
        } else if (inputCase == 1) {
            viewModelPanSize.setCircularPanDimension(dimension1[0]);
            viewModelPanSize.setCircularConversionFactor(conversionFactors[0]);
        } else if (inputCase == 2) {
            viewModelPanSize.setBundtPanDimension1(dimension1[0]);
            viewModelPanSize.setBundtPanDimension2(dimension2[0]);
            viewModelPanSize.setBundtConversionFactor(conversionFactors[0]);
        } else {
            viewModelPanSize.setRectangularPanDimension1(dimension1[0]);
            viewModelPanSize.setRectangularPanDimension2(dimension2[0]);
            viewModelPanSize.setRectangularConversionFactor(conversionFactors[0]);
        }
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
