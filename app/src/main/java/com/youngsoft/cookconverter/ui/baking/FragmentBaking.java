package com.youngsoft.cookconverter.ui.baking;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.ui.save.BottomSheetSaveMeasurement;

import java.text.DecimalFormat;
import java.text.ParseException;

public class FragmentBaking extends Fragment {

    private ViewModelBaking viewModelBaking;
    private TextInputEditText etInputValue;
    private TextInputEditText etOutputValue;
    private Button btSaveBaking;
    private BottomSheetSaveMeasurement bottomSheetSaveMeasurement;

    private TextView tvInputPanType;
    private TextView tvOutputPanType;
    private TextView tvInputDim1Name;
    private TextView tvOutputDim1Name;
    private TextView tvInputDim2Name;
    private TextView tvOutputDim2Name;
    private TextView tvInputDim1Value;
    private TextView tvOutputDim1Value;
    private TextView tvInputDim2Value;
    private TextView tvOutputDim2Value;
    private TextView tvInputDim1Unit;
    private TextView tvOutputDim1Unit;
    private TextView tvInputDim2Unit;
    private TextView tvOutputDim2Unit;
    private ImageView ivInputPanIcon;
    private ImageView ivOutputPanIcon;
    private Button btInfoButton;
    private BottomSheetPanSize bottomSheetPanSize;
    private Button btLaunchInputBottomSheet;
    private Button btLaunchOutputBottomSheet;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModelBaking = new ViewModelProvider(this).get(ViewModelBaking.class);
        View root = inflater.inflate(R.layout.fragment_baking, container, false);
        mapViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
        setObservers();
        etInputValue.setText("0.0");
    }

    /**
     * set view listeners
     */
    private void setListeners() {

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
                    double temp;
                    //try to catch error associated with leading decimal
                    try {
                        temp = DecimalFormat.getNumberInstance().parse(etInputValue.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        //Can't handle leading decimal
                        e.printStackTrace();
                        temp = 0.0;
                    }
                    viewModelBaking.setInputValueMutable(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //set click listener for info button
        btInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog)
                        .setMessage(getResources().getString(R.string.info_fragment_baking))
                        .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Do nothing
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
                Button btNegativeDialog = dialog.getButton(Dialog.BUTTON_NEGATIVE);
                btNegativeDialog.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        //set listener for save button
        btSaveBaking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetSaveMeasurement = new BottomSheetSaveMeasurement( 2);
                bottomSheetSaveMeasurement.show(getChildFragmentManager(), "saveDataBottomSheet");
            }
        });

        //DEBUG
        btLaunchInputBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetPanSize = new BottomSheetPanSize(1);
                bottomSheetPanSize.show(getChildFragmentManager(),"panSizeBottomFragment");
            }
        });
        btLaunchOutputBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetPanSize = new BottomSheetPanSize(2);
                bottomSheetPanSize.show(getChildFragmentManager(),"panSizeBottomFragment");
            }
        });
    }

    /**
     * set livedata observers
     */
    private void setObservers() {
        //observe output value
        viewModelBaking.getMediatorOutput().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                //check if value is too small to be worth displaying
                if (aDouble*1000 < 1) {
                    etOutputValue.setText("0.0");
                } else {
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");
                    etOutputValue.setText(decimalFormat.format(aDouble));
                }
            }
        });

        //observe input pan type
        viewModelBaking.getPanTypeInputMutable().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0) {
                    ivInputPanIcon.setBackgroundResource(R.drawable.ic_rectangular_pan_96px);
                    tvInputPanType.setText(R.string.pan_type_rectangular);
                    tvInputDim1Name.setText(R.string.dimension_rectangular_1);
                    tvInputDim2Name.setVisibility(View.VISIBLE);
                    tvInputDim2Unit.setVisibility(View.VISIBLE);
                    tvInputDim2Value.setVisibility(View.VISIBLE);
                    tvInputDim2Name.setText(R.string.dimensions_rectangular_2);
                } else if (integer == 1) {
                    ivInputPanIcon.setBackgroundResource(R.drawable.ic_circular_pan_96px);
                    tvInputPanType.setText(R.string.pan_type_circular);
                    tvInputDim1Name.setText(R.string.dimensions_circular);
                    tvInputDim2Name.setVisibility(View.GONE);
                    tvInputDim2Unit.setVisibility(View.GONE);
                    tvInputDim2Value.setVisibility(View.GONE);
                } else if (integer == 2) {
                    ivInputPanIcon.setBackgroundResource(R.drawable.ic_bundt_pan_96px);
                    tvInputPanType.setText(R.string.pan_type_bundt);
                    tvInputDim1Name.setText(R.string.dimensions_bundt_1);
                    tvInputDim2Name.setVisibility(View.VISIBLE);
                    tvInputDim2Unit.setVisibility(View.VISIBLE);
                    tvInputDim2Value.setVisibility(View.VISIBLE);
                    tvInputDim2Name.setText(R.string.dimensions_bundt_2);
                } else {
                    ivInputPanIcon.setBackgroundResource(R.drawable.ic_rectangular_pan_96px);
                    tvInputPanType.setText(R.string.pan_type_unknown);
                    tvInputDim1Name.setText(R.string.dimensions_unknown);
                    tvInputDim2Name.setVisibility(View.VISIBLE);
                    tvInputDim2Unit.setVisibility(View.VISIBLE);
                    tvInputDim2Value.setVisibility(View.VISIBLE);
                    tvInputDim2Name.setText(R.string.dimensions_unknown);
                }
            }
        });

        //observe output pan type
        viewModelBaking.getPanTypeOutputMutable().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0) {
                    ivOutputPanIcon.setBackgroundResource(R.drawable.ic_rectangular_pan_96px);
                    tvOutputPanType.setText(R.string.pan_type_rectangular);
                    tvOutputDim1Name.setText(R.string.dimension_rectangular_1);
                    tvOutputDim2Name.setVisibility(View.VISIBLE);
                    tvOutputDim2Unit.setVisibility(View.VISIBLE);
                    tvOutputDim2Value.setVisibility(View.VISIBLE);
                    tvOutputDim2Name.setText(R.string.dimensions_rectangular_2);
                } else if (integer == 1) {
                    ivOutputPanIcon.setBackgroundResource(R.drawable.ic_circular_pan_96px);
                    tvOutputPanType.setText(R.string.pan_type_circular);
                    tvOutputDim1Name.setText(R.string.dimensions_circular);
                    tvOutputDim2Name.setVisibility(View.GONE);
                    tvOutputDim2Unit.setVisibility(View.GONE);
                    tvOutputDim2Value.setVisibility(View.GONE);
                } else if (integer == 2) {
                    ivOutputPanIcon.setBackgroundResource(R.drawable.ic_bundt_pan_96px);
                    tvOutputPanType.setText(R.string.pan_type_bundt);
                    tvOutputDim1Name.setText(R.string.dimensions_bundt_1);
                    tvOutputDim2Name.setVisibility(View.VISIBLE);
                    tvOutputDim2Unit.setVisibility(View.VISIBLE);
                    tvOutputDim2Value.setVisibility(View.VISIBLE);
                    tvOutputDim2Name.setText(R.string.dimensions_bundt_2);
                } else {
                    ivOutputPanIcon.setBackgroundResource(R.drawable.ic_rectangular_pan_96px);
                    tvOutputPanType.setText(R.string.pan_type_unknown);
                    tvOutputDim1Name.setText(R.string.dimensions_unknown);
                    tvOutputDim2Name.setVisibility(View.VISIBLE);
                    tvOutputDim2Unit.setVisibility(View.VISIBLE);
                    tvOutputDim2Value.setVisibility(View.VISIBLE);
                    tvOutputDim2Name.setText(R.string.dimensions_unknown);
                }
            }
        });

        //input conversion factor
        viewModelBaking.getInputConversionFactor().observe(getViewLifecycleOwner(), new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                tvInputDim1Unit.setText(conversionFactorsRecord.getName());
                tvInputDim2Unit.setText(conversionFactorsRecord.getName());
            }
        });

        //output conversion factor
        viewModelBaking.getOutputConversionFactor().observe(getViewLifecycleOwner(), new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                tvOutputDim1Unit.setText(conversionFactorsRecord.getName());
                tvOutputDim2Unit.setText(conversionFactorsRecord.getName());
            }
        });

        //input dimension 1
        viewModelBaking.getInputPanDimension1().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                tvInputDim1Value.setText(aDouble.toString());
            }
        });

        //input dimension 2
        viewModelBaking.getInputPanDimension2().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                tvInputDim2Value.setText(aDouble.toString());
            }
        });

        //output dimension 1
        viewModelBaking.getOutputPanDimension1().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                tvOutputDim1Value.setText(aDouble.toString());
            }
        });

        //output dimension 2
        viewModelBaking.getOutputPanDimension2().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                tvOutputDim2Value.setText(aDouble.toString());
            }
        });

    }

    /**
     * map views from the root fragment view
     * @param root input view used for mapping views
     */
    private void mapViews(View root) {
        etInputValue = root.findViewById(R.id.tiet_fb_input_value);
        etOutputValue = root.findViewById(R.id.tiet_fb_output_value);
        btSaveBaking = root.findViewById(R.id.bt_save_baking);

        tvInputPanType = root.findViewById(R.id.tv_bakingfragment_input_pantype);
        tvOutputPanType = root.findViewById(R.id.tv_bakingfragment_output_pantype);
        tvInputDim1Name = root.findViewById(R.id.tv_bakingfragment_input_dim1_name);
        tvOutputDim1Name = root.findViewById(R.id.tv_bakingfragment_output_dim1_name);
        tvInputDim2Name = root.findViewById(R.id.tv_bakingfragment_input_dim2_name);
        tvOutputDim2Name = root.findViewById(R.id.tv_bakingfragment_output_dim2_name);
        tvInputDim1Value = root.findViewById(R.id.tv_bakingfragment_input_dim1_value);
        tvOutputDim1Value = root.findViewById(R.id.tv_bakingfragment_output_dim1_value);
        tvInputDim2Value = root.findViewById(R.id.tv_bakingfragment_input_dim2_value);
        tvOutputDim2Value = root.findViewById(R.id.tv_bakingfragment_output_dim2_value);
        tvInputDim1Unit = root.findViewById(R.id.tv_bakingfragment_input_dim1_units);
        tvOutputDim1Unit = root.findViewById(R.id.tv_bakingfragment_output_dim1_units);
        tvInputDim2Unit = root.findViewById(R.id.tv_bakingfragment_input_dim2_units);
        tvOutputDim2Unit = root.findViewById(R.id.tv_bakingfragment_output_dim2_units);
        ivInputPanIcon = root.findViewById(R.id.iv_input_pan_icon);
        ivOutputPanIcon = root.findViewById(R.id.iv_output_pan_icon);
        btInfoButton = root.findViewById(R.id.bt_info_fragment_baking);
        btLaunchInputBottomSheet = root.findViewById(R.id.bt_input_pan_edit);
        btLaunchOutputBottomSheet = root.findViewById(R.id.bt_output_pan_edit);
    }
}
