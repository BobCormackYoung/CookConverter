package com.youngsoft.cookconverter.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DataRepository {

    private final String TAG = "DataRepository";
    private DataDao dataDao;

    private LiveData<List<ConversionFactorsRecord>> allConversionFactorsRecords;
    private LiveData<List<IngredientsRecord>> allIngredientsRecords;
    private LiveData<List<PanTypeRecord>> allPanTypeRecords;
    private LiveData<List<ConversionFactorsRecord>> subsetConversionFactors;

    public DataRepository(Application application) {
        DataDatabase dataDatabase = DataDatabase.getInstance(application);
        dataDao = dataDatabase.dataDao();
        allConversionFactorsRecords = dataDao.getAllConversionFactorsRecordsSortById();
        allIngredientsRecords = dataDao.getAllIngredientsRecordsSortById();
        allPanTypeRecords = dataDao.getAllPanTypeRecordsSortById();
    }

    public LiveData<List<ConversionFactorsRecord>> getAllConversionFactorsRecords() {
        return allConversionFactorsRecords;
    }

    public LiveData<List<IngredientsRecord>> getAllIngredientsRecords() {
        return allIngredientsRecords;
    }

    public LiveData<List<PanTypeRecord>> getAllPanTypeRecords() {
        return allPanTypeRecords;
    }

    public LiveData<List<ConversionFactorsRecord>> getSubsetConversionFactors(ConversionFactorsRecord inputConversionFactor, IngredientsRecord inputIngredients) {
        if (inputIngredients.getType() == 0) {
            return dataDao.getSubsetConversionFactors(inputConversionFactor.getType());
        } else {
            return allConversionFactorsRecords;
        }
    }
}
