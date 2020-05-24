package com.youngsoft.cookconverter.ui.measures;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.youngsoft.cookconverter.R;

import org.w3c.dom.Text;

public class FragmentMeasures extends Fragment {

    private ViewModelMeasures viewModelMeasures;
    private Spinner spInput;
    private Spinner spOutput;
    private TextInputEditText etInputValue;
    private TextInputEditText etOutputValue;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModelMeasures = ViewModelProviders.of(this).get(ViewModelMeasures.class);
        View root = inflater.inflate(R.layout.fragment_measures, container, false);
        mapViews(root); //map views for the root layout
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                    viewModelMeasures.setInputValueMutable(Double.valueOf(etInputValue.getText().toString()));
                }
                Log.i("FM","input text changed");
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                etOutputValue.setText(aDouble.toString());
                Log.i("FM","input value updated");
            }
        });

        /**
        //observe changes to the input value
        viewModelMeasures.getInputValue().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                //if not the same as the existing text, update the text field
                if (aDouble.toString().equals(etInputValue.getText().toString())) {
                    Log.i("FM","input value the same: " + aDouble+ " vs " + Double.valueOf(etInputValue.getText().toString()) );
                } else {
                    etInputValue.setText(aDouble.toString());
                    Log.i("FM","input value updated: " + aDouble+ " vs " + Double.valueOf(etInputValue.getText().toString()) );
                }
            }
        });**/
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
    }

}
