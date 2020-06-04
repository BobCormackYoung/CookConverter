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

public class FragmentCircularCake extends Fragment {

    private ViewModelBaking viewModelBaking;
    private Spinner spFCCUnits;
    private TextInputEditText etDimension;
    private boolean isInput;

    FragmentCircularCake(ViewModelBaking viewModel, boolean isInput) {
        viewModelBaking = viewModel;
        this.isInput = isInput;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_circular_cake, container, false);
        mapViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    private void setListeners() {
        etDimension.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etDimension.getText().toString().isEmpty()) {
                    saveEditTextValue(0.0);
                } else {
                    Double temp = null;
                    //try to catch error associated with leading decimal
                    try {
                        temp = DecimalFormat.getNumberInstance().parse(etDimension.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        //Can't handle leading decimal
                        e.printStackTrace();
                        temp = 0.0;
                    }
                    saveEditTextValue(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void saveEditTextValue(double v) {
        if (isInput) {
            viewModelBaking.setInputCircularPanDimension(v);
        } else {
            viewModelBaking.setOutputCircularPanDimension(v);
        }

    }

    private void mapViews(View root) {
        spFCCUnits = root.findViewById(R.id.sp_fcc_input_units);
        etDimension = root.findViewById(R.id.tiet_fcc_od_input);
    }
}
