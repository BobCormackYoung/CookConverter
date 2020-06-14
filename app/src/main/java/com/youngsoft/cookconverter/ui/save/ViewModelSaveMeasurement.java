package com.youngsoft.cookconverter.ui.save;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.data.DataRepository;

import java.util.List;

public class ViewModelSaveMeasurement extends AndroidViewModel {

    private DataRepository dataRepository;

    //Live Data from Database
    private LiveData<List<ConversionFactorsRecord>> allConversionFactors;

    //Mutabale Life Data
    private MutableLiveData<String> measurementName;
    private MutableLiveData<Double> measurementValue;
    private MutableLiveData<Integer> measurementUnit;

    public ViewModelSaveMeasurement(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
        setupLiveData();
        allConversionFactors = dataRepository.getAllMassVolumeConversionFactors();
    }

    private void setupLiveData() {
        measurementName = new MutableLiveData<>();
        measurementValue = new MutableLiveData<>();
        measurementUnit = new MutableLiveData<>();
    }

    public LiveData<List<ConversionFactorsRecord>> getAllConversionFactors() {
        return allConversionFactors;
    }
    public MutableLiveData<String> getMeasurementName() {
        return measurementName;
    }
    public MutableLiveData<Double> getMeasurementValue() {
        return measurementValue;
    }
    public MutableLiveData<Integer> getMeasurementUnit() {
        return measurementUnit;
    }


    public void setMeasurementName(String input) {
        measurementName.setValue(input);
    }
    public void setMeasurementValue(Double input) {
        measurementValue.setValue(input);
    }
    public void setMeasurementUnit(Integer input) {
        measurementUnit.setValue(input);
    }
}
