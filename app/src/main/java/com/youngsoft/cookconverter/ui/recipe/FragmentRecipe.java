package com.youngsoft.cookconverter.ui.recipe;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.data.RecipeWithConversionFactor;
import com.youngsoft.cookconverter.ui.util.GlobalFragment;

import java.util.List;

public class FragmentRecipe extends GlobalFragment implements AdapterRecipeList.OnDeleteClickListener {

    private ViewModelRecipe viewModelRecipe;
    private MaterialButton btClearAllData;
    private RecyclerView rvRecipeList;
    private AdapterRecipeList adapterRecipeList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModelRecipe = new ViewModelProvider(this).get(ViewModelRecipe.class);
        View root = inflater.inflate(R.layout.fragment_recipe, container, false);
        mapViews(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvRecipeList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRecipeList.setHasFixedSize(true);

        adapterRecipeList = new AdapterRecipeList(new DiffUtil.ItemCallback<RecipeWithConversionFactor>() {
            @Override
            public boolean areItemsTheSame(@NonNull RecipeWithConversionFactor oldItem, @NonNull RecipeWithConversionFactor newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull RecipeWithConversionFactor oldItem, @NonNull RecipeWithConversionFactor newItem) {
                return false;
            }
        }, this);
        rvRecipeList.setAdapter(adapterRecipeList);

        setListeners();
        setObservers();
    }

    @Override
    public void displayInformationAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                .setMessage(getResources().getString(R.string.info_fragment_recipe))
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

    /**
     * set up livedata observers for the view
     */
    private void setObservers() {
        //observe the list of data
        viewModelRecipe.getAllRecipeWithConversionFactor().observe(getViewLifecycleOwner(), new Observer<List<RecipeWithConversionFactor>>() {
            @Override
            public void onChanged(List<RecipeWithConversionFactor> recipeLists) {
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
                                viewModelRecipe.deleteAllRecipeList();
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
                btPositiveDialog.setTextColor(getResources().getColor(R.color.colorDeleteRed));
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
    public void onDeleteClick(final long index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                .setMessage("Are you sure you'd like to delete this single ingredient?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewModelRecipe.deleteSingleRecipeList(index);
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
        btPositiveDialog.setTextColor(getResources().getColor(R.color.colorDeleteRed));
    }
}