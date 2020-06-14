package com.youngsoft.cookconverter.ui.servings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.youngsoft.cookconverter.data.DataRepository;

public class ViewModelServings extends AndroidViewModel {

    private DataRepository dataRepository;

    //Mutable live data values
    private MutableLiveData<Double> inputValue;
    private MutableLiveData<Double> outputValue;
    private MutableLiveData<Integer> inputServingSize;
    private MutableLiveData<Integer> outputServingSize;

    //Mediator live data values
    private MediatorLiveData<Double> mediatorOutput;
    private MediatorLiveData<Double> mediatorConversionFactor;

    public ViewModelServings(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);

        initLiveData();

        //create mediator conversion factor to convert when changing either input or output serving size
        mediatorConversionFactor = new MediatorLiveData<>();
        mediatorConversionFactorInit();

        //create mediator output to convert when changing either input value or conversion value
        mediatorOutput = new MediatorLiveData<>();
        mediatorOutputInit();

    }

    /**
     * initialise the mediator live data for the output
     */
    private void mediatorOutputInit() {

        //add conversion factor as a source for the output
        mediatorOutput.addSource(mediatorConversionFactor, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                mediatorOutput.setValue(calculateOutput());
            }
        });

        //add input value as a source for the output
        mediatorOutput.addSource(inputValue, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                mediatorOutput.setValue(calculateOutput());
            }
        });

    }

    /**
     * calculate the output value
     * @return the output value
     */
    private Double calculateOutput() {

        if (mediatorConversionFactor.getValue() != null && inputValue.getValue() != null) {
            return mediatorConversionFactor.getValue()*inputValue.getValue();
        } else {
            return 0.0;
        }
    }

    /**
     * initialise the mediator live data for the conversion factor
     */
    private void mediatorConversionFactorInit() {

        //add input serving size as a source
        mediatorConversionFactor.addSource(inputServingSize, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mediatorConversionFactor.setValue(calculateConversionFactor());
            }
        });

        //add the output serving size as a source
        mediatorConversionFactor.addSource(outputServingSize, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mediatorConversionFactor.setValue(calculateConversionFactor());
            }
        });

    }

    /**
     * calculate the conversion factor
     * @return the conversion factor
     */
    private Double calculateConversionFactor() {
        if (outputServingSize.getValue() != null || inputServingSize.getValue() != null) {
            return Double.valueOf(outputServingSize.getValue())/Double.valueOf(inputServingSize.getValue());
        } else {
            return 1.0;
        }
    }

    /**
     * initialise the live data
     */
    private void initLiveData() {

        //set initial input value for conversion to 0
        inputValue = new MutableLiveData<>();
        inputValue.setValue(0.0);

        //set initial input serving size
        inputServingSize = new MutableLiveData<>();
        inputServingSize.setValue(1);

        //set initial output serving size
        outputServingSize = new MutableLiveData<>();
        outputServingSize.setValue(1);

    }

    //Getters
    public LiveData<Double> getInputValue() {
        return inputValue;
    }
    public LiveData<Double> getMediatorOutput() {
        return mediatorOutput;
    }

    //Setters
    public void setInputValue(Double input) {
        inputValue.setValue(input);
    }
    public void setInputServingSize(Integer input) {
        inputServingSize.setValue(input);
    }
    public void setOutputServingSize(Integer input) {
        outputServingSize.setValue(input);
    }
}