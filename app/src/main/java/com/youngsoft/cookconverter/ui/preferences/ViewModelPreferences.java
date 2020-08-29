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
    private final LiveData<List<ConversionFactorsRecord>> allConversionFactors;

    public ViewModelPreferences(@NonNull Application application) {
        super(application);
        Log.i("FragPref","ViewModelPreferences constructor");
        dataRepository = new DataRepository(application);
        allConversionFactors = dataRepository.getAllMassVolumeConversionFactors();
    }

    //return list of all mss conversion factor records
    LiveData<List<ConversionFactorsRecord>> getAllConversionFactors() {
        Log.i("FragPref","ViewModelPreferences getAllConversionFactors");
        return allConversionFactors;
    }


}
