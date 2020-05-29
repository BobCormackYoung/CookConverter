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

import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.data.DataRepository;
import com.youngsoft.cookconverter.data.IngredientsRecord;

import java.util.List;

public class ViewModelMeasures extends AndroidViewModel {

    private DataRepository dataRepository;

    //Live Data from Database
    private LiveData<List<ConversionFactorsRecord>> allConversionFactors;
    private LiveData<List<IngredientsRecord>> allIngredients;

    //Mutable live data values
    private MutableLiveData<Double> inputValue;
    private MutableLiveData<Double> outputValue;
    private MutableLiveData<ConversionFactorsRecord> conversionFactorInputID;
    private MutableLiveData<ConversionFactorsRecord> conversionFactorOutputID;
    private MutableLiveData<IngredientsRecord> ingredientSelected;

    //Mediator live data values
    private MediatorLiveData<Double> mediatorOutput;
    private MediatorLiveData<Double> conversionFactor;
    private MediatorLiveData<List<ConversionFactorsRecord>> subsetConversionFactorList;
    private MediatorLiveData<SubsetConversionFactorFilter> subsetConversionFactorFilter;


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
        //conversionFactorInputID.setValue(new ConversionFactorsRecord("dummy",1,-1,-1));
        conversionFactorOutputID = new MutableLiveData<>();
        //conversionFactorOutputID.setValue(new ConversionFactorsRecord("dummy",1,-1, -1));

        //create mediator conversion factor to convert when changing either input type or output type
        conversionFactor = new MediatorLiveData<>();
        mediatorConversionFactor();

        //create mediator output to convert when changing either input value or conversion value
        mediatorOutput = new MediatorLiveData<>();
        mediatorOutputInit();

        //create the mediator filter for the output values
        subsetConversionFactorFilter = new MediatorLiveData<>();
        mediatorSubsetFilterInit();

        //create the mediator list of allowable output types that can be chosen
        subsetConversionFactorList = new MediatorLiveData<>();

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
                if (inputValue.getValue() != null && conversionFactor.getValue() != null) {
                    mediatorOutput.setValue(calculateOutputValue());
                }
            }
        });
        //observe the conversion factor value
        mediatorOutput.addSource(conversionFactor, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (inputValue.getValue() != null && conversionFactor.getValue() != null) {
                    mediatorOutput.setValue(calculateOutputValue());
                }
            }
        });
    }

    private Double calculateOutputValue() {
        Log.i("VMM","Input = " + inputValue.getValue() + " Conversion = " + conversionFactor.getValue() + " Output = " + inputValue.getValue()*conversionFactor.getValue());
        return inputValue.getValue()*conversionFactor.getValue();
    }

    /**
     * setup the observers for the conversion factor mediator value
     */
    private void mediatorConversionFactor() {

        //TODO: add logic for handling changes to the ingredients value

        //observe the input type
        conversionFactor.addSource(conversionFactorInputID, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord input) {
                if (conversionFactorInputID.getValue() != null && conversionFactorOutputID.getValue() != null) {
                    //calculate the conversion factor
                    conversionFactor.setValue(calculateConversionFactor());
                }
            }
        });
        //observe the output type
        conversionFactor.addSource(conversionFactorOutputID, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord input) {
                if (conversionFactorInputID.getValue() != null && conversionFactorOutputID.getValue() != null) {
                    //calculate the conversion factor
                    conversionFactor.setValue(calculateConversionFactor());
                }
            }
        });
    }

    private Double calculateConversionFactor() {
        Log.i("VMM","Conversion Factor = " + conversionFactorInputID.getValue().getConversionFactor()/conversionFactorOutputID.getValue().getConversionFactor());
        return conversionFactorInputID.getValue().getConversionFactor()/conversionFactorOutputID.getValue().getConversionFactor();
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
        //observe the input type
        subsetConversionFactorFilter.addSource(conversionFactorInputID, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord input) {
                if (conversionFactorInputID.getValue() != null && ingredientSelected.getValue() != null) {
                    //create the subset filter
                    subsetConversionFactorFilter.setValue(createSubsetFilter());
                }
            }
        });
        //observe the output type
        subsetConversionFactorFilter.addSource(ingredientSelected, new Observer<IngredientsRecord>() {
            @Override
            public void onChanged(IngredientsRecord input) {
                if (conversionFactorInputID.getValue() != null && ingredientSelected.getValue() != null) {
                    //create the subset filter
                    subsetConversionFactorFilter.setValue(createSubsetFilter());
                }
            }
        });
    }

    private SubsetConversionFactorFilter createSubsetFilter() {
        return new SubsetConversionFactorFilter(conversionFactorInputID.getValue(), ingredientSelected.getValue());
    }
}