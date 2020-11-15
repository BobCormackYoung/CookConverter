package com.youngsoft.cookconverter;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewModelMainActivity extends AndroidViewModel {

    private MutableLiveData<Double> copyDataValue;

    public ViewModelMainActivity(@NonNull Application application) {
        super(application);
        setupLiveData();
    }

    /**
     * setup the livedata values
     */
    private void setupLiveData() {
        copyDataValue = new MutableLiveData<>();
        copyDataValue.setValue(0.0);
    }

    public void setCopyDataValue(Double input) {
        Log.i("VMMA","setCopyDataValue " + input);
        copyDataValue.setValue(input);
    }

    public LiveData<Double> getCopyDataValue() {
        return copyDataValue;
    }
}
