package com.youngsoft.cookconverter.ui.measures;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.material.textfield.TextInputEditText;
import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.ViewModelMainActivity;
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
    private Button btInfoButton;
    SharedPreferences preferences;
    private Button btCopyMeasures;
    private Button btPasteMeasures;

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
        etInputValue.setText("0.0"); //TODO: can this be removed?
    }

    private void initDefaultUnit(int id) {
        spInput.setSelection(id-1);
    }

    /**
     * set listeners on the view fields & update the viewmodel when changed
     */
    private void setListeners() {

        //set click listener for the copy button
        btCopyMeasures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FS","btCopyServings onClick");
                viewModelMeasures.getMediatorOutput().observe(getViewLifecycleOwner(), new Observer<Double>() {
                    @Override
                    public void onChanged(Double aDouble) {
                        DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");
                        viewModelMeasures.getMediatorOutput().removeObserver(this);
                        Log.i("FS","btCopyServings onClick onChanged " + aDouble);
                        ViewModelMainActivity viewModelMainActivity = new ViewModelProvider(getParentFragment().getActivity()).get(ViewModelMainActivity.class);
                        viewModelMainActivity.setCopyDataValue(aDouble);
                        Toast.makeText(getContext(),"Copied output value " + decimalFormat.format(aDouble), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //set click listener for the paste button
        btPasteMeasures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ViewModelMainActivity viewModelMainActivity = new ViewModelProvider(getParentFragment().getActivity()).get(ViewModelMainActivity.class);
                viewModelMainActivity.getCopyDataValue().observe(getViewLifecycleOwner(), new Observer<Double>() {
                    @Override
                    public void onChanged(Double aDouble) {
                        viewModelMainActivity.getCopyDataValue().removeObserver(this);

                        final Double tempDouble = aDouble;
                        final DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");
                        //create an alert dialog to check to make sure they they would like to paste the new value
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                                .setMessage("Overwrite the \"Convert From\" value with " + decimalFormat.format(aDouble) + " ?")
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        etInputValue.setText(decimalFormat.format(tempDouble));
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Do nothing
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        Button btNegativeDialog = dialog.getButton(Dialog.BUTTON_NEGATIVE);
                        btNegativeDialog.setTextColor(getResources().getColor(R.color.colorPrimary));
                        Button btPositiveDialog = dialog.getButton(Dialog.BUTTON_POSITIVE);
                        btPositiveDialog.setTextColor(getResources().getColor(R.color.colorConfirmGreen));
                    }
                });
            }
        });

        //set click listener for info button
        btInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                        .setMessage(getResources().getString(R.string.info_fragment_measures))
                        .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Do nothing
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                Button btNegativeDialog = dialog.getButton(Dialog.BUTTON_NEGATIVE);
                btNegativeDialog.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });


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
        //observe changes to the output value
        viewModelMeasures.getMediatorOutput().observe(getViewLifecycleOwner(), new Observer<Double>() {
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

        //observe changes to the list of conversion factors
        final LiveData<List<ConversionFactorsRecord>> conversionFactorsList = viewModelMeasures.getAllConversionFactors();
        conversionFactorsList.observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
                conversionFactorsList.removeObserver(this); //remove the observer, this list won't change during fragment lifecycle
                ConversionFactorsRecord[] outputArray = new ConversionFactorsRecord[conversionFactorsRecords.size()];
                conversionFactorsRecords.toArray(outputArray);
                spinnerAdapterInput = new MeasuresSpinnerAdapter(context, outputArray);
                spInput.setAdapter(spinnerAdapterInput);

                initDefaultUnit(preferences.getInt(KEY_PREF_DEFAULT_UNIT,1)); //select the default value in the shared preferences
            }
        });

        //observe changes to the list of ingredients
        viewModelMeasures.getAllIngredients().observe(getViewLifecycleOwner(), new Observer<List<IngredientsRecord>>() {
            @Override
            public void onChanged(List<IngredientsRecord> ingredientsRecords) {
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
                //observe to initialise
            }
        });

        //observe changes to the output conversion factor selection
        viewModelMeasures.getOutputConversionFactor().observe(getViewLifecycleOwner(), new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord input) {
                //observe to initialise
            }
        });

        //observe the subset conversion factors list, and update the output spinner adapter
        viewModelMeasures.getSubsetConversionFactors().observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
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
        btInfoButton = root.findViewById(R.id.bt_info_fragment_measures);
        btCopyMeasures =root.findViewById(R.id.bt_copy_measures);
        btPasteMeasures = root.findViewById(R.id.bt_paste_measures);
    }

}
