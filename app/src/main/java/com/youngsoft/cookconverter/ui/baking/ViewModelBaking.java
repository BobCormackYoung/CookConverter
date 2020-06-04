package com.youngsoft.cookconverter.ui.baking;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.youngsoft.cookconverter.data.DataRepository;

public class ViewModelBaking extends AndroidViewModel {

    private DataRepository dataRepository;

    private static Double PI = 3.14159265359;

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
    private MediatorLiveData<Double> areaInput;
    private MediatorLiveData<Double> areaOutput;

    public ViewModelBaking(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);

        setupLiveData();
        initLiveData();

        areaInput = new MediatorLiveData<>();
        mediatorAreaInputInit();
        areaOutput = new MediatorLiveData<>();
        mediatorAreaOutputInit();

        outputValue = new MediatorLiveData<>();
        mediatorOutputValueInit();
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
        inputRectangularPanDimension1.setValue(0.0);
        inputRectangularPanDimension2.setValue(0.0);
        inputCircularPanDimension.setValue(0.0);
        inputBundtPanDimension1.setValue(0.0);
        inputBundtPanDimension2.setValue(0.0);
        outputRectangularPanDimension1.setValue(0.0);
        outputRectangularPanDimension2.setValue(0.0);
        outputCircularPanDimension.setValue(0.0);
        outputBundtPanDimension1.setValue(0.0);
        outputBundtPanDimension2.setValue(0.0);
    }

    //getters
    public LiveData<Integer> getPanTypeInputMutable() {
        return panTypeInputMutable;
    }
    public LiveData<Integer> getPanTypeOutputMutable() {
        return panTypeOutputMutable;
    }
    public LiveData<Double> getOutputValue() {
        return outputValue;
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
    void setInputRectangularPanDimension1(Double input) { inputRectangularPanDimension1.setValue(input); }
    void setInputRectangularPanDimension2(Double input) { inputRectangularPanDimension2.setValue(input); }
    void setInputCircularPanDimension (Double input) { inputCircularPanDimension.setValue(input); }
    void setInputBundtPanDimension1(Double input) { inputBundtPanDimension1.setValue(input); }
    void setInputBundtPanDimension2(Double input) { inputBundtPanDimension2.setValue(input); }
    void setOutputRectangularPanDimension1(Double input) { outputRectangularPanDimension1.setValue(input); }
    void setOutputRectangularPanDimension2(Double input) { outputRectangularPanDimension2.setValue(input); }
    void setOutputCircularPanDimension(Double input) { outputCircularPanDimension.setValue(input); }
    void setOutputBundtPanDimension1(Double input) { outputBundtPanDimension1.setValue(input); }
    void setOutputBundtPanDimension2(Double input) { outputBundtPanDimension2.setValue(input); }

    /**
     * setup the sources for the output value to trigger recalculation
     */
    private void mediatorOutputValueInit() {
        //observe the input
        outputValue.addSource(inputValueMutable, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                outputValue.setValue(calculateOutputValue());
            }
        });

        //observe changes to the input and output areas
        outputValue.addSource(areaOutput, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                outputValue.setValue(calculateOutputValue());
            }
        });
        outputValue.addSource(areaInput, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                outputValue.setValue(calculateOutputValue());
            }
        });
    }

    /**
     * calculate the output value
     * @return the converted output value
     */
    private Double calculateOutputValue() {
        if (areaInput.getValue() == null || areaOutput.getValue() == null) {
            return 0.0;
        } else {
            if (areaInput.getValue() != 0.0 && areaOutput.getValue() != 0.0) {
                return inputValueMutable.getValue()*areaOutput.getValue()/areaInput.getValue();
            } else {
                return 0.0;
            }
        }
    }


    /**
     * setup the mediator sources for the output area
     */
    private void mediatorAreaOutputInit() {
        areaOutput.addSource(panTypeOutputMutable, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                areaOutput.setValue(calculateOutputPanArea());
            }
        });

        areaOutput.addSource(outputRectangularPanDimension1, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                areaOutput.setValue(calculateOutputPanArea());
            }
        });

        areaOutput.addSource(outputRectangularPanDimension2, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                areaOutput.setValue(calculateOutputPanArea());
            }
        });

        areaOutput.addSource(outputCircularPanDimension, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                areaOutput.setValue(calculateOutputPanArea());
            }
        });

        areaOutput.addSource(outputBundtPanDimension1, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                areaOutput.setValue(calculateOutputPanArea());
            }
        });

        areaOutput.addSource(outputBundtPanDimension2, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                areaOutput.setValue(calculateOutputPanArea());
            }
        });
    }

    /**
     * calculate the output value based on which pan type is currently selected
     * @return the pan area
     */
    private Double calculateOutputPanArea() {
        if (panTypeOutputMutable.getValue() == 0) {
            return outputRectangularPanDimension1.getValue()*outputRectangularPanDimension2.getValue();
        } else if (panTypeOutputMutable.getValue() == 1) {
            return (outputCircularPanDimension.getValue()/2)*(outputCircularPanDimension.getValue()/2)*PI;
        } else if (panTypeOutputMutable.getValue() == 2) {
            Double areaOuter = (outputBundtPanDimension1.getValue()/2)*(outputBundtPanDimension1.getValue()/2)*PI;
            Double areaInner = (outputBundtPanDimension2.getValue()/2)*(outputBundtPanDimension2.getValue()/2)*PI;
            return areaOuter-areaInner;
        } else {
            return 0.0;
        }
    }

    /**
     * setup the mediator sources for the input area
     */
    private void mediatorAreaInputInit() {
        areaInput.addSource(panTypeInputMutable, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                areaInput.setValue(calculateInputPanArea());
            }
        });

        areaInput.addSource(inputRectangularPanDimension1, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                areaInput.setValue(calculateInputPanArea());
            }
        });

        areaInput.addSource(inputRectangularPanDimension2, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                areaInput.setValue(calculateInputPanArea());
            }
        });

        areaInput.addSource(inputCircularPanDimension, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                areaInput.setValue(calculateInputPanArea());
            }
        });

        areaInput.addSource(inputBundtPanDimension1, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                areaInput.setValue(calculateInputPanArea());
            }
        });

        areaInput.addSource(inputBundtPanDimension2, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                areaInput.setValue(calculateInputPanArea());
            }
        });
    }

    /**
     * calculate the output value based on which pan type is currently selected
     * @return the pan area
     */
    private Double calculateInputPanArea() {
        if (panTypeInputMutable.getValue() == 0) {
            return inputRectangularPanDimension1.getValue()*inputRectangularPanDimension2.getValue();
        } else if (panTypeInputMutable.getValue() == 1) {
            return (inputCircularPanDimension.getValue()/2)*(inputCircularPanDimension.getValue()/2)*PI;
        } else if (panTypeInputMutable.getValue() == 2) {
            Double areaOuter = (inputBundtPanDimension1.getValue()/2)*(inputBundtPanDimension1.getValue()/2)*PI;
            Double areaInner = (inputBundtPanDimension2.getValue()/2)*(inputBundtPanDimension2.getValue()/2)*PI;
            return areaOuter-areaInner;
        } else {
            return 0.0;
        }
    }
}