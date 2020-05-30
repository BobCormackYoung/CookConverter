package com.youngsoft.cookconverter.ui.measures;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.data.DataRepository;
import com.youngsoft.cookconverter.data.IngredientsRecord;

import java.util.List;

public class ViewModelMeasures extends AndroidViewModel {

    private DataRepository dataRepository;

    //Live Data from Database
    private LiveData<List<ConversionFactorsRecord>> allConversionFactors;
    private LiveData<List<IngredientsRecord>> allIngredients;
    private LiveData<List<ConversionFactorsRecord>> subsetConversionFactorList;

    //Mutable live data values
    private MutableLiveData<Double> inputValue;
    private MutableLiveData<Double> outputValue;
    private MutableLiveData<ConversionFactorsRecord> conversionFactorInputID;
    private MutableLiveData<ConversionFactorsRecord> conversionFactorOutputID;
    private MutableLiveData<IngredientsRecord> ingredientSelected;

    //Mediator live data values
    private MediatorLiveData<Double> mediatorOutput;
    private MediatorLiveData<Double> mediatorConversionFactor;
    private MediatorLiveData<SubsetConversionFactorFilter> mediatorSubsetConversionFactorFilter;


    //Constructor
    public ViewModelMeasures(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
        allConversionFactors = dataRepository.getAllConversionFactorsRecords();
        allIngredients = dataRepository.getAllIngredientsRecords();

        //set initial input value for conversion to 0
        inputValue = new MutableLiveData<>();
        inputValue.setValue(0.0);

        //set initial output value from conversion to 0
        outputValue = new MutableLiveData<>();
        outputValue.setValue(0.0);

        //set initial ingredient
        ingredientSelected = new MutableLiveData<>();

        //set initial input/output conversion values
        conversionFactorInputID = new MutableLiveData<>();
        conversionFactorOutputID = new MutableLiveData<>();
        //conversionFactorInputID.setValue(new ConversionFactorsRecord("dummy",1,-1,-1));
        //conversionFactorOutputID.setValue(new ConversionFactorsRecord("dummy",1,-1, -1));

        //create the mediator filter for the output values
        mediatorSubsetConversionFactorFilter = new MediatorLiveData<>();
        mediatorSubsetFilterInit();

        //create mediator conversion factor to convert when changing either input type or output type
        mediatorConversionFactor = new MediatorLiveData<>();
        mediatorConversionFactorInit();

        //create mediator output to convert when changing either input value or conversion value
        mediatorOutput = new MediatorLiveData<>();
        mediatorOutputInit();

        //create the mediator list of allowable output types that can be chosen
        subsetConversionFactorList = Transformations.switchMap(mediatorSubsetConversionFactorFilter, new Function<SubsetConversionFactorFilter, LiveData<List<ConversionFactorsRecord>>>() {
            @Override
            public LiveData<List<ConversionFactorsRecord>> apply(SubsetConversionFactorFilter input) {
                Log.i("VMM","Transformation, CFR = " + input.conversionFactorsRecord.getName() + " IR = " + input.ingredientsRecord.getName());
                return dataRepository.getSubsetConversionFactors(input.conversionFactorsRecord, input.ingredientsRecord);
            }
        });

    }







    //get the converted value
    public LiveData<Double> getMediatorOutput() {
        return mediatorOutput;
    }

    //return list of all conversion factor records
    public LiveData<List<ConversionFactorsRecord>> getAllConversionFactors() {
        return allConversionFactors;
    }

    //return list of all ingredient records
    public LiveData<List<IngredientsRecord>> getAllIngredients() {
        return allIngredients;
    }

    //return list of all ingredient records
    public LiveData<SubsetConversionFactorFilter> getSubsetConversionFactorFilter() {
        return mediatorSubsetConversionFactorFilter;
    }

    //return list of subset conversion factor records
    public LiveData<List<ConversionFactorsRecord>> getSubsetConversionFactors() {
        return subsetConversionFactorList;
    }




    //set the input value
    public void setInputValueMutable(Double input) {
        inputValue.setValue(input);
    }

    //set the picked input conversion factor ID
    public void setConversionFactorInputID(ConversionFactorsRecord input) {
        conversionFactorInputID.setValue(input);
    }

    //set the picked output conversion factor ID
    public void setConversionFactorOutputID(ConversionFactorsRecord input) {
        conversionFactorOutputID.setValue(input);
    }

    //set the selected ingredient
    public void setIngredientSelected(IngredientsRecord input) {
        Log.i("VMM","setIngredientSelected " + input.getName());
        ingredientSelected.setValue(input);
    }








    /**
     * setup the observers for the output value mediator that will calculate the output value based on input & conversion factor
     */
    private void mediatorOutputInit() {
        //observe the input value
        mediatorOutput.addSource(inputValue, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (inputValue.getValue() != null && mediatorConversionFactor.getValue() != null) {
                    mediatorOutput.setValue(calculateOutputValue());
                }
            }
        });
        //observe the conversion factor value
        mediatorOutput.addSource(mediatorConversionFactor, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (inputValue.getValue() != null && mediatorConversionFactor.getValue() != null) {
                    mediatorOutput.setValue(calculateOutputValue());
                }
            }
        });
    }

    private Double calculateOutputValue() {
        Log.i("VMM","Input = " + inputValue.getValue() + " Conversion = " + mediatorConversionFactor.getValue() + " Output = " + inputValue.getValue()* mediatorConversionFactor.getValue());
        return inputValue.getValue()* mediatorConversionFactor.getValue();
    }

    /**
     * setup the observers for the conversion factor mediator value
     */
    private void mediatorConversionFactorInit() {
        //observe the input type
        mediatorConversionFactor.addSource(conversionFactorInputID, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord input) {
                if (conversionFactorInputID.getValue() != null && conversionFactorOutputID.getValue() != null && ingredientSelected.getValue() != null) {
                    //calculate the conversion factor
                    mediatorConversionFactor.setValue(calculateConversionFactor());
                }
            }
        });
        //observe the output type
        mediatorConversionFactor.addSource(conversionFactorOutputID, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord input) {
                if (conversionFactorInputID.getValue() != null && conversionFactorOutputID.getValue() != null && ingredientSelected.getValue() != null) {
                    //calculate the conversion factor
                    mediatorConversionFactor.setValue(calculateConversionFactor());
                }
            }
        });
        //observe changes to the selected ingredient
        mediatorConversionFactor.addSource(ingredientSelected, new Observer<IngredientsRecord>() {
            @Override
            public void onChanged(IngredientsRecord ingredientsRecord) {
                if (conversionFactorInputID.getValue() != null && conversionFactorOutputID.getValue() != null && ingredientSelected.getValue() != null) {
                    //calculate the conversion factor
                    mediatorConversionFactor.setValue(calculateConversionFactor());
                }
            }
        });
    }

    private Double calculateConversionFactor() {
        Log.i("VMM","Conversion Factor = " + conversionFactorInputID.getValue().getConversionFactor()/conversionFactorOutputID.getValue().getConversionFactor());

        //check if input and output data types are the same (i.e. both mass or both volume
        if (conversionFactorInputID.getValue().getType() == conversionFactorOutputID.getValue().getType()) {
            Log.i("VMM","conversionFactorInputID type == conversionFactorOutputID type");
            return conversionFactorInputID.getValue().getConversionFactor()/conversionFactorOutputID.getValue().getConversionFactor();
        } else {
            Log.i("VMM","conversionFactorInputID type != conversionFactorOutputID type");
            //check if input is mass or volume
            if (conversionFactorInputID.getValue().getType() == 1 && conversionFactorOutputID.getValue().getType() == 2) {
                Log.i("VMM","conversionFactorInputID is mass");
                return conversionFactorInputID.getValue().getConversionFactor()/(conversionFactorOutputID.getValue().getConversionFactor()*ingredientSelected.getValue().getDensity());
            } else {
                Log.i("VMM","conversionFactorInputID is volume");
                return conversionFactorInputID.getValue().getConversionFactor()*ingredientSelected.getValue().getDensity()/conversionFactorOutputID.getValue().getConversionFactor();
            }
        }
    }





    //TODO: https://stackoverflow.com/questions/49493772/mediatorlivedata-or-switchmap-transformation-with-multiple-parameters

    /**
     * filter for the allowable output list for the spinner
     */
    static class SubsetConversionFactorFilter {
        final ConversionFactorsRecord conversionFactorsRecord;
        final IngredientsRecord ingredientsRecord;

        SubsetConversionFactorFilter(ConversionFactorsRecord conversionFactorsRecord, IngredientsRecord ingredientsRecord) {
            this.conversionFactorsRecord = conversionFactorsRecord;
            this.ingredientsRecord = ingredientsRecord;
        }

    }

    private void mediatorSubsetFilterInit() {
        Log.i("VMM","mediatorSubsetFilterInit init");
        //observe the input type
        mediatorSubsetConversionFactorFilter.addSource(conversionFactorInputID, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord input) {
                Log.i("VMM","mediatorSubsetFilterInit conversionFactorInputID changed");
                if (conversionFactorInputID.getValue() != null && ingredientSelected.getValue() != null) {
                    //create the subset filter
                    mediatorSubsetConversionFactorFilter.setValue(createSubsetFilter());
                }
            }
        });
        //observe the output type
        mediatorSubsetConversionFactorFilter.addSource(ingredientSelected, new Observer<IngredientsRecord>() {
            @Override
            public void onChanged(IngredientsRecord input) {
                if (conversionFactorInputID.getValue() != null && ingredientSelected.getValue() != null) {
                    //create the subset filter
                    Log.i("VMM","mediatorSubsetFilterInit ingredientSelected changed");
                    mediatorSubsetConversionFactorFilter.setValue(createSubsetFilter());
                }
            }
        });
    }

    private SubsetConversionFactorFilter createSubsetFilter() {
        Log.i("VMM","createSubsetFilter " + conversionFactorInputID.getValue().getName() + " " + ingredientSelected.getValue().getName());
        return new SubsetConversionFactorFilter(conversionFactorInputID.getValue(), ingredientSelected.getValue());
    }
}