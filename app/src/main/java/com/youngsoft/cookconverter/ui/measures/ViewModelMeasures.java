package com.youngsoft.cookconverter.ui.measures;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.preference.PreferenceManager;

import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.data.DataRepository;
import com.youngsoft.cookconverter.data.IngredientsRecord;

import java.util.List;

import static com.youngsoft.cookconverter.ui.preferences.FragmentPreferences.KEY_PREF_DEFAULT_UNIT;

public class ViewModelMeasures extends AndroidViewModel {

    private final DataRepository dataRepository;

    SharedPreferences preferences;

    //Live Data from Database
    private final LiveData<List<ConversionFactorsRecord>> allConversionFactors;
    private final LiveData<List<IngredientsRecord>> allIngredients;
    private final LiveData<List<ConversionFactorsRecord>> subsetConversionFactorList;
    private final LiveData<ConversionFactorsRecord> sharedPreferenceConversionFactor;

    //Mutable live data values
    private final MutableLiveData<Double> inputValue;
    private final MutableLiveData<ConversionFactorsRecord> conversionFactorInputID;
    private final MutableLiveData<ConversionFactorsRecord> conversionFactorOutputID;
    private final MutableLiveData<IngredientsRecord> ingredientSelected;
    //private final MutableLiveData<ConversionFactorsRecord>

    //Mediator live data values
    private final MediatorLiveData<ConversionFactorsRecord> mediatorInputConversionFactor;
    private final MediatorLiveData<ConversionFactorsRecord> mediatorOutputConversionFactor;
    private final MediatorLiveData<Double> mediatorOutput;
    private final MediatorLiveData<Double> mediatorConversionFactor;
    private final MediatorLiveData<SubsetConversionFactorFilter> mediatorSubsetConversionFactorFilter;


    //Constructor
    public ViewModelMeasures(@NonNull Application application) {
        super(application);

        Log.i("ViewModelMeasures","ViewModel Constructor");

        dataRepository = new DataRepository(application);
        allConversionFactors = dataRepository.getAllMassVolumeConversionFactors();
        allIngredients = dataRepository.getAllIngredientsRecords();

        //get the sharedpreference livedata value
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        sharedPreferenceConversionFactor = dataRepository.getSingleConversionFactor(preferences.getInt(KEY_PREF_DEFAULT_UNIT,1));

        //set initial input value for conversion to 0
        inputValue = new MutableLiveData<>();
        inputValue.setValue(0.0);

        //set initial ingredient
        ingredientSelected = new MutableLiveData<>();

        //set initial input/output conversion values
        conversionFactorInputID = new MutableLiveData<>();
        conversionFactorOutputID = new MutableLiveData<>();

        //create the mediator value for the input conversion value
        mediatorInputConversionFactor = new MediatorLiveData<>();
        mediatorInputConversionFactorInit();

        //create the mediator value for the output conversion value
        mediatorOutputConversionFactor = new MediatorLiveData<>();
        mediatorOutputConversionFactorInit();

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
                return dataRepository.getSubsetConversionFactors(input.conversionFactorsRecord, input.ingredientsRecord);
            }
        });

    }


    //get the converted value
    public LiveData<Double> getMediatorOutput() {
        return mediatorOutput;
    }

    //return list of all conversion factor records
    LiveData<List<ConversionFactorsRecord>> getAllConversionFactors() {
        return allConversionFactors;
    }

    //return list of all ingredient records
    LiveData<List<IngredientsRecord>> getAllIngredients() {
        return allIngredients;
    }

    //return list of all ingredient records
    LiveData<SubsetConversionFactorFilter> getSubsetConversionFactorFilter() {
        return mediatorSubsetConversionFactorFilter;
    }

    //return list of subset conversion factor records
    LiveData<List<ConversionFactorsRecord>> getSubsetConversionFactors() {
        return subsetConversionFactorList;
    }

    //return input conversion factor
    LiveData<ConversionFactorsRecord> getInputConversionFactor() {
        return mediatorInputConversionFactor;
    }

    //return output conversion factor
    LiveData<ConversionFactorsRecord> getOutputConversionFactor() {
        return mediatorOutputConversionFactor;
    }




    //set the input value
    void setInputValueMutable(Double input) {
        inputValue.setValue(input);
    }

    //set the picked input conversion factor ID
    void setConversionFactorInputID(ConversionFactorsRecord input) {
        conversionFactorInputID.setValue(input);
    }

    //set the picked output conversion factor ID
    void setConversionFactorOutputID(ConversionFactorsRecord input) {
        conversionFactorOutputID.setValue(input);
    }

    //set the selected ingredient
    void setIngredientSelected(IngredientsRecord input) {
        ingredientSelected.setValue(input);
    }



    private void mediatorInputConversionFactorInit() {

        //observe the user selected input value
        mediatorInputConversionFactor.addSource(conversionFactorInputID, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                Log.i("ViewModelMeasures", "mediatorInputConversionFactorInit conversionFactorInputID "
                        + conversionFactorInputID.getValue().getName());
                mediatorInputConversionFactor.setValue(conversionFactorInputID.getValue());
            }
        });

        //observe the shared preference value
        mediatorInputConversionFactor.addSource(sharedPreferenceConversionFactor, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                Log.i("ViewModelMeasures", "mediatorInputConversionFactorInit sharedPreferenceConversionFactor "
                 + sharedPreferenceConversionFactor.getValue().getName());
                mediatorInputConversionFactor.setValue(sharedPreferenceConversionFactor.getValue());
                //mediatorInputConversionFactor.removeSource(sharedPreferenceConversionFactor);
            }
        });

    }


    private void mediatorOutputConversionFactorInit() {

        //observe the user selected input value
        mediatorOutputConversionFactor.addSource(conversionFactorOutputID, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                Log.i("ViewModelMeasures", "mediatorOutputConversionFactorInit conversionFactorOutputID "
                        + conversionFactorOutputID.getValue().getName());
                mediatorOutputConversionFactor.setValue(conversionFactorOutputID.getValue());
            }
        });

        //observe the shared preference value
        mediatorOutputConversionFactor.addSource(sharedPreferenceConversionFactor, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                Log.i("ViewModelMeasures", "mediatorOutputConversionFactorInit sharedPreferenceConversionFactor "
                        + sharedPreferenceConversionFactor.getValue().getName());
                mediatorOutputConversionFactor.setValue(sharedPreferenceConversionFactor.getValue());
                //mediatorOutputConversionFactor.removeSource(sharedPreferenceConversionFactor);
            }
        });

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

    /**
     * calculate the conversion factor based on input, output, and ingredient
     * @return returns the conversion factor value
     */
    private Double calculateConversionFactor() {
        //check if input and output data types are the same (i.e. both mass or both volume
        if (conversionFactorInputID.getValue().getType() == conversionFactorOutputID.getValue().getType()) {
            return conversionFactorInputID.getValue().getConversionFactor()/conversionFactorOutputID.getValue().getConversionFactor();
        } else {
            //check if input is mass or volume
            if (conversionFactorInputID.getValue().getType() == 1 && conversionFactorOutputID.getValue().getType() == 2) {
                return conversionFactorInputID.getValue().getConversionFactor()/(conversionFactorOutputID.getValue().getConversionFactor()*ingredientSelected.getValue().getDensity());
            } else {
                return conversionFactorInputID.getValue().getConversionFactor()*ingredientSelected.getValue().getDensity()/conversionFactorOutputID.getValue().getConversionFactor();
            }
        }
    }






    /**
     * filter class for the allowable output list for the spinner
     */
    static class SubsetConversionFactorFilter {
        final ConversionFactorsRecord conversionFactorsRecord;
        final IngredientsRecord ingredientsRecord;

        SubsetConversionFactorFilter(ConversionFactorsRecord conversionFactorsRecord, IngredientsRecord ingredientsRecord) {
            this.conversionFactorsRecord = conversionFactorsRecord;
            this.ingredientsRecord = ingredientsRecord;
        }

    }

    /**
     * setup the mediator for the subset conversion factor filter, based on conversion factor & ingredient
     */
    private void mediatorSubsetFilterInit() {
        //observe the input type
        mediatorSubsetConversionFactorFilter.addSource(conversionFactorInputID, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord input) {
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
                    mediatorSubsetConversionFactorFilter.setValue(createSubsetFilter());
                }
            }
        });
    }

    /**
     * create a subset filter based on livedata values of conversion factor and ingredient
     * @return returns the subset filter used for filtering the list of conversion factors
     */
    private SubsetConversionFactorFilter createSubsetFilter() {
        return new SubsetConversionFactorFilter(conversionFactorInputID.getValue(), ingredientSelected.getValue());
    }
}