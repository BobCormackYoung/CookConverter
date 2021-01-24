package com.youngsoft.cookconverter.ui.measures;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;

import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.data.DataRepository;
import com.youngsoft.cookconverter.data.IngredientsRecord;

import java.util.List;
import java.util.concurrent.Executors;

import static com.youngsoft.cookconverter.ui.preferences.FragmentPreferences.KEY_PREF_DEFAULT_UNIT;

public class ViewModelMeasures extends AndroidViewModel {

    private final DataRepository dataRepository;

    SharedPreferences preferences;

    //Live Data from Database
    private final LiveData<List<ConversionFactorsRecord>> allConversionFactors;
    private final LiveData<List<IngredientsRecord>> allIngredients;

    //Mutable live data values
    private MutableLiveData<Double> inputValue;
    private MutableLiveData<ConversionFactorsRecord> conversionFactorInputID;
    private MutableLiveData<ConversionFactorsRecord> conversionFactorOutputID;
    private MutableLiveData<IngredientsRecord> ingredientSelected;

    //Mediator live data values
    private final MediatorLiveData<Double> mediatorOutput;
    private final MediatorLiveData<Double> mediatorConversionFactor;
    private final MediatorLiveData<IngredientDropDownState> mediatorIngredientDropDownState;


    /**
     * ViewModelMeasures constructor
     * @param application the application
     */
    public ViewModelMeasures(@NonNull Application application) {
        super(application);

        dataRepository = new DataRepository(application);
        allConversionFactors = dataRepository.getAllMassVolumeConversionFactors();
        allIngredients = dataRepository.getAllIngredientsRecords();

        //get the sharedPreference livedata value
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        //sharedPreferenceConversionFactor = dataRepository.getSingleConversionFactor(preferences.getInt(KEY_PREF_DEFAULT_UNIT,1));

        setupLiveData();
        initLiveData();

        //instantiate mediatorLiveData objects
        mediatorConversionFactor = new MediatorLiveData<>();
        mediatorOutput = new MediatorLiveData<>();
        mediatorIngredientDropDownState = new MediatorLiveData<>();

        //initialise mediatorLiveData objects
        mediatorConversionFactorInit();
        mediatorOutputInit();
        mediatorIngredientDropDownStateInit();
    }

    /**
     * set up mutable live data values
     */
    private void setupLiveData() {
        inputValue = new MutableLiveData<>();
        ingredientSelected = new MutableLiveData<>();
        conversionFactorInputID = new MutableLiveData<>();
        conversionFactorOutputID = new MutableLiveData<>();
    }

    /**
     * initialise mutable live data values
     */
    private void initLiveData() {
        inputValue.setValue(0.0);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ingredientSelected.postValue(dataRepository.getSingleIngredient(1));
            }
        });
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                conversionFactorInputID.postValue(dataRepository.getSingleConversionFactorNonLive(preferences.getInt(KEY_PREF_DEFAULT_UNIT,1)));
            }
        });
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                conversionFactorOutputID.postValue(dataRepository.getSingleConversionFactorNonLive(preferences.getInt(KEY_PREF_DEFAULT_UNIT,1)));
            }
        });
    }

    /**
     * Get LiveData of the output value
     * @return the output value
     */
    public LiveData<Double> getMediatorOutput() {
        return mediatorOutput;
    }

    /**
     * Get LiveData list of conversion factors or units
     * @return the list of conversion factors
     */
    LiveData<List<ConversionFactorsRecord>> getAllConversionFactors() { return allConversionFactors; }

    /**
     * Get LiveData list of ingredients
     * @return the list of ingredients
     */
    LiveData<List<IngredientsRecord>> getAllIngredients() {
        return allIngredients;
    }

    /**
     * Get LiveData of the selected input conversion factor
     * @return the conversion factor
     */
    LiveData<ConversionFactorsRecord> getInputConversionFactor() { return conversionFactorInputID; }

    /**
     * Get LiveData of the selected output conversion factor
     * @return the conversion factor
     */
    public LiveData<ConversionFactorsRecord> getOutputConversionFactor() { return conversionFactorOutputID; }

    /**
     * Get LiveData of the selected ingredient
     * @return the ingredient
     */
    public LiveData<IngredientsRecord> getIngredientsRecord() {
        return ingredientSelected;
    }

    /**
     * Get LiveData of the ingredient drop down error state
     * @return the ingredient drop down state
     */
    public LiveData<IngredientDropDownState> getIngredientsDropDownState() { return mediatorIngredientDropDownState; }

    /**
     * Get LiveData of the input value
     * @return the input value
     */
    LiveData<Double> getInputValue() { return inputValue; }


    //set the input value
    void setInputValueMutable(Double input) {
        inputValue.setValue(input);
    }
    //set the picked input conversion factor ID
    void setConversionFactorInputID(ConversionFactorsRecord input) { conversionFactorInputID.setValue(input); }
    //set the picked output conversion factor ID
    void setConversionFactorOutputID(ConversionFactorsRecord input) { conversionFactorOutputID.setValue(input); }
    //set the selected ingredient
    void setIngredientSelected(IngredientsRecord input) {
        ingredientSelected.setValue(input);
    }

    /**
     * setup the observers for the output value mediator that will calculate the output value based on input & conversion factor
     */
    private void mediatorOutputInit() {
        mediatorOutput.setValue(0.0);

        //observe the input value
        mediatorOutput.addSource(inputValue, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (inputValue.getValue() != null && mediatorConversionFactor.getValue() != null) {
                    mediatorOutput.setValue(calculateOutputValue(inputValue.getValue(), mediatorConversionFactor.getValue()));
                }
            }
        });
        //observe the conversion factor value
        mediatorOutput.addSource(mediatorConversionFactor, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (inputValue.getValue() != null && mediatorConversionFactor.getValue() != null) {
                    mediatorOutput.setValue(calculateOutputValue(inputValue.getValue(), mediatorConversionFactor.getValue()));
                }
            }
        });
    }

    /**
     * Calculate output based on input and conversion factor
     * @param input the input value
     * @param factor the conversion factor
     * @return the calculated output
     */
    private Double calculateOutputValue(Double input, Double factor) {
        return input*factor;
    }

    /**
     * setup the observers for the conversion factor mediator value
     */
    private void mediatorConversionFactorInit() {
        //observe the input type
        mediatorConversionFactor.addSource(conversionFactorInputID, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord input) {
                if (conversionFactorInputID.getValue() != null && conversionFactorOutputID.getValue() != null && ingredientSelected.getValue() != null && mediatorIngredientDropDownState.getValue() != null) {
                    //calculate the conversion factor
                    mediatorConversionFactor.setValue(calculateConversionFactor(conversionFactorInputID.getValue(), conversionFactorOutputID.getValue(), ingredientSelected.getValue(), mediatorIngredientDropDownState.getValue().getErrorState()));
                }
            }
        });
        //observe the output type
        mediatorConversionFactor.addSource(conversionFactorOutputID, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord input) {
                if (conversionFactorInputID.getValue() != null && conversionFactorOutputID.getValue() != null && ingredientSelected.getValue() != null && mediatorIngredientDropDownState.getValue() != null) {
                    //calculate the conversion factor
                    mediatorConversionFactor.setValue(calculateConversionFactor(conversionFactorInputID.getValue(), conversionFactorOutputID.getValue(), ingredientSelected.getValue(), mediatorIngredientDropDownState.getValue().getErrorState()));
                }
            }
        });
        //observe changes to the selected ingredient
        mediatorConversionFactor.addSource(ingredientSelected, new Observer<IngredientsRecord>() {
            @Override
            public void onChanged(IngredientsRecord ingredientsRecord) {
                if (conversionFactorInputID.getValue() != null && conversionFactorOutputID.getValue() != null && ingredientSelected.getValue() != null && mediatorIngredientDropDownState.getValue() != null) {
                    //calculate the conversion factor
                    mediatorConversionFactor.setValue(calculateConversionFactor(conversionFactorInputID.getValue(), conversionFactorOutputID.getValue(), ingredientSelected.getValue(), mediatorIngredientDropDownState.getValue().getErrorState()));
                }
            }
        });
        //observe changes to the ingredients error state
        mediatorConversionFactor.addSource(mediatorIngredientDropDownState, new Observer<IngredientDropDownState>() {
            @Override
            public void onChanged(IngredientDropDownState aIngredientDropDownState) {
                if (conversionFactorInputID.getValue() != null && conversionFactorOutputID.getValue() != null && ingredientSelected.getValue() != null && mediatorIngredientDropDownState.getValue() != null) {
                    //calculate the conversion factor
                    mediatorConversionFactor.setValue(calculateConversionFactor(conversionFactorInputID.getValue(), conversionFactorOutputID.getValue(), ingredientSelected.getValue(), mediatorIngredientDropDownState.getValue().getErrorState()));
                }
            }
        });
    }

    /**
     * Calculate conversion factor based on input, output, ingredient, and error state
     * @param inputConversionFactor the selected input conversion factor
     * @param outputConversionFactor the selected output conversion factor
     * @param ingredient the selected ingredient
     * @param ingredientIsError the error state of the user selections
     * @return the conversion factor
     */
    private Double calculateConversionFactor(ConversionFactorsRecord inputConversionFactor, ConversionFactorsRecord outputConversionFactor, IngredientsRecord ingredient, Boolean ingredientIsError) {
        //check if input and output data types are the same (i.e. both mass or both volume
        if (ingredientIsError) {
            return 0.0;
        } else if (inputConversionFactor.getType() == outputConversionFactor.getType()) {
            return inputConversionFactor.getConversionFactor()/outputConversionFactor.getConversionFactor();
        } else {
            //check if input is mass or volume
            if (inputConversionFactor.getType() == 1 && outputConversionFactor.getType() == 2) {
                return inputConversionFactor.getConversionFactor()/(outputConversionFactor.getConversionFactor()*ingredient.getDensity());
            } else {
                return inputConversionFactor.getConversionFactor()*ingredient.getDensity()/outputConversionFactor.getConversionFactor();
            }
        }
    }

    /**
     * output whether the ingredient is in an error state
     */
    private void mediatorIngredientDropDownStateInit() {

        mediatorIngredientDropDownState.setValue(new IngredientDropDownState(getApplication(), 1));

        mediatorIngredientDropDownState.addSource(conversionFactorInputID, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                if (conversionFactorInputID.getValue() != null &&
                        conversionFactorOutputID.getValue() != null &&
                        ingredientSelected.getValue() != null) {
                    mediatorIngredientDropDownState.setValue(new IngredientDropDownState(getApplication(),
                            checkIngredientStateState(conversionFactorInputID.getValue().getType(),
                                    conversionFactorOutputID.getValue().getType(),
                            ingredientSelected.getValue().getType())));
                }
            }
        });

        mediatorIngredientDropDownState.addSource(conversionFactorOutputID, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                if (conversionFactorInputID.getValue() != null &&
                        conversionFactorOutputID.getValue() != null &&
                        ingredientSelected.getValue() != null) {
                    mediatorIngredientDropDownState.setValue(new IngredientDropDownState(getApplication(),
                            checkIngredientStateState(conversionFactorInputID.getValue().getType(),
                                    conversionFactorOutputID.getValue().getType(),
                                    ingredientSelected.getValue().getType())));
                }
            }
        });

        mediatorIngredientDropDownState.addSource(ingredientSelected, new Observer<IngredientsRecord>() {
            @Override
            public void onChanged(IngredientsRecord ingredientsRecord) {
                if (conversionFactorInputID.getValue() != null &&
                        conversionFactorOutputID.getValue() != null &&
                        ingredientSelected.getValue() != null) {
                    mediatorIngredientDropDownState.setValue(new IngredientDropDownState(getApplication(),
                            checkIngredientStateState(conversionFactorInputID.getValue().getType(),
                                    conversionFactorOutputID.getValue().getType(),
                                    ingredientSelected.getValue().getType())));
                }
            }
        });
    }

    /**
     * Checks whether the ingredients dropdown should show as an error or not
     * @param inputType the type of the selected input (mass or volume)
     * @param outputType the type of the selected output (mass of volume)
     * @param ingredientType the type of the selected ingredient (0 = unselected)
     * @return the helper text state
     */
    public int checkIngredientStateState(int inputType, int outputType, int ingredientType) {
        if (inputType != outputType && ingredientType != 0) {
            //input and output different, ingredient not the default "no selection"
            return IngredientDropDownState.STATE_REQUIRED_NO_ERROR;
        } else if (inputType != outputType && ingredientType == 0) {
            //input and output different, ingredient is the default "no selection"
            return IngredientDropDownState.STATE_REQUIRED_ERROR;
        } else if (inputType == outputType) {
            //input and output the same, ingredient doesn't matter
            return IngredientDropDownState.STATE_OPTIONAL_NO_ERROR;
        } else {
            //no idea what is going on
            return IngredientDropDownState.STATE_OPTIONAL_NO_ERROR;
        }
    }

}