package com.youngsoft.cookconverter.ui.preferences;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.data.DataRepository;

import java.util.List;

public class ViewModelPreferences extends AndroidViewModel {

    private final DataRepository dataRepository;

    //Live Data from Database
    private final LiveData<List<ConversionFactorsRecord>> allConversionFactors;
    private final LiveData<List<ConversionFactorsRecord>> allDistanceConversionFactors;

    public ViewModelPreferences(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
        allConversionFactors = dataRepository.getAllMassVolumeConversionFactors();
        allDistanceConversionFactors = dataRepository.getAllDistanceConversionFactors();
    }

    /**
     * Get a live data list of all mass & volume conversion factors
     * @return LiveData list of ConversionFactorsRecord
     */
    LiveData<List<ConversionFactorsRecord>> getAllConversionFactors() {
        return allConversionFactors;
    }

    /**
     * Get a live data list of all distance conversion factors
     * @return LiveData list of ConversionFactorsRecord
     */
    LiveData<List<ConversionFactorsRecord>> getAllDistanceConversionFactors() {
        return allDistanceConversionFactors;
    }

}
