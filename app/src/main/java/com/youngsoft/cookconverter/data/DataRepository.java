package com.youngsoft.cookconverter.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executors;

public class DataRepository {

    private final String TAG = "DataRepository";
    private DataDao dataDao;

    private LiveData<List<ConversionFactorsRecord>> allConversionFactorsRecords;
    private LiveData<List<IngredientsRecord>> allIngredientsRecords;
    private LiveData<List<PanTypeRecord>> allPanTypeRecords;
    private LiveData<List<ConversionFactorsRecord>> allMassVolumeConversionFactors;
    private LiveData<List<ConversionFactorsRecord>> allDistanceConversionFactors;
    private LiveData<List<RecipeList>> allRecipeList;

    public DataRepository(Application application) {
        DataDatabase dataDatabase = DataDatabase.getInstance(application);
        dataDao = dataDatabase.dataDao();
        allConversionFactorsRecords = dataDao.getAllConversionFactorsRecordsSortById();
        allMassVolumeConversionFactors = dataDao.getAllMassVolumeConversionFactors();
        allIngredientsRecords = dataDao.getAllIngredientsRecordsSortById();
        allPanTypeRecords = dataDao.getAllPanTypeRecordsSortById();
        allDistanceConversionFactors = dataDao.getAllDistanceConversionFactors();
        allRecipeList = dataDao.getAllRecipeListSortById();
    }

    public LiveData<List<ConversionFactorsRecord>> getAllConversionFactorsRecords() {
        return allConversionFactorsRecords;
    }

    public LiveData<List<ConversionFactorsRecord>> getAllDistanceConversionFactors() {
        return allDistanceConversionFactors;
    }

    public LiveData<List<ConversionFactorsRecord>> getAllMassVolumeConversionFactors() {
        return allMassVolumeConversionFactors;
    }


    public LiveData<List<IngredientsRecord>> getAllIngredientsRecords() {
        return allIngredientsRecords;
    }

    public LiveData<List<RecipeList>> getAllRecipeListRecords() {
        return allRecipeList;
    }

    public LiveData<List<PanTypeRecord>> getAllPanTypeRecords() {
        return allPanTypeRecords;
    }

    public LiveData<List<ConversionFactorsRecord>> getSubsetConversionFactors(ConversionFactorsRecord inputConversionFactor, IngredientsRecord inputIngredients) {
        if (inputIngredients.getType() == 0) {
            //if no ingredient selected, return only a subset of conversion factors of the same type as the selected type
            return dataDao.getSubsetConversionFactors(inputConversionFactor.getType());
        } else {
            if (inputConversionFactor.getType() == 4) {
                //if the selected conversion factor is "pieces", allow only pieces to be returned
                //cannot convert from pieces to kilograms for example
                return dataDao.getSubsetConversionFactors(inputConversionFactor.getType());
            } else {
                //if a different type of conversion factor, allow conversion to mass of volume
                return allMassVolumeConversionFactors;
            }
        }
    }

    public void addSingleRecipeList(final RecipeList recipeList) {
        //run the task to add data asynchronously
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                dataDao.insertMultipleRecipeListRecords(recipeList);
            }
        });
    }

    public void deleteAllRecipeListItems() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                dataDao.deleteAllRecipeListItems();
            }
        });
    }

    public void deleteSingleRecipeListItem(final Integer input) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                dataDao.deleteSingleRecipeListItem(input);
            }
        });
    }
}
