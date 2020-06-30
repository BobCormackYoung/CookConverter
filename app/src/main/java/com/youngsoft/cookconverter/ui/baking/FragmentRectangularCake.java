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

public class FragmentRectangularCake extends Fragment {

    private final ViewModelBaking viewModelBaking;
    private Spinner spFRCUnits;
    private TextInputEditText etDimensionWidth;
    private TextInputEditText etDimensionLength;
    private final boolean isInput;
    private MeasuresSpinnerAdapter measuresSpinnerAdapter;
    private Context context;

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
                spFRCUnits.setAdapter(measuresSpinnerAdapter);
            }
        });
    }

    //set view listeners for edit texts
    private void setListeners() {
        //listener for width
        etDimensionWidth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etDimensionWidth.getText().toString().isEmpty()) {
                    saveDimension1EditTextValue(0.0);
                } else {
                    Double temp;
                    //try to catch error associated with leading decimal
                    try {
                        temp = DecimalFormat.getNumberInstance().parse(etDimensionWidth.getText().toString()).doubleValue();
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

        //listener for length
        etDimensionLength.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etDimensionLength.getText().toString().isEmpty()) {
                    saveDimension2EditTextValue(0.0);
                } else {
                    Double temp;
                    //try to catch error associated with leading decimal
                    try {
                        temp = DecimalFormat.getNumberInstance().parse(etDimensionLength.getText().toString()).doubleValue();
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

        //listen for changes to the measurement spinner value
        spFRCUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ConversionFactorsRecord selectedItem = measuresSpinnerAdapter.getItem(position);
                if (isInput) {
                    viewModelBaking.setInputRectangularConversionFactor(selectedItem);
                } else {
                    viewModelBaking.setOutputRectangularConversionFactor(selectedItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //save the width to input or output pan variable in viewmodel
    private void saveDimension1EditTextValue(Double v) {
        if (isInput) {
            viewModelBaking.setInputRectangularPanDimension1(v);
        } else {
            viewModelBaking.setOutputRectangularPanDimension1(v);
        }
    }

    //save the length to input or output pan variable in viewmodel
    private void saveDimension2EditTextValue(Double v) {
        if (isInput) {
            viewModelBaking.setInputRectangularPanDimension2(v);
        } else {
            viewModelBaking.setOutputRectangularPanDimension2(v);
        }
    }

    //map views to object variables
    private void mapViews(View root) {
        spFRCUnits = root.findViewById(R.id.sp_frc_input_units);
        etDimensionWidth = root.findViewById(R.id.tiet_frc_width_input);
        etDimensionLength = root.findViewById(R.id.tiet_frc_length_input);
    }

}
