package com.youngsoft.cookconverter.ui.baking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.youngsoft.cookconverter.R;

public class FragmentCircularCake extends Fragment {

    private ViewModelBaking viewModelBaking;
    private Spinner spFCCUnits;
    private TextInputEditText etDimension;

    FragmentCircularCake(ViewModelBaking viewModel) {
        viewModelBaking = viewModel;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_circular_cake, container, false);
        mapViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void mapViews(View root) {
        spFCCUnits = root.findViewById(R.id.sp_fcc_input_units);
        etDimension = root.findViewById(R.id.tiet_fcc_od_input);
    }
}
