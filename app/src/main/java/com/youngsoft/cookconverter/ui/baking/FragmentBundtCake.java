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

import com.google.android.material.textfield.TextInputEditText;
import com.youngsoft.cookconverter.R;

import java.text.DecimalFormat;
import java.text.ParseException;

public class FragmentBundtCake extends Fragment {

    private ViewModelBaking viewModelBaking;
    private Spinner spFBCUnits;
    private TextInputEditText etOuterDiameter;
    private TextInputEditText etInnerDiameter;
    private boolean isInput;

    public FragmentBundtCake(ViewModelBaking viewModel, boolean isInput) {
        viewModelBaking = viewModel;
        this.isInput = isInput;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bundt_cake, container, false);
        mapViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    private void setListeners() {
        etOuterDiameter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etOuterDiameter.getText().toString().isEmpty()) {
                    saveOuterDiameterEditTextValue(0.0);
                } else {
                    Double temp = null;
                    //try to catch error associated with leading decimal
                    try {
                        temp = DecimalFormat.getNumberInstance().parse(etOuterDiameter.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        //Can't handle leading decimal
                        e.printStackTrace();
                        temp = 0.0;
                    }
                    saveOuterDiameterEditTextValue(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etInnerDiameter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etInnerDiameter.getText().toString().isEmpty()) {
                    saveInnerDiameterEditTextValue(0.0);
                } else {
                    Double temp = null;
                    //try to catch error associated with leading decimal
                    try {
                        temp = DecimalFormat.getNumberInstance().parse(etInnerDiameter.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        //Can't handle leading decimal
                        e.printStackTrace();
                        temp = 0.0;
                    }
                    saveInnerDiameterEditTextValue(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void saveOuterDiameterEditTextValue(Double v) {
        if (isInput) {
            viewModelBaking.setInputBundtPanDimension1(v);
        } else {
            viewModelBaking.setOutputBundtPanDimension1(v);
        }
    }

    private void saveInnerDiameterEditTextValue(Double v) {
        if (isInput) {
            viewModelBaking.setInputBundtPanDimension2(v);
        } else {
            viewModelBaking.setOutputBundtPanDimension2(v);
        }
    }

    private void mapViews(View root) {
        etOuterDiameter = root.findViewById(R.id.tiet_fbc_od_input);
        etInnerDiameter = root.findViewById(R.id.tiet_fbc_id_input);
        spFBCUnits = root.findViewById(R.id.sp_fbc_input_units);
    }


}
