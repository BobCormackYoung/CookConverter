package com.youngsoft.cookconverter.ui.baking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.youngsoft.cookconverter.R;

public class FragmentBundtCake extends Fragment {

    ViewModelBaking viewModelBaking;

    public FragmentBundtCake(ViewModelBaking viewModel) {
        viewModelBaking = viewModel;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bundt_cake, container, false);
        return root;
    }




}
