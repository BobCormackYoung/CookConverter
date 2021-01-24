package com.youngsoft.cookconverter.ui.servings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
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

public class FragmentServings extends GlobalFragment {

    private ViewModelServings viewModelServings;
    private TextInputEditText etInputValue;
    private TextInputEditText etOutputValue;
    private NumberPicker npInputServing;
    private NumberPicker npOutputServing;
    private Button btSaveServing;
    private Button btInfoButton;
    private BottomSheetSaveMeasurement bottomSheetSaveMeasurement;
    private Button btCopyServing;
    private Button btPasteServing;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModelServings = new ViewModelProvider(this).get(ViewModelServings.class);
        View root = inflater.inflate(R.layout.fragment_servings, container, false);
        mapViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNumberPickers();
        setListeners();
        setObservers();
        etInputValue.setText("0.0"); //is this needed
    }

    @Override
    public void displayInformationAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                .setMessage(getResources().getString(R.string.info_fragment_serving))
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

    private void initNumberPickers() {
        npInputServing.setMinValue(1);
        npInputServing.setMaxValue(100);
        npInputServing.setWrapSelectorWheel(true);
        npOutputServing.setMinValue(1);
        npOutputServing.setMaxValue(100);
        npOutputServing.setWrapSelectorWheel(true);
    }

    /**
     * set livedata observers
     */
    private void setObservers() {

        //observe changes to the output value
        viewModelServings.getMediatorOutput().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                //check if value is too small to be worth displaying
                if (aDouble * 1000 < 1) {
                    etOutputValue.setText("0.0");
                } else {
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");
                    etOutputValue.setText(decimalFormat.format(aDouble));
                }

            }
        });

    }

    /**
     * set view listeners
     */
    private void setListeners() {

        //set click listener for the copy button
        btCopyServing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModelServings.getMediatorOutput().observe(getViewLifecycleOwner(), new Observer<Double>() {
                    @Override
                    public void onChanged(Double aDouble) {
                        DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");
                        viewModelServings.getMediatorOutput().removeObserver(this);
                        ViewModelMainActivity viewModelMainActivity = new ViewModelProvider(getParentFragment().getActivity()).get(ViewModelMainActivity.class);
                        viewModelMainActivity.setCopyDataValue(aDouble);
                        Toast.makeText(getContext(),"Copied output value " + aDouble, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //set click listener for the paste button
        btPasteServing.setOnClickListener(new View.OnClickListener() {
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

        //set listener for the input edit text
        etInputValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etInputValue.getText().toString().isEmpty()) {
                    viewModelServings.setInputValue(0.0);
                } else {
                    Double temp = null;
                    //try to catch error associated with leading decimal
                    try {
                        temp = DecimalFormat.getNumberInstance().parse(etInputValue.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        //Can't handle leading decimal
                        e.printStackTrace();
                    }
                    viewModelServings.setInputValue(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //set listener for the input serving size
        npInputServing.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                viewModelServings.setInputServingSize(newVal);
            }
        });

        //set listener for the output serving size
        npOutputServing.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                viewModelServings.setOutputServingSize(newVal);
            }
        });

        //set listener for save button
        btSaveServing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetSaveMeasurement = new BottomSheetSaveMeasurement( ViewModelSaveMeasurement.LAUNCH_CASE_SERVINGS);
                bottomSheetSaveMeasurement.show(getChildFragmentManager(), "saveDataBottomSheet");
            }
        });
    }


    private void mapViews(View root) {
        etInputValue = root.findViewById(R.id.tiet_fs_input_value);
        etOutputValue = root.findViewById(R.id.tiet_fs_output_value);
        npInputServing = root.findViewById(R.id.np_servings_input);
        npOutputServing = root.findViewById(R.id.np_servings_output);
        btSaveServing = root.findViewById(R.id.bt_save_serving);
        btCopyServing = root.findViewById(R.id.bt_copy_serving);
        btPasteServing = root.findViewById(R.id.bt_paste_serving);
    }
}
