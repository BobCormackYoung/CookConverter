package com.youngsoft.cookconverter.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ConversionFactorsRecord_Table")
public class ConversionFactorsRecord {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private double conversionFactor; //mass vs Kg, volume vs m^3, distance vs m
    private int type; //1 = mass, 2 = volume, 3 = distance

    public ConversionFactorsRecord(String name, double conversionFactor, int type) {
        this.name = name;
        this.conversionFactor = conversionFactor;
        this.type = type;
    }

    public static ConversionFactorsRecord[] populateConversionFactorRecordData() {
      return new ConversionFactorsRecord[] {
              new ConversionFactorsRecord("kg",1,1),
              new ConversionFactorsRecord("dg (DAG)",0.01,1),
              new ConversionFactorsRecord("gr",0.001,1),
              new ConversionFactorsRecord("stone",6.35029,1),
              new ConversionFactorsRecord("lb", 0.45359,1),
              new ConversionFactorsRecord("oz",0.02835,1),
              new ConversionFactorsRecord("litre",0.001,2),
              new ConversionFactorsRecord("millilitre",0.000001,2),
              new ConversionFactorsRecord("cup",0.0002365882,2),
              new ConversionFactorsRecord("fl-oz (US)",0.00002957353,2),
              new ConversionFactorsRecord("fl-oz (UK)",0.00002841307,2),
              new ConversionFactorsRecord("pint (US)",0.00047317650,2),
              new ConversionFactorsRecord("pint (UK)",0.00056826100,2),
              new ConversionFactorsRecord("quart",0.00094635290,2),
              new ConversionFactorsRecord("tablespoon (tbsp)",0.00001478676,2),
              new ConversionFactorsRecord("teaspoon (tsp)",0.00000492892,2),
              new ConversionFactorsRecord("m",1,3),
              new ConversionFactorsRecord("cm",0.01,3),
              new ConversionFactorsRecord("mm",0.001,3),
              new ConversionFactorsRecord("inch",0.0254,3),
              new ConversionFactorsRecord("foot",0.3048,3)
      };
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
