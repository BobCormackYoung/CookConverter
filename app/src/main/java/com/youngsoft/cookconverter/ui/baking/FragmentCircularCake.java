package com.youngsoft.cookconverter.ui.baking;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.google.android.material.textfield.TextInputEditText;
import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.ui.util.MeasuresSpinnerAdapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

public class FragmentCircularCake extends Fragment {

    private ViewModelBaking viewModelBaking;
    private Spinner spFCCUnits;
    private TextInputEditText etDimension;
    private boolean isInput;
    private MeasuresSpinnerAdapter measuresSpinnerAdapter;
    private Context context;

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
        context = getActivity();
        setListeners();
        setObservers();
    }

    //observe changes to livedata for updating views
    private void setObservers() {

        //observe changes to the list of conversion factors
        //update adapter when a change is observed
        viewModelBaking.getConversionFactorsRecordLiveData().observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
                ConversionFactorsRecord[] outputArray = new ConversionFactorsRecord[conversionFactorsRecords.size()];
                conversionFactorsRecords.toArray(outputArray);
                measuresSpinnerAdapter = new MeasuresSpinnerAdapter(context, outputArray);
                spFCCUnits.setAdapter(measuresSpinnerAdapter);
            }
        });
    }

    //set listeners for views
    private void setListeners() {
        //set listener for the cake pan dimension
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

        //listen for changes to the measurement spinner value
        spFCCUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ConversionFactorsRecord selectedItem = measuresSpinnerAdapter.getItem(position);
                if (isInput) {
                    viewModelBaking.setInputCircularConversionFactor(selectedItem);
                } else {
                    viewModelBaking.setOutputCircularConversionFactor(selectedItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //save value for the pan diameter
    private void saveEditTextValue(double v) {
        if (isInput) {
            viewModelBaking.setInputCircularPanDimension(v);
        } else {
            viewModelBaking.setOutputCircularPanDimension(v);
        }

    }

    //map views from the fragment
    private void mapViews(View root) {
        spFCCUnits = root.findViewById(R.id.sp_fcc_input_units);
        etDimension = root.findViewById(R.id.tiet_fcc_od_input);
    }
}
