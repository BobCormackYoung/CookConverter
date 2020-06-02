package com.youngsoft.cookconverter.ui.baking;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.youngsoft.cookconverter.data.DataRepository;

public class ViewModelBaking extends AndroidViewModel {

    private DataRepository dataRepository;

    //Mutable livedata
    private MutableLiveData<Integer> panTypeInputMutable;
    private MutableLiveData<Integer> panTypeOutputMutable;
    private MutableLiveData<Double> inputValueMutable;
    private MutableLiveData<Double> inputRectangularPanDimension1;
    private MutableLiveData<Double> inputRectangularPanDimension2;
    private MutableLiveData<Double> inputCircularPanDimension;
    private MutableLiveData<Double> inputBundtPanDimension1;
    private MutableLiveData<Double> inputBundtPanDimension2;
    private MutableLiveData<Double> outputRectangularPanDimension1;
    private MutableLiveData<Double> outputRectangularPanDimension2;
    private MutableLiveData<Double> outputCircularPanDimension;
    private MutableLiveData<Double> outputBundtPanDimension1;
    private MutableLiveData<Double> outputBundtPanDimension2;

    //Mediator livedata
    private MediatorLiveData<Double> outputValue;

    public ViewModelBaking(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);

        setupLiveData();
        initLiveData();

    }

    private void setupLiveData() {
        panTypeInputMutable = new MutableLiveData<>();
        panTypeOutputMutable = new MutableLiveData<>();
        inputValueMutable = new MutableLiveData<>();
        inputRectangularPanDimension1 = new MutableLiveData<>();
        inputRectangularPanDimension2 = new MutableLiveData<>();
        inputCircularPanDimension = new MutableLiveData<>();
        inputBundtPanDimension1 = new MutableLiveData<>();
        inputBundtPanDimension2 = new MutableLiveData<>();
        outputRectangularPanDimension1 = new MutableLiveData<>();
        outputRectangularPanDimension2 = new MutableLiveData<>();
        outputCircularPanDimension = new MutableLiveData<>();
        outputBundtPanDimension1 = new MutableLiveData<>();
        outputBundtPanDimension2 = new MutableLiveData<>();
    }

    private void initLiveData() {
        panTypeInputMutable.setValue(0);
        panTypeOutputMutable.setValue(0);
        inputValueMutable.setValue(0.0);
    }

    //getters
    public LiveData<Integer> getPanTypeInputMutable() {
        return panTypeInputMutable;
    }
    public LiveData<Integer> getPanTypeOutputMutable() {
        return panTypeOutputMutable;
    }

    //setters
    void setPanTypeInputMutable(Integer input) {
        panTypeInputMutable.setValue(input);
    }
    void setPanTypeOutputMutable(Integer input) {
        panTypeOutputMutable.setValue(input);
    }
    void setInputValueMutable(Double input) {
        inputValueMutable.setValue(input);
    }
}