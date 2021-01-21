package com.youngsoft.cookconverter.ui.preferences;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.data.ConversionFactorsRecord;

import java.util.ArrayList;
import java.util.List;

public class DialogUnitPicker extends DialogFragment implements AdapterUnitPickerList.OnUnitClickListener {

    ViewModelPreferences viewModelPreferences;
    View view;
    RecyclerView recyclerView;
    AdapterUnitPickerList adapterUnitPickerList;
    List<ConversionFactorsRecord> conversionFactorsRecordList;
    ArrayList<ConversionFactorsRecord> unitArrayList;
    String keyPref;
    Preference preference;

    private final OnSaveListener onSaveListener;

    public DialogUnitPicker(OnSaveListener onSaveListener, String keyPref, List<ConversionFactorsRecord> conversionFactorsRecordList, Preference preference) {
        this.onSaveListener = onSaveListener;
        this.conversionFactorsRecordList = conversionFactorsRecordList;
        this.keyPref = keyPref;
        this.preference = preference;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getParentFragment().getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        view = inflater.inflate(R.layout.fragment_unit_picker_dialog, null);
        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //LoginDialogFragment.this.getDialog().cancel();
                    }
                });
        recyclerView = view.findViewById(R.id.rv_unit_picker);
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapterUnitPickerList = new AdapterUnitPickerList(this);
        recyclerView.setAdapter(adapterUnitPickerList);

        unitArrayList = new ArrayList<>(conversionFactorsRecordList);

        adapterUnitPickerList.submitList(unitArrayList);
    }

    @Override
    public void onDestroyView() {
        view = null;
        super.onDestroyView();
    }

    @Override
    public void onUnitClick(ConversionFactorsRecord index) {
        onSaveListener.onSave(index, keyPref, preference, conversionFactorsRecordList);
        dismiss();
    }

    public interface OnSaveListener {
        void onSave(ConversionFactorsRecord conversionFactorsRecord, String keyPref, Preference preference, List<ConversionFactorsRecord> conversionFactorsList);
    }
}
