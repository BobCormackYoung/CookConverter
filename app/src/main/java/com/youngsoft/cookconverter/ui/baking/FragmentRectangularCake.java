package com.youngsoft.cookconverter.ui.baking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.youngsoft.cookconverter.R;

public class FragmentRectangularCake extends Fragment {

    private ViewModelBaking viewModelBaking;

    FragmentRectangularCake(ViewModelBaking viewModel) {
        viewModelBaking = viewModel;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rectangular_cake, container, false);
        mapViews(root);
        return root;
    }

    private void mapViews(View root) {
        //map views here
    }

}
