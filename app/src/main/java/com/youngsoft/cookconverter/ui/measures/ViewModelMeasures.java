package com.youngsoft.cookconverter.ui.measures;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

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

import static com.youngsoft.cookconverter.ui.preferences.FragmentPreferences.KEY_PREF_DEFAULT_UNIT;

public class ViewModelMeasures extends AndroidViewModel {

    private final DataRepository dataRepository;

    SharedPreferences preferences;

    //Live Data from Database
    private final LiveData<List<ConversionFactorsRecord>> allConversionFactors;
    private final LiveData<List<IngredientsRecord>> allIngredients;
    private final LiveData<ConversionFactorsRecord> sharedPreferenceConversionFactor;

    //Mutable live data values
    private final MutableLiveData<Double> inputValue;
    private final MutableLiveData<ConversionFactorsRecord> conversionFactorInputID;
    private final MutableLiveData<ConversionFactorsRecord> conversionFactorOutputID;
    private final MutableLiveData<IngredientsRecord> ingredientSelected;

    //Mediator live data values
    private final MediatorLiveData<ConversionFactorsRecord> mediatorInputConversionFactor;
    private final MediatorLiveData<ConversionFactorsRecord> mediatorOutputConversionFactor;
    private final MediatorLiveData<Double> mediatorOutput;
    private final MediatorLiveData<Double> mediatorConversionFactor;
    private final MediatorLiveData<IngredientDropDownState> mediatorIngredientDropDownState;


    //Constructor
    public ViewModelMeasures(@NonNull Application application) {
        super(application);

        dataRepository = new DataRepository(application);
        allConversionFactors = dataRepository.getAllMassVolumeConversionFactors();
        allIngredients = dataRepository.getAllIngredientsRecords();

        //get the sharedPreference livedata value
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

        //instantiate mediatorLiveData objects
        mediatorInputConversionFactor = new MediatorLiveData<>();
        mediatorOutputConversionFactor = new MediatorLiveData<>();
        mediatorConversionFactor = new MediatorLiveData<>();
        mediatorOutput = new MediatorLiveData<>();
        mediatorIngredientDropDownState = new MediatorLiveData<>();

        //initialise mediatorLiveData objects
        mediatorInputConversionFactorInit();
        mediatorOutputConversionFactorInit();
        mediatorConversionFactorInit();
        mediatorOutputInit();
        mediatorIngredientDropDownStateInit();
    }


    //get the converted value
    public LiveData<Double> getMediatorOutput() {
        return mediatorOutput;
    }
    //return list of all conversion factor records
    LiveData<List<ConversionFactorsRecord>> getAllConversionFactors() { return allConversionFactors; }
    //return list of all ingredient records
    LiveData<List<IngredientsRecord>> getAllIngredients() {
        return allIngredients;
    }
    //return input conversion factor
    LiveData<ConversionFactorsRecord> getInputConversionFactor() { return mediatorInputConversionFactor; }
    //return output conversion factor
    public LiveData<ConversionFactorsRecord> getOutputConversionFactor() { return mediatorOutputConversionFactor; }
    //return the selected ingredient value
    public LiveData<IngredientsRecord> getIngredientsRecord() {
        return ingredientSelected;
    }
    //return the helper text
    public LiveData<IngredientDropDownState> getIngredientsDropDownState() { return mediatorIngredientDropDownState; }


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
     * set the input conversion factor based on sharedpreferences or user selection
     */
    private void mediatorInputConversionFactorInit() {

        //observe the user selected input value
        mediatorInputConversionFactor.addSource(conversionFactorInputID, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                mediatorInputConversionFactor.setValue(conversionFactorInputID.getValue());
            }
        });

        //observe the shared preference value
        mediatorInputConversionFactor.addSource(sharedPreferenceConversionFactor, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                mediatorInputConversionFactor.setValue(sharedPreferenceConversionFactor.getValue());
                //mediatorInputConversionFactor.removeSource(sharedPreferenceConversionFactor);
            }
        });

    }

    /**
     * set the output conversion factor based on sharedpreferences or user selection
     */
    private void mediatorOutputConversionFactorInit() {

        //observe the user selected input value
        mediatorOutputConversionFactor.addSource(conversionFactorOutputID, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                mediatorOutputConversionFactor.setValue(conversionFactorOutputID.getValue());
            }
        });

        //observe the shared preference value
        mediatorOutputConversionFactor.addSource(sharedPreferenceConversionFactor, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
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

        mediatorIngredientDropDownState.addSource(mediatorInputConversionFactor, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                if (mediatorInputConversionFactor.getValue() != null &&
                        mediatorOutputConversionFactor.getValue() != null &&
                        ingredientSelected.getValue() != null) {
                    mediatorIngredientDropDownState.setValue(new IngredientDropDownState(getApplication(),
                            checkIngredientStateState(mediatorInputConversionFactor.getValue().getType(),
                            mediatorOutputConversionFactor.getValue().getType(),
                            ingredientSelected.getValue().getType())));
                }
            }
        });

        mediatorIngredientDropDownState.addSource(mediatorOutputConversionFactor, new Observer<ConversionFactorsRecord>() {
            @Override
            public void onChanged(ConversionFactorsRecord conversionFactorsRecord) {
                if (mediatorInputConversionFactor.getValue() != null &&
                        mediatorOutputConversionFactor.getValue() != null &&
                        ingredientSelected.getValue() != null) {
                    mediatorIngredientDropDownState.setValue(new IngredientDropDownState(getApplication(),
                            checkIngredientStateState(mediatorInputConversionFactor.getValue().getType(),
                                    mediatorOutputConversionFactor.getValue().getType(),
                                    ingredientSelected.getValue().getType())));
                }
            }
        });

        mediatorIngredientDropDownState.addSource(ingredientSelected, new Observer<IngredientsRecord>() {
            @Override
            public void onChanged(IngredientsRecord ingredientsRecord) {
                if (mediatorInputConversionFactor.getValue() != null &&
                        mediatorOutputConversionFactor.getValue() != null &&
                        ingredientSelected.getValue() != null) {
                    mediatorIngredientDropDownState.setValue(new IngredientDropDownState(getApplication(),
                            checkIngredientStateState(mediatorInputConversionFactor.getValue().getType(),
                                    mediatorOutputConversionFactor.getValue().getType(),
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
            Log.i("VMM","Unknown error combination of ingredient & input/output types");
            return IngredientDropDownState.STATE_OPTIONAL_NO_ERROR;
        }
    }

}