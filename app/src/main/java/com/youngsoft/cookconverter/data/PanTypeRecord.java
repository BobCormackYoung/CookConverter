package com.youngsoft.cookconverter.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PanTypeRecord_Table")
public class PanTypeRecord {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private int type; //1 = rectangular, 2 = circular, 3 = bundt
    private int dimensions; //number of defining dimensions (excluding height)
    private String dimensionName1;
    private String dimensionName2;
    private String dimensionName3; //may never be used

    public PanTypeRecord(String name, int type, int dimensions, String dimensionName1, String dimensionName2, String dimensionName3) {
        this.name = name;
        this.type = type;
        this.dimensions = dimensions;
        this.dimensionName1 = dimensionName1;
        this.dimensionName2 = dimensionName2;
        this.dimensionName3 = dimensionName3;
    }

    public static PanTypeRecord[] populatePanTypeRecordData() {
        return new PanTypeRecord[] {
                new PanTypeRecord("Rectangular", 1, 2, "Length", "Width", "unused"),
                new PanTypeRecord("Circular", 2, 1, "Diameter", "unused", "unused"),
                new PanTypeRecord("Bundtpan", 3, 2, "Outer Diameter", "Inner Diameter", "unused")
        };
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getDimensions() {
        return dimensions;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    public String getDimensionName1() {
        return dimensionName1;
    }

    public void setDimensionName1(String dimensionName1) {
        this.dimensionName1 = dimensionName1;
    }

    public String getDimensionName2() {
        return dimensionName2;
    }

    public void setDimensionName2(String dimensionName2) {
        this.dimensionName2 = dimensionName2;
    }

    public String getDimensionName3() {
        return dimensionName3;
    }

    public void setDimensionName3(String dimensionName3) {
        this.dimensionName3 = dimensionName3;
    }
}
