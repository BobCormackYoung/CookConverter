package com.youngsoft.cookconverter.ui.preferences;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.data.DataRepository;

import java.util.List;

public class ViewModelPreferences extends AndroidViewModel {

    private final DataRepository dataRepository;

    //Live Data from Database
    private final LiveData<List<ConversionFactorsRecord>> massConversionFactors;
    private final LiveData<List<ConversionFactorsRecord>> volumeConversionFactors;

    public ViewModelPreferences(@NonNull Application application) {
        super(application);
        Log.i("FragPref","ViewModelPreferences constructor");
        dataRepository = new DataRepository(application);
        //massConversionFactors = dataRepository.getSimpleSubsetConversionFactors(1);
        //volumeConversionFactors = dataRepository.getSimpleSubsetConversionFactors(2);
        massConversionFactors = dataRepository.getAllMassConversionFactors();
        volumeConversionFactors = dataRepository.getAllVolumeConversionFactors();
    }

    //return list of all mss conversion factor records
    LiveData<List<ConversionFactorsRecord>> getMassConversionFactors() {
        Log.i("FragPref","ViewModelPreferences getMassConversionFactors");
        return massConversionFactors;
    }

    //return list of all mss conversion factor records
    LiveData<List<ConversionFactorsRecord>> getVolumeConversionFactors() {
        Log.i("FragPref","ViewModelPreferences getVolumeConversionFactors");
        return volumeConversionFactors;
    }


}
