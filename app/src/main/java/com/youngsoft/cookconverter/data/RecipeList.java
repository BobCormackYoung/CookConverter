package com.youngsoft.cookconverter.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RecipeList_Table")
public class RecipeList {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private double value; //value
    private int conversionFactor; //id of the conversion factor type

    public RecipeList(String name, double value, int conversionFactor) {
        this.name = name;
        this.value = value;
        this.conversionFactor = conversionFactor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    public int getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(int conversionFactor) {
        this.conversionFactor = conversionFactor;
    }
}
