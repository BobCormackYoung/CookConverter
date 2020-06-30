package com.youngsoft.cookconverter.data;

import androidx.room.Entity;

@Entity (primaryKeys = {"recipeListID", "conversionFactorID"})
public class RecipeConversionFactorCrossReference {
    public long recipeListID;
    public long conversionFactorID;

    public RecipeConversionFactorCrossReference(long recipeListID, long conversionFactorID) {
        this.recipeListID = recipeListID;
        this.conversionFactorID = conversionFactorID;
    }

}
