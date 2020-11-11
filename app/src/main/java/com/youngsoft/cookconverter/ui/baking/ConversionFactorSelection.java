package com.youngsoft.cookconverter.ui.baking;

import com.youngsoft.cookconverter.data.ConversionFactorsRecord;

import java.util.List;

public class ConversionFactorSelection {

    List<ConversionFactorsRecord> conversionFactorsRecordList;
    int selectedConversionFactor = -1;
    boolean isConversionFactorSelected = false;

    //empty constructor
    public ConversionFactorSelection() {
        selectedConversionFactor = -1; //init selected conversion factor as -1
        isConversionFactorSelected = false; //init that no conversion factor is selected
    }

    /**
     * Get a list of conversion factor records
     * @return List<ConversionFactorsRecord>
     */
    public List<ConversionFactorsRecord> getConversionFactorsRecordList() {
        return conversionFactorsRecordList;
    }

    /**
     * Set a list of conversion factor records
     * @param conversionFactorsRecordList
     */
    public void setConversionFactorsRecordList(List<ConversionFactorsRecord> conversionFactorsRecordList) {
        this.conversionFactorsRecordList = conversionFactorsRecordList;
    }

    /**
     * Get the id of the selected conversion factor
     * @return int
     */
    public int getSelectedConversionFactor() {
        return selectedConversionFactor;
    }

    /**
     * Set the id of the selected conversion factor
     * @param selectedConversionFactor
     */
    public void setSelectedConversionFactor(int selectedConversionFactor) {
        this.selectedConversionFactor = selectedConversionFactor;
    }

    /**
     * Get whether a conversion factor has been selected
     * @return boolean
     */
    public boolean isConversionFactorSelected() {
        return isConversionFactorSelected;
    }

    /**
     * Set whether a conversion factor has been selected
     * @param conversionFactorSelected
     */
    public void setConversionFactorSelected(boolean conversionFactorSelected) {
        isConversionFactorSelected = conversionFactorSelected;
    }
}
