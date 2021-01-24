package com.youngsoft.cookconverter.ui.ingredients;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.ViewModelMainActivity;
import com.youngsoft.cookconverter.ui.save.BottomSheetSaveMeasurement;
import com.youngsoft.cookconverter.ui.save.ViewModelSaveMeasurement;
import com.youngsoft.cookconverter.ui.util.GlobalFragment;

import java.text.DecimalFormat;
import java.text.ParseException;

public class FragmentIngredients extends GlobalFragment {

    ViewModelIngredients viewModelIngredients;

    BottomSheetSaveMeasurement bottomSheetSaveMeasurement;

    BottomSheetScaleFactor bottomSheetScaleFactor;

    Button btSave;
    Button btCopy;
    Button btPaste;
    Button btEditScaleFactor;
    TextView tvScaleFactor;
    TextInputEditText tietInputValue;
    TextInputEditText tietOutputValue;
    TextView tvRecipeValue;
    TextView tvUserValue;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModelIngredients = new ViewModelProvider(this).get(ViewModelIngredients.class);
        View root = inflater.inflate(R.layout.fragment_ingredients, container, false);
        mapViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
        setObservers();
    }

    @Override
    public void displayInformationAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                .setMessage(getResources().getString(R.string.info_fragment_ingredients))
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

    private void mapViews(View root) {
        btSave = root.findViewById(R.id.bt_save_ingredients);
        btCopy = root.findViewById(R.id.bt_copy_ingredients);
        btPaste = root.findViewById(R.id.bt_paste_ingredients);
        btEditScaleFactor = root.findViewById(R.id.bt_fi_scale_factor_edit);
        tvScaleFactor = root.findViewById(R.id.tv_fi_scale_factor);
        tietInputValue = root.findViewById(R.id.tiet_fi_input_value);
        tietOutputValue = root.findViewById(R.id.tiet_fi_output_value);
        tvRecipeValue = root.findViewById(R.id.tv_fi_input_value1);
        tvUserValue = root.findViewById(R.id.tv_fi_input_value2);
    }

    private void setListeners() {
        //set click listener for the copy button
        btCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModelIngredients.getMediatorOutput().observe(getViewLifecycleOwner(), new Observer<Double>() {
                    @Override
                    public void onChanged(Double aDouble) {
                        DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");
                        viewModelIngredients.getMediatorOutput().removeObserver(this);
                        ViewModelMainActivity viewModelMainActivity = new ViewModelProvider(getParentFragment().getActivity()).get(ViewModelMainActivity.class);
                        viewModelMainActivity.setCopyDataValue(aDouble);
                        Toast.makeText(getContext(),"Copied output value " + decimalFormat.format(aDouble), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //set click listener for the paste button
        btPaste.setOnClickListener(new View.OnClickListener() {
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
                                        tietInputValue.setText(decimalFormat.format(tempDouble));
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

        //set listener for the input edit text
        tietInputValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tietInputValue.getText().toString().isEmpty()) {
                    viewModelIngredients.setInputValueMutable(0.0);
                } else {
                    double temp;
                    //try to catch error associated with leading decimal
                    try {
                        temp = DecimalFormat.getNumberInstance().parse(tietInputValue.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        //Can't handle leading decimal
                        e.printStackTrace();
                        temp = 0.0;
                    }
                    viewModelIngredients.setInputValueMutable(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //set listener for save button
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetSaveMeasurement = new BottomSheetSaveMeasurement( ViewModelSaveMeasurement.LAUNCH_CASE_INGREDIENTS);
                bottomSheetSaveMeasurement.show(getChildFragmentManager(), "saveDataBottomSheet");
            }
        });

        //DEBUG
        btEditScaleFactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetScaleFactor = new BottomSheetScaleFactor();
                bottomSheetScaleFactor.show(getChildFragmentManager(),"scaleFactorBottomFragment");
            }
        });
    }

    private void setObservers() {
        //observe output value
        viewModelIngredients.getMediatorOutput().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                //check if value is too small to be worth displaying
                if (aDouble*1000 < 1) {
                    tietOutputValue.setText("0.0");
                } else {
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");
                    tietOutputValue.setText(decimalFormat.format(aDouble));
                }
            }
        });

        //observe the recipe value
        viewModelIngredients.getInputRecipeValue().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble != null) {
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");
                    tvRecipeValue.setText(decimalFormat.format(aDouble));
                }
            }
        });

        //observe the user value
        viewModelIngredients.getInputUserValue().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble != null) {
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");
                    tvUserValue.setText(decimalFormat.format(aDouble));
                }
            }
        });

        //observe the scale factor value
        viewModelIngredients.getScaleFactor().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble != null) {
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.0");
                    tvScaleFactor.setText(decimalFormat.format(aDouble));
                }
            }
        });
    }
}
