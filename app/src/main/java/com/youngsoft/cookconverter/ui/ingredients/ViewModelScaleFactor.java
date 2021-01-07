package com.youngsoft.cookconverter.ui.ingredients;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class ViewModelScaleFactor extends AndroidViewModel {

    private MutableLiveData<Double> userAmount;
    private MutableLiveData<Double> recipeAmount;

    private final MediatorLiveData<Double> scaleFactor;
    private final MediatorLiveData<Boolean> isRecipeAmountErrorState;

    public ViewModelScaleFactor(@NonNull Application application) {
        super(application);

        setupLiveData();
        initLiveData();

        isRecipeAmountErrorState = new MediatorLiveData<>();
        scaleFactor = new MediatorLiveData<>();

        mediatorIsRecipeAmountErrorStateInit();
        mediatorScaleFactorInit();
    }

    private void mediatorScaleFactorInit() {
        scaleFactor.setValue(0.0);

        scaleFactor.addSource(recipeAmount, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (recipeAmount.getValue() != null && userAmount.getValue() != null && isRecipeAmountErrorState.getValue() != null) {
                    scaleFactor.setValue(calculateScaleFactor(recipeAmount.getValue(), userAmount.getValue(), isRecipeAmountErrorState.getValue()));
                }
            }
        });

        scaleFactor.addSource(userAmount, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (recipeAmount.getValue() != null && userAmount.getValue() != null && isRecipeAmountErrorState.getValue() != null) {
                    scaleFactor.setValue(calculateScaleFactor(recipeAmount.getValue(), userAmount.getValue(), isRecipeAmountErrorState.getValue()));
                }
            }
        });

        scaleFactor.addSource(isRecipeAmountErrorState, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (recipeAmount.getValue() != null && userAmount.getValue() != null && isRecipeAmountErrorState.getValue() != null) {
                    scaleFactor.setValue(calculateScaleFactor(recipeAmount.getValue(), userAmount.getValue(), isRecipeAmountErrorState.getValue()));
                }
            }
        });

    }

    private Double calculateScaleFactor(Double recipeAmount, Double userAmount, Boolean isError) {
        if (isError) {
            return 0.0;
        } else {
            return userAmount/recipeAmount;
        }
    }

    private void mediatorIsRecipeAmountErrorStateInit() {
        isRecipeAmountErrorState.setValue(false);

        isRecipeAmountErrorState.addSource(recipeAmount, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble != null) {
                    if (aDouble == 0.0) {
                        //cannot divide by zero, need to display an error
                        isRecipeAmountErrorState.setValue(true);
                    } else {
                        isRecipeAmountErrorState.setValue(false);
                    }
                }
            }
        });

    }

    //getters
    public LiveData<Double> getUserAmount() { return userAmount; }
    public LiveData<Double> getRecipeAmount() { return recipeAmount; }
    public LiveData<Double> getScaleFactor() { return scaleFactor; }
    public LiveData<Boolean> getIsRecipeAmountErrorState() { return isRecipeAmountErrorState; }

    //setters
    public void setUserAmount(Double input) { userAmount.setValue(input); }
    public void setRecipeAmount(Double input) { recipeAmount.setValue(input); }

    private void setupLiveData() {
        userAmount = new MutableLiveData<>();
        recipeAmount = new MutableLiveData<>();
    }

    private void initLiveData() {
        userAmount.setValue(1.0);
        recipeAmount.setValue(1.0);
    }

    public Boolean saveDataAsOutput(ViewModelIngredients viewModelIngredients) {
        if (scaleFactor.getValue() != null &&
        recipeAmount.getValue() != null &&
        userAmount != null) {
            viewModelIngredients.setScaleFactor(scaleFactor.getValue());
            viewModelIngredients.setInputRecipeValue(recipeAmount.getValue());
            viewModelIngredients.setInputUserValue(userAmount.getValue());
            return true;
        } else {
            return false;
        }
    }

}
