package com.youngsoft.cookconverter.ui.servings;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.youngsoft.cookconverter.R;

import java.text.DecimalFormat;
import java.text.ParseException;

public class FragmentServings extends Fragment {

    private ViewModelServings viewModelServings;
    private TextInputEditText etInputValue;
    private TextInputEditText etOutputValue;
    private NumberPicker npInputServing;
    private NumberPicker npOutputServing;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModelServings = ViewModelProviders.of(this).get(ViewModelServings.class);
        View root = inflater.inflate(R.layout.fragment_servings, container, false);
        mapViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNumberPickers();
        etInputValue.setText("0.0");
        setListeners();
        setObservers();
        etInputValue.setText("0.0");
    }

    private void initNumberPickers() {
        npInputServing.setMinValue(1);
        npInputServing.setMaxValue(100);
        npInputServing.setWrapSelectorWheel(true);
        npOutputServing.setMinValue(1);
        npOutputServing.setMaxValue(100);
        npOutputServing.setWrapSelectorWheel(true);
    }

    /**
     * set livedata observers
     */
    private void setObservers() {

        //observe changes to the output value
        viewModelServings.getMediatorOutput().observe(getViewLifecycleOwner(), new Observer<Double>() {
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
                    viewModelServings.setInputValue(0.0);
                } else {
                    Double temp = null;
                    //try to catch error associated with leading decimal
                    try {
                        temp = DecimalFormat.getNumberInstance().parse(etInputValue.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        //Can't handle leading decimal
                        e.printStackTrace();
                    }
                    viewModelServings.setInputValue(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //set listener for the input serving size
        npInputServing.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                viewModelServings.setInputServingSize(newVal);
            }
        });

        //set listener for the output serving size
        npOutputServing.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                viewModelServings.setOutputServingSize(newVal);
            }
        });
    }


    private void mapViews(View root) {
        etInputValue = root.findViewById(R.id.tiet_fs_input_value);
        etOutputValue = root.findViewById(R.id.tiet_fs_output_value);
        npInputServing = root.findViewById(R.id.np_servings_input);
        npOutputServing = root.findViewById(R.id.np_servings_output);
    }
}
