package com.youngsoft.cookconverter.ui.baking;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.textfield.TextInputEditText;
import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.ui.util.MeasuresSpinnerAdapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

public class FragmentRectangularCake extends Fragment {

    private final ViewModelPanSize viewModelPanSize;
    private AutoCompleteTextView actvFRCUnits;
    private TextInputEditText etDimensionWidth;
    private TextInputEditText etDimensionLength;
    private final boolean isInput;
    private MeasuresSpinnerAdapter measuresSpinnerAdapter;
    private Context context;

    FragmentRectangularCake(ViewModelPanSize viewModel, boolean isInput) {
        viewModelPanSize = viewModel;
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

    /**
     * observe changes to livedata for updating views
     */
    private void setObservers() {

        //observe changes to the list of conversion factors
        //update adapter when a change is observed
        viewModelPanSize.getConversionFactorsRecordLiveData().observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
                ConversionFactorsRecord[] outputArray = new ConversionFactorsRecord[conversionFactorsRecords.size()];
                conversionFactorsRecords.toArray(outputArray);
                measuresSpinnerAdapter = new MeasuresSpinnerAdapter(context, outputArray);
                actvFRCUnits.setAdapter(measuresSpinnerAdapter);
            }
        });

        viewModelPanSize.getRectangularConversionFactor().observe(getViewLifecycleOwner(), new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                if (conversionFactorsRecord != null ) {
                    actvFRCUnits.setText(conversionFactorsRecord.getName());
                }
            }
        });

        //observe dimension 1, update the text view, then remove the observer
        final LiveData<Double> rectangularPanDimension1 = viewModelPanSize.getRectangularPanDimension1();
        rectangularPanDimension1.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                rectangularPanDimension1.removeObserver(this);
                etDimensionWidth.setText(aDouble.toString());
            }
        });

        //observe dimension 2, update the text view, then remove the observer
        final LiveData<Double> rectangularPanDimension2 = viewModelPanSize.getRectangularPanDimension2();
        rectangularPanDimension2.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                rectangularPanDimension2.removeObserver(this);
                etDimensionLength.setText(aDouble.toString());
            }
        });

        viewModelPanSize.getRectangularConversionFactor().observe(getViewLifecycleOwner(), new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                actvFRCUnits.setText(conversionFactorsRecord.getName());
            }
        });

    }

    /**
     * set view listeners for edit texts
     */
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
                    double temp;
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
                    double temp;
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
        actvFRCUnits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConversionFactorsRecord selectedItem = measuresSpinnerAdapter.getItem(position);
                viewModelPanSize.setRectangularConversionFactor(selectedItem);
            }
        });
    }

    /**
     * save the width to input or output pan variable in viewmodel
     * @param v Double input value
     */
    private void saveDimension1EditTextValue(Double v) {
        viewModelPanSize.setRectangularPanDimension1(v);
    }

    /**
     * save the length to input or output pan variable in viewmodel
     * @param v Double input value
     */
    private void saveDimension2EditTextValue(Double v) {
        viewModelPanSize.setRectangularPanDimension2(v);
    }

    /**
     * map views to object variables
     * @param root View input that contains the views for mapping
     */
    private void mapViews(View root) {
        actvFRCUnits = root.findViewById(R.id.actv_frc_input_units);
        etDimensionWidth = root.findViewById(R.id.tiet_frc_width_input);
        etDimensionLength = root.findViewById(R.id.tiet_frc_length_input);
    }

}
