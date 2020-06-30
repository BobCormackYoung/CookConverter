package com.youngsoft.cookconverter.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RecipeList_Table")
public class RecipeList {

    @PrimaryKey(autoGenerate = true)
    private long recipeListID;

    private String name;
    private double value; //value
    private long conversionFactor; //id of the conversion factor type

    public RecipeList(String name, double value, long conversionFactor) {
        this.name = name;
        this.value = value;
        this.conversionFactor = conversionFactor;
    }

    public void setRecipeListID(long recipeListID) {
        this.recipeListID = recipeListID;
    }

    public long getRecipeListID() {
        return recipeListID;
    }

    public String getName() {
        return name;
    }                       

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(int conversionFactor) {
        this.conversionFactor = conversionFactor;
    }
}
