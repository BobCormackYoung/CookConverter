package com.youngsoft.cookconverter.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "IngredientsRecord_Table")
public class IngredientsRecord {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private int type; //1 = solid, 2 = liquid
    private double density; //kg/m^3

    public IngredientsRecord(String name, int type, double density) {
        this.name = name;
        this.type = type;
        this.density = density;
    }

    public static IngredientsRecord[] populateIngredientsRecordData() {
        return new IngredientsRecord[] {
                new IngredientsRecord("Not Selected",0,-1),
                new IngredientsRecord("Butter (solid)",1,911),
                new IngredientsRecord("Butter (melted)",2, 911),
                new IngredientsRecord("Chocolate (solid)",1, 1325),
                new IngredientsRecord("Chocolate (melted)",1, 1325),
                new IngredientsRecord("Cocoa (powdered)",1, 360),
                new IngredientsRecord("Cream",2,1031),
                new IngredientsRecord("Flour",1,593),
                new IngredientsRecord("Milk",2,1026),
                new IngredientsRecord("Sugar (granulated)",1,1590),
                new IngredientsRecord("Sugar (powdered)",1,801),
                new IngredientsRecord("Sugar (brown)",1, 721),
                new IngredientsRecord("Water",2,997),
                new IngredientsRecord("Wine",2, 994)
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getDensity() {
        return density;
    }

    public void setDensity(double density) {
        this.density = density;
    }
}
