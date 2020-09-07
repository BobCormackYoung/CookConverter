package com.youngsoft.cookconverter.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executors;

public class DataRepository {

    private final String TAG = "DataRepository";
    private final DataDao dataDao;

    //private final LiveData<List<ConversionFactorsRecord>> allConversionFactorsRecords;
    private final LiveData<List<IngredientsRecord>> allIngredientsRecords;
    //private final LiveData<List<PanTypeRecord>> allPanTypeRecords;
    private final LiveData<List<ConversionFactorsRecord>> allMassVolumeConversionFactors;
    private final LiveData<List<ConversionFactorsRecord>> allMassConversionFactors;
    private final LiveData<List<ConversionFactorsRecord>> allVolumeConversionFactors;
    private final LiveData<List<ConversionFactorsRecord>> allDistanceConversionFactors;
    private final LiveData<List<RecipeList>> allRecipeList;
    private final LiveData<List<RecipeWithConversionFactor>> allRecipeWithConversionFactor;

    public DataRepository(Application application) {
        DataDatabase dataDatabase = DataDatabase.getInstance(application);
        dataDao = dataDatabase.dataDao();
        //allConversionFactorsRecords = dataDao.getAllConversionFactorsRecordsSortById();
        allMassVolumeConversionFactors = dataDao.getAllMassVolumeConversionFactors();
        allMassConversionFactors = dataDao.getAllMassConversionFactors();
        allVolumeConversionFactors = dataDao.getAllVolumeConversionFactors();
        allIngredientsRecords = dataDao.getAllIngredientsRecordsSortById();
        //allPanTypeRecords = dataDao.getAllPanTypeRecordsSortById();
        allDistanceConversionFactors = dataDao.getAllDistanceConversionFactors();
        allRecipeList = dataDao.getAllRecipeListSortById();
        allRecipeWithConversionFactor = dataDao.getRecipeWithConversionFactor();
    }

    public LiveData<List<RecipeWithConversionFactor>> getAllRecipeWithConversionFactor() {
        return allRecipeWithConversionFactor;
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

    /**
     * Simplified method to get a subset list of conversion factors based on type
     * @param conversionFactorType where 1 = mass, 2 = volume, 3 = distance, 4  = pieces
     * @return livedata list of ConversionFactorRecords
     */
    public LiveData<List<ConversionFactorsRecord>> getSimpleSubsetConversionFactors(int conversionFactorType) {
        return dataDao.getSubsetConversionFactors(conversionFactorType);
    }

    public void addSingleRecipeList(final RecipeList recipeList, final long conversionFactorId) {
        //run the task to add data asynchronously
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                long newRecipeListID = dataDao.insertSingleRecipeListRecords(recipeList);
                dataDao.insertRecipeConversionFactorCrossReference(new RecipeConversionFactorCrossReference(newRecipeListID, conversionFactorId));
            }
        });
    }

    public void deleteAllRecipeListItems() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                dataDao.deleteAllRecipeListItems();
                dataDao.RecipeConversionFactorCrossReference();
            }
        });
    }

    public void deleteSingleRecipeListItem(final Long input) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                dataDao.deleteSingleRecipeListItem(input);
                dataDao.deleteSingleRecipeConversionFactorCrossReference(input);
            }
        });
    }

    public LiveData<ConversionFactorsRecord> getSingleConversionFactor(int id) {
        return dataDao.getSingleConversionFactor(id);
    }
}
