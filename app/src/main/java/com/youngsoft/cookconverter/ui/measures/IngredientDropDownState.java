package com.youngsoft.cookconverter.ui.measures;

import android.app.Application;

import com.youngsoft.cookconverter.R;

public class IngredientDropDownState {

    public static final int STATE_OPTIONAL_NO_ERROR = 1;
    public static final int STATE_REQUIRED_NO_ERROR = 2;
    public static final int STATE_REQUIRED_ERROR = 3;

    boolean isError = false;
    int currentState = 1;
    Application application;

    /**
     * Constructor for IngredientsDropDownState
     * @param application the application required to get resources
     * @param currentState the current state.
     * @see IngredientDropDownState#STATE_OPTIONAL_NO_ERROR Optional no error =1
     * @see IngredientDropDownState#STATE_REQUIRED_NO_ERROR Required no error =2
     * @see IngredientDropDownState#STATE_REQUIRED_ERROR Required error =3
     */
    public IngredientDropDownState(Application application, int currentState) {
        this.application = application;
        this.currentState = currentState;
        updateErrorState(currentState);
    }

    /**
     * Get the current state of the Ingredient DropDown
     * @return int of current state
     */
    public int getState() {
        return currentState;
    }

    /**
     * Get whether the view should be an error state or not
     * @return whether error or not
     */
    public boolean getErrorState() { return isError; }

    /**
     * Get the required helper text
     * @return String helper text
     */
    public String getHelperText() {
        if (currentState == STATE_OPTIONAL_NO_ERROR) {
            return application.getResources().getString(R.string.helper_ingredients_optional);
        } else if (currentState == STATE_REQUIRED_NO_ERROR) {
            return application.getResources().getString(R.string.helper_ingredients_required);
        } else if (currentState == STATE_REQUIRED_ERROR) {
            return application.getResources().getString(R.string.error_ingredients);
        } else {
            return "error";
        }
    }

    void updateErrorState(int state) {
        if (state == STATE_REQUIRED_ERROR) {
            isError = true;
        } else {
            isError = false;
        }
    }
}
