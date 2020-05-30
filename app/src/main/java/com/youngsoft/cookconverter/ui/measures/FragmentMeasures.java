package com.youngsoft.cookconverter.ui.measures;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.data.IngredientsRecord;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

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
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModelMeasures = ViewModelProviders.of(this).get(ViewModelMeasures.class);
        View root = inflater.inflate(R.layout.fragment_measures, container, false);
        mapViews(root); //map views for the root layout
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        setListeners(); //init view listeners
        setObservers(); //init viewmodel observers
        etInputValue.setText("0.0"); //TODO: can this be removed?
    }










    /**
     * set listeners on the view fields & update the viewmodel when changed
     */
    private void setListeners() {
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
                Log.i("FM","input text changed");
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
                Log.i("FM","Spinner input selected Item " + selectedItem.getId());
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
                Log.i("FM","Spinner output selected Item " + selectedItem.getId());
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
                Log.i("FM","Ingredient output selected Item " + selectedItem.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                    Log.i("FM","Output value updated");
                }

            }
        });

        //observe changes to the list of conversion factors
        viewModelMeasures.getAllConversionFactors().observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
                ConversionFactorsRecord[] outputArray = new ConversionFactorsRecord[conversionFactorsRecords.size()];
                conversionFactorsRecords.toArray(outputArray);
                for(int i=0; i<outputArray.length; i++){
                    Log.i("FM","ConversionFactor element at the index "+i+" is ::"+outputArray[i].getName());
                }
                spinnerAdapterInput = new MeasuresSpinnerAdapter(context, outputArray);
                spInput.setAdapter(spinnerAdapterInput);
            }
        });

        //observe changes to the list of ingredients
        viewModelMeasures.getAllIngredients().observe(getViewLifecycleOwner(), new Observer<List<IngredientsRecord>>() {
            @Override
            public void onChanged(List<IngredientsRecord> ingredientsRecords) {
                Log.i("FM","Ingredients list change");
                IngredientsRecord[] outputArray = new IngredientsRecord[ingredientsRecords.size()];
                ingredientsRecords.toArray(outputArray);
                for(int i=0; i<outputArray.length; i++){
                    Log.i("FM","Ingredients element at the index "+i+" is ::"+outputArray[i].getName());
                }
                spinnerIngredients = new IngredientsSpinnerAdapter(context, outputArray);
                spIngredients.setAdapter(spinnerIngredients);
            }
        });

        viewModelMeasures.getSubsetConversionFactorFilter().observe(getViewLifecycleOwner(), new Observer<ViewModelMeasures.SubsetConversionFactorFilter>() {
            @Override
            public void onChanged(ViewModelMeasures.SubsetConversionFactorFilter subsetConversionFactorFilter) {
                Log.i("FM","SubsetConversionFactorFilter chnaged");
            }
        });

        viewModelMeasures.getSubsetConversionFactors().observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
                ConversionFactorsRecord[] outputArray = new ConversionFactorsRecord[conversionFactorsRecords.size()];
                conversionFactorsRecords.toArray(outputArray);
                for(int i=0; i<outputArray.length; i++){
                    Log.i("FM","Subset conversionFactor element at the index "+i+" is ::"+outputArray[i].getName());
                }

                spinnerAdapterOutput = new MeasuresSpinnerAdapter(context, outputArray);
                spOutput.setAdapter(spinnerAdapterOutput);
            }
        });

        /**
        //observe changes to the subset list of conversion factors
        viewModelMeasures.getSubsetConversionFactors().observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
                ConversionFactorsRecord[] outputArray = new ConversionFactorsRecord[conversionFactorsRecords.size()];
                conversionFactorsRecords.toArray(outputArray);
                for(int i=0; i<outputArray.length; i++){
                    Log.i("FM","Element at the index "+i+" is ::"+outputArray[i].getName());
                }

                spinnerAdapterOutput = new MeasuresSpinnerAdapter(context, outputArray);
                spOutput.setAdapter(spinnerAdapterOutput);
            }
        });
         **/


    }







    /**
     * map views from the root view
     * @param root
     */
    private void mapViews(View root) {
        spInput = root.findViewById(R.id.sp_measure_input);
        spOutput = root.findViewById(R.id.sp_measure_output);
        etInputValue = root.findViewById(R.id.tiet_measures_input);
        etOutputValue = root.findViewById(R.id.tiet_measures_output);
        spIngredients = root.findViewById(R.id.sp_measure_ingredients);
    }

}
