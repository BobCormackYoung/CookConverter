package com.youngsoft.cookconverter.ui.save;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.data.IngredientsRecord;
import com.youngsoft.cookconverter.ui.baking.ViewModelBaking;
import com.youngsoft.cookconverter.ui.ingredients.ViewModelIngredients;
import com.youngsoft.cookconverter.ui.measures.ViewModelMeasures;
import com.youngsoft.cookconverter.ui.servings.ViewModelServings;
import com.youngsoft.cookconverter.ui.util.MeasuresSpinnerAdapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

public class BottomSheetSaveMeasurement extends BottomSheetDialogFragment {

    private ViewModelMeasures viewModelMeasures;
    private ViewModelBaking viewModelBaking;
    private ViewModelServings viewModelServings;
    private ViewModelSaveMeasurement viewModelSaveMeasurement;
    private ViewModelIngredients viewModelIngredients;
    private final int launchCase; //1 = measures, 2 = baking, 3 = servings, 4 = ingredients

    private TextInputEditText etMeasurementName;
    private TextInputEditText etMeasurementValue;
    private AutoCompleteTextView actvMeasurementUnit;
    private MeasuresSpinnerAdapter spinnerAdapterUnits;
    private MaterialButton btSave;
    private MaterialButton btCancel;

    private boolean dataCompleteForSave = false;

    /**
     * Constructor for the Bottom Sheet fragment for saving data to recipe list
     * @param i int indicating which fragment launched the activity, 1 = Measures, 2 = Baking, 3 = servings
     */
    public BottomSheetSaveMeasurement(int i) {
        this.launchCase = i;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bottomsheet_save_measurement, container, false);
        viewModelSaveMeasurement = new ViewModelProvider(this).get(ViewModelSaveMeasurement.class);

        //find the viewmodels from the parent fragment depending on which was used to launch this fragment
        if (launchCase == 1) {
            if (getParentFragment() != null) {
                viewModelMeasures = new ViewModelProvider(getParentFragment()).get(ViewModelMeasures.class);
            }
        } else if (launchCase == 2) {
            if (getParentFragment() != null) {
                viewModelBaking = new ViewModelProvider(getParentFragment()).get(ViewModelBaking.class);
            }
        } else if (launchCase == 3) {
            if (getParentFragment() != null) {
                viewModelServings = new ViewModelProvider(getParentFragment()).get(ViewModelServings.class);
            }
        } else if (launchCase == 4) {
            if (getParentFragment() != null) {
                viewModelIngredients = new ViewModelProvider(getParentFragment()).get(ViewModelIngredients.class);
            }
        } else {

        }

        mapViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //set listeners for the bottom sheet view
        setListeners();

        //set common observers
        setCommonObservers();

        //set livedata observers depending on which parent fragment was used to launch this fragment
        if (launchCase == 1) {
            setMeasureObservers();
        } else if (launchCase == 2) {
            setBakingObservers();
        } else if (launchCase == 3) {
            setServingObservers();
        } else if (launchCase == 4) {
            setIngredientsObservers();
        } else {

        }

    }

    /**
     * set the observers that are common to all launch cases
     */
    private void setCommonObservers() {
        //observe the output value
        viewModelSaveMeasurement.getMeasurementValue().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {

                //get the current value in the output text box from the bottom sheet
                double temp;
                //try to catch error associated with leading decimal
                try {
                    temp = DecimalFormat.getNumberInstance().parse(etMeasurementValue.getText().toString()).doubleValue();
                } catch (ParseException e) {
                    //Can't handle leading decimal
                    e.printStackTrace();
                    temp = 0.0;
                }

                if (aDouble != null) { //check if the observed value is null
                    if (aDouble != temp) { //check if observed value is the same as the displayed value
                        if (aDouble * 1000 < 1) { //check is smaller than usable significant figures
                            etMeasurementValue.setText("0.0");
                        } else { //else display in required format
                            DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");
                            etMeasurementValue.setText(decimalFormat.format(aDouble));
                        }
                    }
                } else { //if null, display as empty
                    etMeasurementValue.setText("");
                }

            }
        });

        //observe whether data is ready for save
        viewModelSaveMeasurement.getIsDataCompleteForSave().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                dataCompleteForSave = aBoolean;
            }
        });

        //observer the selected ingredient value, and update the view
        viewModelSaveMeasurement.getMeasurementUnit().observe(getViewLifecycleOwner(), new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                actvMeasurementUnit.setText(conversionFactorsRecord.getName());
            }
        });
    }

    /**
     * set the observers if the bottom sheet is launched from the measurement fragment
     */
    private void setMeasureObservers() {
        //observe the output value
        final LiveData<Double> outputValue = viewModelMeasures.getMediatorOutput();
        outputValue.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                outputValue.removeObserver(this); //remove the observer, no longer want to observe changes to the output value
                viewModelSaveMeasurement.setMeasurementValue(aDouble); //save the output value into the viewmodel for the dialogfragment
            }
        });

        viewModelMeasures.getIngredientsRecord().observe(getViewLifecycleOwner(), new Observer<IngredientsRecord>() {
            @Override
            public void onChanged(IngredientsRecord ingredientsRecord) {
                viewModelMeasures.getIngredientsRecord().removeObserver(this);
                if (ingredientsRecord.getType()!=0) {
                    etMeasurementName.setText(ingredientsRecord.getName());
                }
            }
        });

        //observe the list of units
        viewModelSaveMeasurement.getAllConversionFactors().observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
                ConversionFactorsRecord[] outputArray = new ConversionFactorsRecord[conversionFactorsRecords.size()];
                conversionFactorsRecords.toArray(outputArray);
                spinnerAdapterUnits = new MeasuresSpinnerAdapter(getContext(), outputArray);
                actvMeasurementUnit.setAdapter(spinnerAdapterUnits);

                //observe conversion factor, update the spinner, then remove the observer
                final LiveData<ConversionFactorsRecord> selectedConversionFactor = viewModelMeasures.getOutputConversionFactor();
                selectedConversionFactor.observe(getViewLifecycleOwner(), new Observer<ConversionFactorsRecord>() {
                    @Override
                    public void onChanged(ConversionFactorsRecord aConversionFactor) {
                        selectedConversionFactor.removeObserver(this);
                        viewModelSaveMeasurement.setMeasurementUnit(aConversionFactor);
                        //spMeasurementUnit.setSelection((int) (aConversionFactor.getConversionFactorID() - 1));
                    }
                });
            }
        });
    }

    /**
     * set the observers if the bottom sheet is launched from the baking fragment
     */
    private void setBakingObservers() {
        //observe the output value
        final LiveData<Double> outputValue = viewModelBaking.getMediatorOutput();
        outputValue.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                outputValue.removeObserver(this); //remove the observer, no longer want to observe changes to the output value
                viewModelSaveMeasurement.setMeasurementValue(aDouble); //save the output value into the viewmodel for the dialogfragment
            }
        });

        //observe the list of units
        viewModelSaveMeasurement.getAllConversionFactors().observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
                ConversionFactorsRecord[] outputArray = new ConversionFactorsRecord[conversionFactorsRecords.size()];
                conversionFactorsRecords.toArray(outputArray);
                spinnerAdapterUnits = new MeasuresSpinnerAdapter(getContext(), outputArray);
                actvMeasurementUnit.setAdapter(spinnerAdapterUnits);
                viewModelSaveMeasurement.setMeasurementUnit(outputArray[0]);
            }
        });
    }

    /**
     * set the observers if the bottom fragment is launched from the serving fragment
     */
    private void setServingObservers() {
        //observe the output value
        final LiveData<Double> outputValue = viewModelServings.getMediatorOutput();
        outputValue.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                outputValue.removeObserver(this); //remove the observer, no longer want to observe changes to the output value
                viewModelSaveMeasurement.setMeasurementValue(aDouble); //save the output value into the viewmodel for the dialogfragment
            }
        });

        //observe the list of units
        viewModelSaveMeasurement.getAllConversionFactors().observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
                ConversionFactorsRecord[] outputArray = new ConversionFactorsRecord[conversionFactorsRecords.size()];
                conversionFactorsRecords.toArray(outputArray);
                spinnerAdapterUnits = new MeasuresSpinnerAdapter(getContext(), outputArray);
                actvMeasurementUnit.setAdapter(spinnerAdapterUnits);
                viewModelSaveMeasurement.setMeasurementUnit(outputArray[0]);
            }
        });
    }

    /**
     * set the observers if the bottom fragment is launched from the ingredients fragment
     */
    private void setIngredientsObservers() {
        //observe the output value
        final LiveData<Double> outputValue = viewModelIngredients.getMediatorOutput();
        outputValue.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                outputValue.removeObserver(this); //remove the observer, no longer want to observe changes to the output value
                viewModelSaveMeasurement.setMeasurementValue(aDouble); //save the output value into the viewmodel for the dialogfragment
            }
        });

        //observe the list of units
        viewModelSaveMeasurement.getAllConversionFactors().observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
                ConversionFactorsRecord[] outputArray = new ConversionFactorsRecord[conversionFactorsRecords.size()];
                conversionFactorsRecords.toArray(outputArray);
                spinnerAdapterUnits = new MeasuresSpinnerAdapter(getContext(), outputArray);
                actvMeasurementUnit.setAdapter(spinnerAdapterUnits);
                viewModelSaveMeasurement.setMeasurementUnit(outputArray[0]);
            }
        });
    }

    /**
     * set listeners for the bottom sheet view
     * primarily the spinner for selecting units, and the name of the ingredients
     */
    private void setListeners() {
        //button to dismiss the bottom sheet dialog
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //button to save data then dismiss the bottom sheet dialog
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataCompleteForSave) {
                    viewModelSaveMeasurement.saveData(); //save the data
                    dismiss();
                } else {
                    Toast.makeText(getContext(),"Missing data required for save!", Toast.LENGTH_LONG).show();
                }

            }
        });

        //observe text box for ingredient name
        etMeasurementName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModelSaveMeasurement.setMeasurementName(s.toString()); //save the data to the viewmodel
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //listen for changes to the measurement unit spinner value
        actvMeasurementUnit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConversionFactorsRecord selectedItem = spinnerAdapterUnits.getItem(position);
                viewModelSaveMeasurement.setMeasurementUnit(selectedItem);
            }
        });

        //set listener for the output edit text
        etMeasurementValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etMeasurementValue.getText().toString().isEmpty()) {
                    viewModelSaveMeasurement.setMeasurementValue(0.0);
                } else {
                    double temp;
                    //try to catch error associated with leading decimal
                    try {
                        temp = DecimalFormat.getNumberInstance().parse(etMeasurementValue.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        //Can't handle leading decimal
                        e.printStackTrace();
                        temp = 0.0;
                    }
                    viewModelSaveMeasurement.setMeasurementValue(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * map views from the bottom sheet fragment
     * @param root the bottom sheet fragment
     */
    private void mapViews(View root) {
        etMeasurementName = root.findViewById(R.id.tiet_save_measurement_name);
        etMeasurementValue = root.findViewById(R.id.tiet_save_measurement_value);
        actvMeasurementUnit = root.findViewById(R.id.actv_save_measurement_spinner);
        btCancel = root.findViewById(R.id.bt_cancel_measurement);
        btSave = root.findViewById(R.id.bt_save_measurement);
    }
}
