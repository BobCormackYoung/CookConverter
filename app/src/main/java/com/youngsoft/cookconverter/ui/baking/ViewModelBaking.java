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
    private MutableLiveData<ConversionFactorsRecord> inputBundtConversionFactor;
    private MutableLiveData<ConversionFactorsRecord> outputBundtConversionFactor;
    private MutableLiveData<ConversionFactorsRecord> inputRectangularConversionFactor;
    private MutableLiveData<ConversionFactorsRecord> outputRectangularConversionFactor;
    private MutableLiveData<ConversionFactorsRecord> inputCircularConversionFactor;
    private MutableLiveData<ConversionFactorsRecord> outputCircularConversionFactor;


    //Mediator livedata
    private final MediatorLiveData<Double> outputValue;
    private final MediatorLiveData<Double> areaInput;
    private final MediatorLiveData<Double> areaOutput;
    private final MediatorLiveData<Boolean> isErrorODltIDInput;
    private final MediatorLiveData<Boolean> isErrorIDgtODInput;
    private final MediatorLiveData<Boolean> isErrorODltIDOutput;
    private final MediatorLiveData<Boolean> isErrorIDgtODOutput;
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

        isErrorIDgtODInput = new MediatorLiveData<>();
        mediatorIsErrorIDgtODInputInit();
        isErrorODltIDInput = new MediatorLiveData<>();
        mediatorIsErrorODltIDInputInit();

        isErrorIDgtODOutput = new MediatorLiveData<>();
        mediatorIsErrorIDgtODOutputInit();
        isErrorODltIDOutput = new MediatorLiveData<>();
        mediatorIsErrorODltIDOutputInit();

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
        inputBundtConversionFactor = new MutableLiveData<>();
        outputBundtConversionFactor = new MutableLiveData<>();
        inputRectangularConversionFactor = new MutableLiveData<>();
        outputRectangularConversionFactor = new MutableLiveData<>();
        inputCircularConversionFactor = new MutableLiveData<>();
        outputCircularConversionFactor = new MutableLiveData<>();
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
    public LiveData<Double> getMediatorOutput() {
        return outputValue;
    }
    public LiveData<Boolean> getIsErrorODltIDInput() { return isErrorODltIDInput; }
    public LiveData<Boolean> getIsErrorIDgtODInput() { return isErrorIDgtODInput; }
    public LiveData<Boolean> getIsErrorODltIDOutput() { return isErrorODltIDOutput; }
    public LiveData<Boolean> getIsErrorIDgtODOutput() { return isErrorIDgtODOutput; }
    public LiveData<List<ConversionFactorsRecord>> getConversionFactorsRecordLiveData() { return conversionFactorsRecordLiveData; }

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
    void setInputBundtConversionFactor(ConversionFactorsRecord input) {
        inputBundtConversionFactor.setValue(input);
    }
    void setOutputBundtConversionFactor(ConversionFactorsRecord input) {
        outputBundtConversionFactor.setValue(input);
    }
    void setInputRectangularConversionFactor(ConversionFactorsRecord input) {
        inputRectangularConversionFactor.setValue(input);
    }
    void setOutputRectangularConversionFactor(ConversionFactorsRecord input) {
        outputRectangularConversionFactor.setValue(input);
    }
    void setInputCircularConversionFactor(ConversionFactorsRecord input) {
        inputCircularConversionFactor.setValue(input);
    }
    void setOutputCircularConversionFactor(ConversionFactorsRecord input) {
        outputCircularConversionFactor.setValue(input);
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

    /**
     * add sources for checking whether OD is less than ID
     * only observe changes to OD value - only want to throw error when editing OD value
     */
    private void mediatorIsErrorODltIDOutputInit() {
        isErrorODltIDOutput.addSource(outputBundtPanDimension1, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (outputBundtPanDimension1.getValue() < outputBundtPanDimension2.getValue()) {
                    isErrorODltIDOutput.setValue(true);
                } else {
                    isErrorODltIDOutput.setValue(false);
                }
            }
        });
    }

    /**
     * add sources for checking whether ID is less than OD
     * only observe changes to ID value - only want to throw error when editing ID value
     */
    private void mediatorIsErrorIDgtODOutputInit() {
        isErrorIDgtODOutput.addSource(outputBundtPanDimension2, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (outputBundtPanDimension1.getValue() < outputBundtPanDimension2.getValue()) {
                    isErrorIDgtODOutput.setValue(true);
                } else {
                    isErrorIDgtODOutput.setValue(false);
                }
            }
        });
    }

    /**
     * add sources for checking whether OD is less than ID
     * only observe changes to OD value - only want to throw error when editing OD value
     */
    private void mediatorIsErrorODltIDInputInit() {
        isErrorODltIDInput.addSource(inputBundtPanDimension1, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (inputBundtPanDimension1.getValue() < inputBundtPanDimension2.getValue()) {
                    isErrorODltIDInput.setValue(true);
                } else {
                    isErrorODltIDInput.setValue(false);
                }
            }
        });
    }

    /**
     * add sources for checking whether ID is less than OD
     * only observe changes to ID value - only want to throw error when editing ID value
     */
    private void mediatorIsErrorIDgtODInputInit() {
        isErrorIDgtODInput.addSource(inputBundtPanDimension2, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (inputBundtPanDimension1.getValue() < inputBundtPanDimension2.getValue()) {
                    isErrorIDgtODInput.setValue(true);
                } else {
                    isErrorIDgtODInput.setValue(false);
                }
            }
        });
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
        conversionFactorOutputPan.addSource(outputBundtConversionFactor, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                conversionFactorOutputPan.setValue(calculateOutputConversionFactor());
            }
        });

        //add source for changes to conversion factors
        conversionFactorOutputPan.addSource(outputCircularConversionFactor, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                conversionFactorOutputPan.setValue(calculateOutputConversionFactor());
            }
        });

        //add source for changes to conversion factors
        conversionFactorOutputPan.addSource(outputRectangularConversionFactor, new Observer<ConversionFactorsRecord>() {
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
        if (panTypeOutputMutable.getValue() == 0) {
            if (outputRectangularConversionFactor.getValue() != null) {
                return outputRectangularConversionFactor.getValue().getConversionFactor()*outputRectangularConversionFactor.getValue().getConversionFactor();
            } else {
                return 1.0;
            }
        } else if (panTypeOutputMutable.getValue() == 1) {
            if (outputCircularConversionFactor.getValue() != null) {
                return outputCircularConversionFactor.getValue().getConversionFactor()*outputCircularConversionFactor.getValue().getConversionFactor();
            } else {
                return 1.0;
            }
        } else if (panTypeOutputMutable.getValue() == 2) {
            if (outputBundtConversionFactor.getValue() != null) {
                return outputBundtConversionFactor.getValue().getConversionFactor()*outputBundtConversionFactor.getValue().getConversionFactor();
            } else {
                return 1.0;
            }
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
        conversionFactorInputPan.addSource(inputBundtConversionFactor, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                conversionFactorInputPan.setValue(calculateInputConversionFactor());
            }
        });

        //add source for changes to conversion factors
        conversionFactorInputPan.addSource(inputCircularConversionFactor, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                conversionFactorInputPan.setValue(calculateInputConversionFactor());
            }
        });

        //add source for changes to conversion factors
        conversionFactorInputPan.addSource(inputRectangularConversionFactor, new Observer<ConversionFactorsRecord>() {
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
        if (panTypeInputMutable.getValue() == 0) {
            if (inputRectangularConversionFactor.getValue() != null) {
                return inputRectangularConversionFactor.getValue().getConversionFactor()*inputRectangularConversionFactor.getValue().getConversionFactor();
            } else {
                return 1.0;
            }
        } else if (panTypeInputMutable.getValue() == 1) {
            if (inputCircularConversionFactor.getValue() != null) {
                return inputCircularConversionFactor.getValue().getConversionFactor()*inputCircularConversionFactor.getValue().getConversionFactor();
            } else {
                return 1.0;
            }
        } else if (panTypeInputMutable.getValue() == 2) {
            if (inputBundtConversionFactor.getValue() != null) {
                return inputBundtConversionFactor.getValue().getConversionFactor()*inputBundtConversionFactor.getValue().getConversionFactor();
            } else {
                return 1.0;
            }
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