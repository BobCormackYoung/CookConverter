package com.youngsoft.cookconverter.data;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class RecipeWithConversionFactor {

    @Embedded
    public RecipeList recipeList;
    @Relation(
            parentColumn = "recipeListID",
            entityColumn = "conversionFactorID",
            associateBy = @Junction(RecipeConversionFactorCrossReference.class)
    )
    public List<ConversionFactorsRecord> conversionFactorsRecords;

}
