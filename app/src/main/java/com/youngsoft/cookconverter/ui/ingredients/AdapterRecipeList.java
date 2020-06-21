package com.youngsoft.cookconverter.ui.ingredients;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.data.RecipeList;

import java.text.DecimalFormat;

public class AdapterRecipeList extends ListAdapter<RecipeList, AdapterRecipeList.RecipeListHolder> {


    private OnDeleteClickListener onDeleteClickListener;

    protected AdapterRecipeList(@NonNull DiffUtil.ItemCallback<RecipeList> diffCallback, OnDeleteClickListener onDeleteClickListener) {
        super(diffCallback);
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public RecipeListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_recipe, parent, false);
        return new AdapterRecipeList.RecipeListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListHolder holder, int position) {
        final RecipeList currentItem =getItem(position);
        holder.tvRecipeIngredientName.setText(currentItem.getName());

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");
        holder.tvRecipeIngredientAmount.setText(decimalFormat.format(currentItem.getValue()));

        holder.tvRecipeIngredientUnit.setText(Integer.toString(currentItem.getId()));

        holder.btDeleteSingleIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickListener.onDeleteClick(currentItem.getId());
            }
        });
    }

    public class RecipeListHolder extends RecyclerView.ViewHolder {

        MaterialTextView tvRecipeIngredientName;
        MaterialTextView tvRecipeIngredientAmount;
        MaterialTextView tvRecipeIngredientUnit;
        MaterialButton btDeleteSingleIngredient;

        public RecipeListHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipeIngredientName = itemView.findViewById(R.id.tv_recipe_ingredient_name);
            tvRecipeIngredientAmount = itemView.findViewById(R.id.tv_recipe_amount);
            tvRecipeIngredientUnit = itemView.findViewById(R.id.tv_recipe_units);
            btDeleteSingleIngredient = itemView.findViewById(R.id.bt_delete_single_ingredient);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int index);
    }
}
