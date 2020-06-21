package com.youngsoft.cookconverter.ui.ingredients;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.youngsoft.cookconverter.data.DataRepository;
import com.youngsoft.cookconverter.data.RecipeList;

import java.util.List;

public class ViewModelIngredients extends AndroidViewModel {

    private DataRepository dataRepository;

    private LiveData<List<RecipeList>> recipeList;

    public ViewModelIngredients(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
        recipeList = dataRepository.getAllRecipeListRecords();
    }

    public LiveData<List<RecipeList>> getAllRecipeListRecords() { return recipeList; }

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
    public void deleteSingleRecipeList(Integer input) {
        dataRepository.deleteSingleRecipeListItem(input);
    }
}