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
import com.google.android.material.textfield.TextInputLayout;
import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.ui.util.MeasuresSpinnerAdapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

public class FragmentBundtCake extends Fragment {

    private final ViewModelPanSize viewModelPanSize;
    private AutoCompleteTextView actvFBCUnits;
    private TextInputEditText etOuterDiameter;
    private TextInputEditText etInnerDiameter;
    private TextInputLayout tilOuterDiameter;
    private TextInputLayout tilInnerDiameter;
    private final boolean isInput;
    private MeasuresSpinnerAdapter measuresSpinnerAdapter;
    private Context context;

    public FragmentBundtCake(ViewModelPanSize viewModel, boolean isInput) {
        viewModelPanSize = viewModel;
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
        context = getActivity();
        setListeners();
        setObservers();
    }

    //observe changes to livedata for updating views
    private void setObservers() {

        //observe for ID value being larger than the OD value
        viewModelPanSize.getIsErrorIDgtOD().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    tilInnerDiameter.setError("Too large!");
                } else {
                    tilInnerDiameter.setError(null);
                }
            }
        });

        //observe for OD value being smaller than the ID value
        viewModelPanSize.getIsErrorODltID().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    tilOuterDiameter.setError("Too small!");
                } else {
                    tilOuterDiameter.setError(null);
                }
            }
        });

        //observe changes to the list of conversion factors
        //update adapter when a change is observed
        viewModelPanSize.getConversionFactorsRecordLiveData().observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
                ConversionFactorsRecord[] outputArray = new ConversionFactorsRecord[conversionFactorsRecords.size()];
                conversionFactorsRecords.toArray(outputArray);
                measuresSpinnerAdapter = new MeasuresSpinnerAdapter(context, outputArray);
                actvFBCUnits.setAdapter(measuresSpinnerAdapter);
            }
        });

        viewModelPanSize.getBundtConversionFactor().observe(getViewLifecycleOwner(), new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                if (conversionFactorsRecord != null) {
                    actvFBCUnits.setText(conversionFactorsRecord.getName());
                }
            }
        });

        viewModelPanSize.getBundtConversionFactor().observe(getViewLifecycleOwner(), new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                actvFBCUnits.setText(conversionFactorsRecord.getName());
            }
        });


        //observe dimension 1, update the text view, then remove the observer
        final LiveData<Double> bundtPanDimension1 = viewModelPanSize.getBundtPanDimension1();
        bundtPanDimension1.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                bundtPanDimension1.removeObserver(this);
                etOuterDiameter.setText(aDouble.toString());
            }
        });

        //observe dimension 2, update the text view, then remove the observer
        final LiveData<Double> bundtPanDimension2 = viewModelPanSize.getBundtPanDimension2();
        bundtPanDimension2.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                bundtPanDimension2.removeObserver(this);
                etInnerDiameter.setText(aDouble.toString());
            }
        });
    }

    //set listeners for fragment views
    private void setListeners() {
        //set listener for outer diameter
        etOuterDiameter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etOuterDiameter.getText().toString().isEmpty()) {
                    saveOuterDiameterEditTextValue(0.0);
                } else {
                    double temp;
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

        //set listener for the inner diameter
        etInnerDiameter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etInnerDiameter.getText().toString().isEmpty()) {
                    saveInnerDiameterEditTextValue(0.0);
                } else {
                    double temp;
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

        //listen for changes to the measurement spinner value
        actvFBCUnits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConversionFactorsRecord selectedItem = measuresSpinnerAdapter.getItem(position);
                viewModelPanSize.setBundtConversionFactor(selectedItem);
            }
        });
    }

    //save the outer diameter value to the input or output pan variable in viewmodel
    private void saveOuterDiameterEditTextValue(Double v) {
        viewModelPanSize.setBundtPanDimension1(v);
    }

    //save the inner diameter value to the input or output pan variable in viewmodel
    private void saveInnerDiameterEditTextValue(Double v) {
        viewModelPanSize.setBundtPanDimension2(v);
    }

    //map views to object variables
    private void mapViews(View root) {
        etOuterDiameter = root.findViewById(R.id.tiet_fbc_od_input);
        etInnerDiameter = root.findViewById(R.id.tiet_fbc_id_input);
        actvFBCUnits = root.findViewById(R.id.actv_fbc_input_units);
        tilOuterDiameter = root.findViewById(R.id.til_fbc_od_input);
        tilInnerDiameter = root.findViewById(R.id.til_fbc_id_input);
    }


}
