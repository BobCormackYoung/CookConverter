package com.youngsoft.cookconverter.ui.ingredients;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.youngsoft.cookconverter.data.DataRepository;
import com.youngsoft.cookconverter.data.RecipeList;
import com.youngsoft.cookconverter.data.RecipeWithConversionFactor;

import java.util.List;

public class ViewModelIngredients extends AndroidViewModel {

    private final DataRepository dataRepository;

    private final LiveData<List<RecipeList>> recipeList;
    private final LiveData<List<RecipeWithConversionFactor>> allRecipeWithConversionFactor;

    public ViewModelIngredients(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
        recipeList = dataRepository.getAllRecipeListRecords();
        allRecipeWithConversionFactor = dataRepository.getAllRecipeWithConversionFactor();
    }

    public LiveData<List<RecipeList>> getAllRecipeListRecords() { return recipeList; }

    public LiveData<List<RecipeWithConversionFactor>> getAllRecipeWithConversionFactor() { return allRecipeWithConversionFactor; }

    /**
     * delete all recipe list items in the database
     */
    public void deleteAllRecipeList() {
        dataRepository.deleteAllRecipeListItems();
    }

    /**
     * delete a single recipe list item entry
     * @param input integer for the record id to be deleted
     */
    public void deleteSingleRecipeList(Long input) {
        dataRepository.deleteSingleRecipeListItem(input);
    }
}