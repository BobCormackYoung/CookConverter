package com.youngsoft.cookconverter.ui.baking;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.data.DataRepository;

import java.util.List;

public class ViewModelBaking extends AndroidViewModel {

    private final DataRepository dataRepository;

    private static final Double PI = 3.14159265359;

    //livedata
    private LiveData<List<ConversionFactorsRecord>> conversionFactorsRecordLiveData;

    //Mutable livedata
    private MutableLiveData<Integer> panTypeInputMutable;
    private MutableLiveData<Integer> panTypeOutputMutable;
    private MutableLiveData<Double> inputValueMutable;

    private MutableLiveData<Double> inputPanDimension1;
    private MutableLiveData<Double> inputPanDimension2;
    private MutableLiveData<Double> outputPanDimension1;
    private MutableLiveData<Double> outputPanDimension2;
    private MutableLiveData<ConversionFactorsRecord> inputConversionFactor;
    private MutableLiveData<ConversionFactorsRecord> outputConversionFactor;


    //Mediator livedata
    private final MediatorLiveData<Double> outputValue;
    private final MediatorLiveData<Double> areaInput;
    private final MediatorLiveData<Double> areaOutput;
    private final MediatorLiveData<Double> conversionFactorGlobalOutput;
    private final MediatorLiveData<Double> conversionFactorInputPan;
    private final MediatorLiveData<Double> conversionFactorOutputPan;


    public ViewModelBaking(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);

        setupLiveData();
        initLiveData();

        areaInput = new MediatorLiveData<>();
        mediatorAreaInputInit();
        areaOutput = new MediatorLiveData<>();
        mediatorAreaOutputInit();

        conversionFactorInputPan = new MediatorLiveData<>();
        mediatorConversionFactorInputPanInit();

        conversionFactorOutputPan = new MediatorLiveData<>();
        mediatorConversionFactorOutputPanInit();

        conversionFactorGlobalOutput = new MediatorLiveData<>();
        mediatorConversionFactorInit();

        outputValue = new MediatorLiveData<>();
        mediatorOutputValueInit();
    }

    private void setupLiveData() {
        conversionFactorsRecordLiveData = dataRepository.getAllDistanceConversionFactors();
        panTypeInputMutable = new MutableLiveData<>();
        panTypeOutputMutable = new MutableLiveData<>();
        inputValueMutable = new MutableLiveData<>();
        inputPanDimension1 = new MutableLiveData<>();
        inputPanDimension2 = new MutableLiveData<>();
        outputPanDimension1 = new MutableLiveData<>();
        outputPanDimension2 = new MutableLiveData<>();
        inputConversionFactor = new MutableLiveData<>();
        outputConversionFactor = new MutableLiveData<>();
    }

    private void initLiveData() {
        panTypeInputMutable.setValue(0);
        panTypeOutputMutable.setValue(0);
        inputValueMutable.setValue(0.0);
        inputPanDimension1.setValue(0.0);
        inputPanDimension2.setValue(0.0);
        outputPanDimension1.setValue(0.0);
        outputPanDimension2.setValue(0.0);
        inputConversionFactor.setValue(new ConversionFactorsRecord("m",1,3,-1));
        outputConversionFactor.setValue(new ConversionFactorsRecord("m",1,3,-1));
        //TODO: initialise conversion factors from the conversion factors list
    }

    //getters
    public LiveData<Integer> getPanTypeInputMutable() {
        return panTypeInputMutable;
    }
    public LiveData<Integer> getPanTypeOutputMutable() {
        return panTypeOutputMutable;
    }
    public LiveData<Double> getMediatorOutput() {
        return outputValue;
    }
    public LiveData<Double> getInputPanDimension1() { return inputPanDimension1; }
    public LiveData<Double> getInputPanDimension2() { return inputPanDimension2; }
    public LiveData<Double> getOutputPanDimension1() { return outputPanDimension1; }
    public LiveData<Double> getOutputPanDimension2() { return outputPanDimension2; }
    public LiveData<ConversionFactorsRecord> getInputConversionFactor() { return inputConversionFactor; }
    public LiveData<ConversionFactorsRecord> getOutputConversionFactor() { return outputConversionFactor; }

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
    void setInputPanDimension1(Double input) { inputPanDimension1.setValue(input); }
    void setInputPanDimension2(Double input) { inputPanDimension2.setValue(input); }
    void setOutputPanDimension1(Double input) { outputPanDimension1.setValue(input); }
    void setOutputPanDimension2(Double input) { outputPanDimension2.setValue(input); }
    void setInputConversionFactor(ConversionFactorsRecord input) {
        inputConversionFactor.setValue(input);
    }
    void setOutputConversionFactor(ConversionFactorsRecord input) {
        outputConversionFactor.setValue(input);
    }

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

        //observe changes to the conversion factors
        outputValue.addSource(conversionFactorGlobalOutput, new Observer<Double>() {
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
                return inputValueMutable.getValue()*(areaOutput.getValue()/areaInput.getValue())*conversionFactorGlobalOutput.getValue();
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

        areaOutput.addSource(outputPanDimension1, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                areaOutput.setValue(calculateOutputPanArea());
            }
        });

        areaOutput.addSource(outputPanDimension2, new Observer<Double>() {
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
            //rectangular pan
            return outputPanDimension1.getValue()*outputPanDimension2.getValue();
        } else if (panTypeOutputMutable.getValue() == 1) {
            //circular pan
            return (outputPanDimension1.getValue()/2)*(outputPanDimension1.getValue()/2)*PI;
        } else if (panTypeOutputMutable.getValue() == 2) {
            //bundt pan
            Double areaOuter = (outputPanDimension1.getValue()/2)*(outputPanDimension1.getValue()/2)*PI;
            Double areaInner = (outputPanDimension2.getValue()/2)*(outputPanDimension2.getValue()/2)*PI;
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

        areaInput.addSource(inputPanDimension1, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                areaInput.setValue(calculateInputPanArea());
            }
        });

        areaInput.addSource(inputPanDimension2, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                areaInput.setValue(calculateInputPanArea());
            }
        });
    }

    /**
     * calculate the input value based on which pan type is currently selected
     * @return the pan area
     */
    private Double calculateInputPanArea() {
        if (panTypeInputMutable.getValue() == 0) {
            //rectangular pan
            return inputPanDimension1.getValue()*inputPanDimension2.getValue();
        } else if (panTypeInputMutable.getValue() == 1) {
            //circular pan
            return (inputPanDimension1.getValue()/2)*(inputPanDimension1.getValue()/2)*PI;
        } else if (panTypeInputMutable.getValue() == 2) {
            //bundt pan
            Double areaOuter = (inputPanDimension1.getValue()/2)*(inputPanDimension1.getValue()/2)*PI;
            Double areaInner = (inputPanDimension2.getValue()/2)*(inputPanDimension2.getValue()/2)*PI;
            return areaOuter-areaInner;
        } else {
            return 0.0;
        }
    }

    /**
     * add sources to select the correct conversion factor based on selected pan type
     */
    private void mediatorConversionFactorOutputPanInit() {
        //add sources for output pan type
        conversionFactorOutputPan.addSource(panTypeOutputMutable, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                conversionFactorOutputPan.setValue(calculateOutputConversionFactor());
            }
        });

        //add source for changes to conversion factors
        conversionFactorOutputPan.addSource(outputConversionFactor, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                conversionFactorOutputPan.setValue(calculateOutputConversionFactor());
            }
        });
    }

    /**
     * switch between cases to get the correct conversion factor
     * calculates area conversion factor (i.e. mm^2)
     * @return the conversion factor for the output pan
     */
    private Double calculateOutputConversionFactor() {
            if (outputConversionFactor.getValue() != null) {
                return outputConversionFactor.getValue().getConversionFactor()*outputConversionFactor.getValue().getConversionFactor();
            } else {
                return 1.0;
            }
    }

    /**
     * add sources to select the correct conversion factor based on selected pan type
     */
    private void mediatorConversionFactorInputPanInit() {
        //add sources for output pan type
        conversionFactorInputPan.addSource(panTypeInputMutable, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                conversionFactorInputPan.setValue(calculateInputConversionFactor());
            }
        });

        //add source for changes to conversion factors
        conversionFactorInputPan.addSource(inputConversionFactor, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                conversionFactorInputPan.setValue(calculateInputConversionFactor());
            }
        });
    }

    /**
     * switch between cases to get the correct conversion factor
     * @return the conversion factor for the input pan
     */
    private Double calculateInputConversionFactor() {
            if (inputConversionFactor.getValue() != null) {
                return inputConversionFactor.getValue().getConversionFactor()*inputConversionFactor.getValue().getConversionFactor();
            } else {
                return 1.0;
            }
    }

    /**
     * add sources for checking changes to the cake type and selected conversion factor
     */
    private void mediatorConversionFactorInit() {
        conversionFactorGlobalOutput.addSource(conversionFactorInputPan, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                conversionFactorGlobalOutput.setValue(calculateGlobalOutputConversionFactor());
            }
        });

        conversionFactorGlobalOutput.addSource(conversionFactorOutputPan, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                conversionFactorGlobalOutput.setValue(calculateGlobalOutputConversionFactor());
            }
        });
    }

    /**
     * calculate the global conversion factor
     * @return the global conversion factor
     */
    private Double calculateGlobalOutputConversionFactor() {
        if (conversionFactorOutputPan.getValue() != null
        && conversionFactorInputPan.getValue() != null) {
            return conversionFactorOutputPan.getValue()/conversionFactorInputPan.getValue();
        } else {
            return 1.0;
        }
    }
}