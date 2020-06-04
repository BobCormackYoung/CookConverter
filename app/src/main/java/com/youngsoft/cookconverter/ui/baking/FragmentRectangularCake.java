package com.youngsoft.cookconverter.ui.baking;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.youngsoft.cookconverter.R;

import java.text.DecimalFormat;
import java.text.ParseException;

public class FragmentRectangularCake extends Fragment {

    private ViewModelBaking viewModelBaking;
    private Spinner spFRCUnits;
    private TextInputEditText etDimension1;
    private TextInputEditText etDimension2;
    private boolean isInput;

    FragmentRectangularCake(ViewModelBaking viewModel, boolean isInput) {
        viewModelBaking = viewModel;
        this.isInput = isInput;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rectangular_cake, container, false);
        mapViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    private void setListeners() {
        etDimension1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etDimension1.getText().toString().isEmpty()) {
                    saveDimension1EditTextValue(0.0);
                } else {
                    Double temp = null;
                    //try to catch error associated with leading decimal
                    try {
                        temp = DecimalFormat.getNumberInstance().parse(etDimension1.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        //Can't handle leading decimal
                        e.printStackTrace();
                        temp = 0.0;
                    }
                    saveDimension1EditTextValue(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etDimension2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etDimension2.getText().toString().isEmpty()) {
                    saveDimension2EditTextValue(0.0);
                } else {
                    Double temp = null;
                    //try to catch error associated with leading decimal
                    try {
                        temp = DecimalFormat.getNumberInstance().parse(etDimension2.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        //Can't handle leading decimal
                        e.printStackTrace();
                        temp = 0.0;
                    }
                    saveDimension2EditTextValue(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void saveDimension1EditTextValue(Double v) {
        if (isInput) {
            viewModelBaking.setInputRectangularPanDimension1(v);
        } else {
            viewModelBaking.setOutputRectangularPanDimension1(v);
        }
    }

    private void saveDimension2EditTextValue(Double v) {
        if (isInput) {
            viewModelBaking.setInputRectangularPanDimension2(v);
        } else {
            viewModelBaking.setOutputRectangularPanDimension2(v);
        }
    }

    private void mapViews(View root) {
        spFRCUnits = root.findViewById(R.id.sp_frc_input_units);
        etDimension1 = root.findViewById(R.id.tiet_frc_width_input);
        etDimension2 = root.findViewById(R.id.tiet_frc_length_input);
    }

}
