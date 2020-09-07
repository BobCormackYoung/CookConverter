package com.youngsoft.cookconverter.ui.measures;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.material.textfield.TextInputEditText;
import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.data.IngredientsRecord;
import com.youngsoft.cookconverter.ui.save.BottomSheetSaveMeasurement;
import com.youngsoft.cookconverter.ui.util.IngredientsSpinnerAdapter;
import com.youngsoft.cookconverter.ui.util.MeasuresSpinnerAdapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import static com.youngsoft.cookconverter.ui.preferences.FragmentPreferences.KEY_PREF_DEFAULT_UNIT;

public class FragmentMeasures extends Fragment {

    private ViewModelMeasures viewModelMeasures;
    private Spinner spInput;
    private Spinner spOutput;
    private Spinner spIngredients;
    private MeasuresSpinnerAdapter spinnerAdapterInput;
    private MeasuresSpinnerAdapter spinnerAdapterOutput;
    private IngredientsSpinnerAdapter spinnerIngredients;
    private TextInputEditText etInputValue;
    private TextInputEditText etOutputValue;
    private Button btSaveMeasure;
    private BottomSheetSaveMeasurement bottomSheetSaveMeasurement;
    private Context context;
    SharedPreferences preferences;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModelMeasures = new ViewModelProvider(this).get(ViewModelMeasures.class);
        View root = inflater.inflate(R.layout.fragment_measures, container, false);
        mapViews(root); //map views for the root layout
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        setObservers(); //init viewmodel observers
        setListeners(); //init view listeners
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        initDefaultUnit(preferences.getInt(KEY_PREF_DEFAULT_UNIT,1));
        etInputValue.setText("0.0"); //TODO: can this be removed?
    }

    @Override
    public void onResume() {
        super.onResume();
        //update the shared preference default value in the viewmodel & views
        Log.i("FragmentMeasures","OnResume");
        initDefaultUnit(preferences.getInt(KEY_PREF_DEFAULT_UNIT,1));
    }

    private void initDefaultUnit(int id) {
        Log.i("FragmentMeasures","initDefaultUnit " + id);
        //spOutput.setSelection(id);
        spInput.setSelection(id-1);
        //Log.i("FragmentMeasures","outputSelected " + spOutput.getSelectedItemPosition() + " " + spinnerAdapterOutput.getItem(id).getName());
        //Log.i("FragmentMeasures","inputSelected " + spInput.getSelectedItemPosition() + " " + spinnerAdapterInput.getItem(id).getName());
    }


    /**
     * set listeners on the view fields & update the viewmodel when changed
     */
    private void setListeners() {
        Log.i("FragmentMeasures","setListeners");
        //listen for changes to the input value edit text field
        etInputValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etInputValue.getText().toString().isEmpty()) {
                    viewModelMeasures.setInputValueMutable(0.0);
                } else {
                    Double temp = null;
                    //try to catch error associated with leading decimal
                    try {
                        temp = DecimalFormat.getNumberInstance().parse(etInputValue.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        //Can't handle leading decimal
                        e.printStackTrace();
                    }
                    viewModelMeasures.setInputValueMutable(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //listen for changes to the input spinner value
        spInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("FragmentMeasures","spInput.setOnItemSelectedListener " + position);
                ConversionFactorsRecord selectedItem = spinnerAdapterInput.getItem(position);
                viewModelMeasures.setConversionFactorInputID(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //listen for changes to the output spinner value
        spOutput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("FragmentMeasures","spOutput.setOnItemSelectedListener " + position);
                ConversionFactorsRecord selectedItem = spinnerAdapterOutput.getItem(position);
                viewModelMeasures.setConversionFactorOutputID(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //listen for changes to the selected ingredient type
        spIngredients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IngredientsRecord selectedItem = spinnerIngredients.getItem(position);
                viewModelMeasures.setIngredientSelected(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //set save button listener
        btSaveMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetSaveMeasurement = new BottomSheetSaveMeasurement( 1);
                bottomSheetSaveMeasurement.show(getChildFragmentManager(), "saveDataBottomSheet");
            }
        });
    }

    /**
     * set observers for the viewmodel and update view when changed
     */
    private void setObservers() {
        Log.i("FragmentMeasures","setObservers");
        //observe changes to the output value
        viewModelMeasures.getMediatorOutput().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                Log.i("FragmentMeasures","getMediatorOutput " + aDouble);
                //check if value is too small to be worth displaying
                if (aDouble*1000 < 1) {
                    etOutputValue.setText("0.0");
                } else {
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");
                    etOutputValue.setText(decimalFormat.format(aDouble));
                }

            }
        });

        //observe changes to the list of conversion factors
        viewModelMeasures.getAllConversionFactors().observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
                Log.i("FragmentMeasures","getAllConversionFactors " + conversionFactorsRecords.size());
                ConversionFactorsRecord[] outputArray = new ConversionFactorsRecord[conversionFactorsRecords.size()];
                conversionFactorsRecords.toArray(outputArray);
                spinnerAdapterInput = new MeasuresSpinnerAdapter(context, outputArray);
                spInput.setAdapter(spinnerAdapterInput);
            }
        });

        //observe changes to the list of ingredients
        viewModelMeasures.getAllIngredients().observe(getViewLifecycleOwner(), new Observer<List<IngredientsRecord>>() {
            @Override
            public void onChanged(List<IngredientsRecord> ingredientsRecords) {
                Log.i("FragmentMeasures","getAllIngredients " + ingredientsRecords.size());
                IngredientsRecord[] outputArray = new IngredientsRecord[ingredientsRecords.size()];
                ingredientsRecords.toArray(outputArray);
                spinnerIngredients = new IngredientsSpinnerAdapter(context, outputArray);
                spIngredients.setAdapter(spinnerIngredients);
            }
        });

        //observe changes t the subset conversion factor filter
        viewModelMeasures.getSubsetConversionFactorFilter().observe(getViewLifecycleOwner(), new Observer<ViewModelMeasures.SubsetConversionFactorFilter>() {
            @Override
            public void onChanged(ViewModelMeasures.SubsetConversionFactorFilter subsetConversionFactorFilter) {
                //needs to be observed to initialise
            }
        });

        //observe changes to the input conversion factor selection
        viewModelMeasures.getInputConversionFactor().observe(getViewLifecycleOwner(), new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord input) {
                Log.i("FragmentMeasures", "getInputConversionFactor " + input.getName());
                //check if the new selection is the same as currently selected, if yes, don't update the view
                /**
                if (spinnerAdapterInput.getItem(spInput.getSelectedItemPosition()).getConversionFactorID() ==
                        input.getConversionFactorID()) {
                    Log.i("FragmentMeasures", "getInputConversionFactor new same as selected");
                } else {
                    Log.i("FragmentMeasures", "getInputConversionFactor new different to selected");
                    //cycle through all items in the adapter, and select the one that matches the viewmodel
                    for (int i=0; i<spinnerAdapterInput.getCount()-1; i++) {
                        if (spinnerAdapterInput.getItem(i).getConversionFactorID() == input.getConversionFactorID()) {
                            spInput.setSelection(i);
                            Log.i("FragmentMeasures", "getInputConversionFactor item " + i);
                        } else {
                            spInput.setSelection(0);
                            Log.i("FragmentMeasures", "getInputConversionFactor default 0");
                        }
                    }
                } **/
                spInput.setSelection((int) input.getConversionFactorID()-1);
            }
        });

        //observe changes to the output conversion factor selection
        viewModelMeasures.getOutputConversionFactor().observe(getViewLifecycleOwner(), new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord input) {
                Log.i("FragmentMeasures", "getOutputConversionFactor " + input.getName());
                //check if the new selection is the same as currently selected, if yes, don't update the view
                /**if (spinnerAdapterOutput.getItem(spOutput.getSelectedItemPosition()).getConversionFactorID() ==
                        input.getConversionFactorID()) {
                    Log.i("FragmentMeasures", "getOutputConversionFactor new same as selected");
                } else {
                    //cycle through all items in the adapter, and select the one that matches the viewmodel
                    for (int i=0; i<spinnerAdapterOutput.getCount()-1; i++) {
                        if (spinnerAdapterOutput.getItem(i).getConversionFactorID() == input.getConversionFactorID()) {
                            spOutput.setSelection(i);
                            Log.i("FragmentMeasures", "getOutputConversionFactor item " + i);
                        } else {
                            spOutput.setSelection(0);
                            Log.i("FragmentMeasures", "getOutputConversionFactor default 0");
                        }
                    }
                }**/
            }
        });

        //observe the subset conversion factors list, and update the output spinner adapter
        viewModelMeasures.getSubsetConversionFactors().observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
                Log.i("FragmentMeasures","getSubsetConversionFactors " + conversionFactorsRecords.size());
                ConversionFactorsRecord[] outputArray = new ConversionFactorsRecord[conversionFactorsRecords.size()];
                conversionFactorsRecords.toArray(outputArray);
                spinnerAdapterOutput = new MeasuresSpinnerAdapter(context, outputArray);
                spOutput.setAdapter(spinnerAdapterOutput);
            }
        });
    }







    /**
     * map views from the root view
     * @param root input view for mapping
     */
    private void mapViews(View root) {
        spInput = root.findViewById(R.id.sp_measure_input);
        spOutput = root.findViewById(R.id.sp_measure_output);
        etInputValue = root.findViewById(R.id.tiet_measures_input);
        etOutputValue = root.findViewById(R.id.tiet_measures_output);
        spIngredients = root.findViewById(R.id.sp_measure_ingredients);
        btSaveMeasure = root.findViewById(R.id.bt_save_measures);
    }

}
