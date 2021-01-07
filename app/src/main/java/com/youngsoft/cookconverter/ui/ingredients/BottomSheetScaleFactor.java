package com.youngsoft.cookconverter.ui.ingredients;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.youngsoft.cookconverter.R;

import java.text.DecimalFormat;
import java.text.ParseException;

public class BottomSheetScaleFactor extends BottomSheetDialogFragment {

    private ViewModelIngredients viewModelIngredients;
    private ViewModelScaleFactor viewModelScaleFactor;

    private TextInputEditText tietRecipeAmount;
    private TextInputEditText tietUserAmount;
    private TextInputEditText tietScaleFactor;
    private TextInputLayout tilRecipeAmount;
    private Button btSave;
    private Button btCancel;

    /**
     * empty constructor
     */
    public BottomSheetScaleFactor () {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("BSSF","onCreateView");
        View root = inflater.inflate(R.layout.bottomsheet_scale_factor, container, false);

        //set the viewmodel for this bottomsheet fragment
        viewModelScaleFactor = new ViewModelProvider(this).get(ViewModelScaleFactor.class);

        //set the viewmodel for the parent fragment
        viewModelIngredients = new ViewModelProvider(getParentFragment()).get(ViewModelIngredients.class);

        mapViews(root);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("BSSF","onViewCreated");
        setListeners();

        setInputObservers();
    }

    private void setListeners() {
        Log.i("BSSF","setListeners");
        //recipe amount
        tietRecipeAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("BSSF","tietRecipeAmount onTextChanged");
                if (tietRecipeAmount.getText().toString().isEmpty()) {
                    viewModelScaleFactor.setRecipeAmount(0.0);
                } else {
                    double temp;
                    //try to catch error associated with leading decimal
                    try {
                        temp = DecimalFormat.getNumberInstance().parse(tietRecipeAmount.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        //Can't handle leading decimal
                        e.printStackTrace();
                        temp = 0.0;
                    }
                    viewModelScaleFactor.setRecipeAmount(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //user amount
        tietUserAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("BSSF","tietUserAmount onTextChanged");
                if (tietUserAmount.getText().toString().isEmpty()) {
                    viewModelScaleFactor.setUserAmount(0.0);
                } else {
                    double temp;
                    //try to catch error associated with leading decimal
                    try {
                        temp = DecimalFormat.getNumberInstance().parse(tietUserAmount.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        //Can't handle leading decimal
                        e.printStackTrace();
                        temp = 0.0;
                    }
                    viewModelScaleFactor.setUserAmount(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //cancel button
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //save button
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tilRecipeAmount.getError() == null) {
                    //no error, can save the data
                    if (viewModelScaleFactor.saveDataAsOutput(viewModelIngredients)) {
                        //saved correctly, can dismiss the bottomSheet
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Can't save the data - check everything is correct.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Can't save data with a Recipe Amount = 0", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setInputObservers() {
        Log.i("BSSF","setInputObservers");
        //error state of the recipe amount
        viewModelScaleFactor.getIsRecipeAmountErrorState().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.i("BSSF","getIsRecipeAmountErrorState onChanged");
                if (aBoolean) {
                    tilRecipeAmount.setError("Non-Zero Value Required!");
                } else {
                    tilRecipeAmount.setError(null);
                }
            }
        });


        //observe the first instance of the recipe amount from the parent fragment/viewmodel then remove the observer
        viewModelIngredients.getInputRecipeValue().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");
                tietRecipeAmount.setText(decimalFormat.format(aDouble));
                viewModelScaleFactor.getRecipeAmount().removeObserver(this);
            }
        });

        //observe the first instance of the user amount from the parent fragment/viewmodel then remove the observer
        viewModelIngredients.getInputUserValue().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");
                tietUserAmount.setText(decimalFormat.format(aDouble));
                viewModelScaleFactor.getUserAmount().removeObserver(this);
            }
        });


        //observe the scale factor
        viewModelScaleFactor.getScaleFactor().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                Log.i("BSSF","getScaleFactor onChanged");
                DecimalFormat decimalFormat = new DecimalFormat("#,##0.0");
                tietScaleFactor.setText(decimalFormat.format(aDouble));
            }
        });
    }

    private void mapViews(View root) {
        Log.i("BSSF","getScaleFactor mapViews");
        btSave = root.findViewById(R.id.bt_ingredientscalefactor_save);
        btCancel = root.findViewById(R.id.bt_ingredientscalefactor_cancel);
        tietRecipeAmount = root.findViewById(R.id.tiet_bssc_recipe_amount);
        tietUserAmount = root.findViewById(R.id.tiet_bssc_user_amount);
        tietScaleFactor = root.findViewById(R.id.tiet_bssc_scale_factor);
        tilRecipeAmount = root.findViewById(R.id.til_bssc_recipe_amount);
    }
}
