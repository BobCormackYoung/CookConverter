package com.youngsoft.cookconverter.ui.ingredients;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.data.RecipeList;

import java.util.List;
import java.util.Objects;

public class FragmentIngredients extends Fragment implements AdapterRecipeList.OnDeleteClickListener {

    private ViewModelIngredients viewModelIngredients;
    private MaterialButton btClearAllData;
    private RecyclerView rvRecipeList;
    private AdapterRecipeList adapterRecipeList;

    public static FragmentIngredients newInstance() {
        return new FragmentIngredients();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModelIngredients = ViewModelProviders.of(this).get(ViewModelIngredients.class);
        View root = inflater.inflate(R.layout.fragment_ingredients, container, false);
        mapViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvRecipeList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRecipeList.setHasFixedSize(true);

        adapterRecipeList = new AdapterRecipeList(new DiffUtil.ItemCallback<RecipeList>() {
            @Override
            public boolean areItemsTheSame(@NonNull RecipeList oldItem, @NonNull RecipeList newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull RecipeList oldItem, @NonNull RecipeList newItem) {
                return false;
            }
        }, this);
        rvRecipeList.setAdapter(adapterRecipeList);

        setListeners();
        setObservers();
    }

    /**
     * set up livedata observers for the view
     */
    private void setObservers() {
        //observe the list of data
        viewModelIngredients.getAllRecipeListRecords().observe(getViewLifecycleOwner(), new Observer<List<RecipeList>>() {
            @Override
            public void onChanged(List<RecipeList> recipeLists) {
                adapterRecipeList.submitList(recipeLists);
                adapterRecipeList.notifyDataSetChanged();
            }
        });
    }

    /**
     * setup listeners for the view
     */
    private void setListeners() {
        //set listener for the button to delete all data
        btClearAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an alert dialog to check to make sure they they would like to delete everything
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                        .setMessage("Are you sure you'd like to delete the entire ingredients list?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                viewModelIngredients.deleteAllRecipeList();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Do nothing
                            }
                        });
                builder.create().show();
            }
        });
    }

    /**
     * map fragment views
     * @param root the layout that contains the views to be mapped
     */
    private void mapViews(View root) {
        btClearAllData = root.findViewById(R.id.bt_clear_recipe_list);
        rvRecipeList = root.findViewById(R.id.rv_recipe_list);
    }

    @Override
    public void onDeleteClick(final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                .setMessage("Are you sure you'd like to delete this single ingredient?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewModelIngredients.deleteSingleRecipeList(index);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Do nothing
                    }
                });

        builder.create().show();
    }
}