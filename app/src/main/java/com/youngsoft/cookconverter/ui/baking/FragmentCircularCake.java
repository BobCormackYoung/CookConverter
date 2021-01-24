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

public class FragmentCircularCake extends Fragment {

    private final ViewModelPanSize viewModelPanSize;
    private AutoCompleteTextView actvFCCUnits;
    private TextInputEditText etDimension;
    private final boolean isInput;
    private MeasuresSpinnerAdapter measuresSpinnerAdapter;
    private Context context;

    FragmentCircularCake(ViewModelPanSize viewModel, boolean isInput) {
        viewModelPanSize = viewModel;
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
        viewModelPanSize.getConversionFactorsRecordLiveData().observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
                ConversionFactorsRecord[] outputArray = new ConversionFactorsRecord[conversionFactorsRecords.size()];
                conversionFactorsRecords.toArray(outputArray);
                measuresSpinnerAdapter = new MeasuresSpinnerAdapter(context, outputArray);
                actvFCCUnits.setAdapter(measuresSpinnerAdapter);
            }
        });

        viewModelPanSize.getCircularConversionFactor().observe(getViewLifecycleOwner(), new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                if (conversionFactorsRecord != null) {
                    actvFCCUnits.setText(conversionFactorsRecord.getName());
                }
            }
        });

        //observe dimension 1, update the text view, then remove the observer
        final LiveData<Double> circularPanDimension = viewModelPanSize.getCircularPanDimension();
        circularPanDimension.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                circularPanDimension.removeObserver(this);
                etDimension.setText(aDouble.toString());
            }
        });

        viewModelPanSize.getCircularConversionFactor().observe(getViewLifecycleOwner(), new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                actvFCCUnits.setText(conversionFactorsRecord.getName());
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
                    double temp;
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
        actvFCCUnits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConversionFactorsRecord selectedItem = measuresSpinnerAdapter.getItem(position);
                viewModelPanSize.setCircularConversionFactor(selectedItem);
            }
        });
    }

    //save value for the pan diameter
    private void saveEditTextValue(double v) {
        viewModelPanSize.setCircularPanDimension(v);
    }

    //map views from the fragment
    private void mapViews(View root) {
        actvFCCUnits = root.findViewById(R.id.actv_fcc_input_units);
        etDimension = root.findViewById(R.id.tiet_fcc_od_input);
    }
}
