package com.youngsoft.cookconverter.ui.ingredients;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.youngsoft.cookconverter.data.DataRepository;

public class ViewModelIngredients extends AndroidViewModel {

    private final DataRepository dataRepository;

    //Mutable livedata
    private MutableLiveData<Double> inputValueMutable;
    private MutableLiveData<Double> inputRecipeValue;
    private MutableLiveData<Double> inputUserValue;
    private MutableLiveData<Double> scaleFactor;

    //Mediator livedata
    private final MediatorLiveData<Double> outputValue;

    public ViewModelIngredients(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);

        setupLiveData();
        initLiveData();

        outputValue = new MediatorLiveData<>();

        mediatorOutputValueInit();
    }

    private void setupLiveData() {
        inputValueMutable = new MutableLiveData<>();
        inputRecipeValue = new MutableLiveData<>();
        inputUserValue = new MutableLiveData<>();
        scaleFactor = new MutableLiveData<>();
    }

    private void initLiveData() {
        inputValueMutable.setValue(0.0);
        inputRecipeValue.setValue(1.0);
        inputUserValue.setValue(1.0);
        scaleFactor.setValue(1.0);
    }

    //getters
    public LiveData<Double> getMediatorOutput() { return outputValue; }
    public LiveData<Double> getScaleFactor() { return scaleFactor; }
    public LiveData<Double> getInputValueMutable() { return inputValueMutable; }
    public LiveData<Double> getInputRecipeValue() { return inputRecipeValue; }
    public LiveData<Double> getInputUserValue() { return inputUserValue; }

    //setters
    void setInputValueMutable(Double input) { inputValueMutable.setValue(input); }
    void setInputRecipeValue(Double input) { inputRecipeValue.setValue(input); }
    void setInputUserValue(Double input) { inputUserValue.setValue(input); }
    void setScaleFactor(Double input) { scaleFactor.setValue(input); }

    /**
     * setup the sources for calculating the output value to trigger recalculation
     */
    private void mediatorOutputValueInit() {
        outputValue.setValue(0.0); //initial value for the output

        outputValue.addSource(inputValueMutable, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (inputValueMutable.getValue() != null && scaleFactor.getValue() != null) {
                    outputValue.setValue(calculateOutputValue(inputValueMutable.getValue(), scaleFactor.getValue()));
                } else {
                    outputValue.setValue(0.0);
                }
            }
        });

        outputValue.addSource(scaleFactor, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (inputValueMutable.getValue() != null && scaleFactor.getValue() != null) {
                    outputValue.setValue(calculateOutputValue(inputValueMutable.getValue(), scaleFactor.getValue()));
                } else {
                    outputValue.setValue(0.0);
                }
            }
        });

    }

    /**
     * calculate the output value
     * @param inputValue the input value that the user wants to scale from
     * @param scaleFactor the scale factor to be applied
     * @return the calculated output value
     */
    private Double calculateOutputValue(Double inputValue, Double scaleFactor) {
        return inputValue*scaleFactor;
    }
}
