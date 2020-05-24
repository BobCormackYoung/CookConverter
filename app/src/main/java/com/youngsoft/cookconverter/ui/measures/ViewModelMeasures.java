package com.youngsoft.cookconverter.ui.measures;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.youngsoft.cookconverter.data.DataRepository;

public class ViewModelMeasures extends AndroidViewModel {

    private DataRepository dataRepository;

    private MutableLiveData<Double> inputValue;
    private MutableLiveData<Double> outputValue;
    private MutableLiveData<Double> conversionFactor;
    private MediatorLiveData<Double> mediatorOutput;

    public ViewModelMeasures(@NonNull Application application) {
        super(application);
        inputValue = new MutableLiveData<>();
        inputValue.setValue(0.0);
        outputValue = new MutableLiveData<>();
        outputValue.setValue(0.0);
        conversionFactor = new MutableLiveData<>();
        conversionFactor.setValue(2.0);

        //create mediator output to reactively convert when changing either input value or conversion value
        mediatorOutput = new MediatorLiveData<Double>();
        //observe the input value
        mediatorOutput.addSource(inputValue, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (inputValue.getValue() != null && conversionFactor.getValue() != null) {
                    mediatorOutput.setValue(inputValue.getValue()*conversionFactor.getValue());
                }
            }
        });
        //observe the conversion factor value
        mediatorOutput.addSource(conversionFactor, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (inputValue.getValue() != null && conversionFactor.getValue() != null) {
                    mediatorOutput.setValue(inputValue.getValue()*conversionFactor.getValue());
                }
            }
        });
    }

    public LiveData<Double> getInputValue() {
        return inputValue;
    }

    public LiveData<Double> getOutputValue() {
        return outputValue;
    }

    //set the input value
    public void setInputValueMutable(Double input) {
        inputValue.setValue(input);
    }

    //set the converted value
    public LiveData<Double> getMediatorOutput() {
        return mediatorOutput;
    }
}