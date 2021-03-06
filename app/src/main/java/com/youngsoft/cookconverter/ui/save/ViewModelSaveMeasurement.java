package com.youngsoft.cookconverter.ui.save;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.data.DataRepository;
import com.youngsoft.cookconverter.data.RecipeList;

import java.util.List;

public class ViewModelSaveMeasurement extends AndroidViewModel {

    private final DataRepository dataRepository;

    //1 = measures, 2 = baking, 3 = servings, 4 = ingredients
    public static final int LAUNCH_CASE_MEASUREMENTS = 1;
    public static final int LAUNCH_CASE_BAKING = 2;
    public static final int LAUNCH_CASE_SERVINGS = 3;
    public static final int LAUNCH_CASE_INGREDIENTS = 4;

    //Live Data from Database
    private LiveData<List<ConversionFactorsRecord>> allConversionFactors;

    //Mutabale Live Data
    private MutableLiveData<String> measurementName;
    private MutableLiveData<Double> measurementValue;
    private MutableLiveData<ConversionFactorsRecord> measurementUnit;

    //Mediator Live Data
    private final MediatorLiveData<Boolean> isDataCompleteForSaveMediator;

    public ViewModelSaveMeasurement(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application); //initialise an instance of the repository
        setupLiveData(); //setup the live data
        initLiveData(); //initialise the live data

        //create the mediator filter for the output values
        isDataCompleteForSaveMediator = new MediatorLiveData<>();
        isDataCompleteForSaveMediatorInit();
    }

    /**
     * setup mutable live data
     */
    private void setupLiveData() {
        measurementName = new MutableLiveData<>();
        measurementValue = new MutableLiveData<>();
        measurementUnit = new MutableLiveData<>();
    }

    /**
     * initialise the live data
     */
    private void initLiveData() {
        allConversionFactors = dataRepository.getAllIngredientConversionFactors();
    }

    //getters
    public LiveData<List<ConversionFactorsRecord>> getAllConversionFactors() { return allConversionFactors; }
    public LiveData<String> getMeasurementName() { return measurementName; }
    public LiveData<Double> getMeasurementValue() { return measurementValue; }
    public LiveData<ConversionFactorsRecord> getMeasurementUnit() { return measurementUnit; }
    public LiveData<Boolean> getIsDataCompleteForSave() { return isDataCompleteForSaveMediator; }

    //setters
    public void setMeasurementName(String input) { measurementName.setValue(input); }
    public void setMeasurementValue(Double input) { measurementValue.setValue(input); }
    public void setMeasurementUnit(ConversionFactorsRecord input) { measurementUnit.setValue(input); }

    /**
     * add sources to the mediator live data
     */
    private void isDataCompleteForSaveMediatorInit() {
        //observe the measurement name
        isDataCompleteForSaveMediator.addSource(measurementName, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                isDataCompleteForSaveMediator.setValue(isDataCompleteForSave());
            }
        });

        //observe the output value
        isDataCompleteForSaveMediator.addSource(measurementValue, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                isDataCompleteForSaveMediator.setValue(isDataCompleteForSave());
            }
        });

        //observe the measurement units
        isDataCompleteForSaveMediator.addSource(measurementUnit, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                isDataCompleteForSaveMediator.setValue(isDataCompleteForSave());
            }
        });
    }

    /**
     * check if the data is complete for save
     */
    private Boolean isDataCompleteForSave() {
        //return whether the inputs values are null or not
        return measurementName.getValue() != null &&
                measurementUnit.getValue() != null &&
                measurementValue.getValue() != null;
    }


    /**
     * save this measurement data to the database
     */
    public void saveData() {
        //save the value to the recipe list
        dataRepository.addSingleRecipeList(new RecipeList(measurementName.getValue(), measurementValue.getValue(), measurementUnit.getValue().getConversionFactorID()), measurementUnit.getValue().getConversionFactorID());
    }
}
