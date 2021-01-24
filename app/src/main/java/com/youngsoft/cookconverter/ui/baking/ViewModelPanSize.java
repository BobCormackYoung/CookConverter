package com.youngsoft.cookconverter.ui.baking;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;

import com.youngsoft.cookconverter.data.ConversionFactorsRecord;
import com.youngsoft.cookconverter.data.DataRepository;

import java.util.List;
import java.util.concurrent.Executors;

import static com.youngsoft.cookconverter.ui.preferences.FragmentPreferences.KEY_PREF_DEFAULT_DISTANCE_UNIT;

public class ViewModelPanSize extends AndroidViewModel {

    private final DataRepository dataRepository;

    SharedPreferences preferences;

    static final int PAN_TYPE_RECTANGULAR = 0;
    static final int PAN_TYPE_CIRCULAR = 1;
    static final int PAN_TYPE_BUNDT = 2;

    //livedata
    private LiveData<List<ConversionFactorsRecord>> conversionFactorsRecordLiveData;

    //Mutable livedata
    private MutableLiveData<Integer> panTypeMutable;
    private MutableLiveData<Double> rectangularPanDimension1;
    private MutableLiveData<Double> rectangularPanDimension2;
    private MutableLiveData<Double> circularPanDimension;
    private MutableLiveData<Double> bundtPanDimension1;
    private MutableLiveData<Double> bundtPanDimension2;
    private MutableLiveData<ConversionFactorsRecord> bundtConversionFactor;
    private MutableLiveData<ConversionFactorsRecord> rectangularConversionFactor;
    private MutableLiveData<ConversionFactorsRecord> circularConversionFactor;

    //Mediator Live Data
    private final MediatorLiveData<Boolean> isErrorODltID;
    private final MediatorLiveData<Boolean> isErrorIDgtOD;

    public ViewModelPanSize(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);

        //get the sharedPreference livedata value
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplication());

        setupLiveData();
        initLiveData();

        isErrorIDgtOD = new MediatorLiveData<>();
        mediatorIsErrorIDgtODInit();
        isErrorODltID = new MediatorLiveData<>();
        mediatorIsErrorODltIDInit();
    }

    /**
     * add sources for checking whether ID is less than OD
     * only observe changes to ID value - only want to throw error when editing ID value
     */
    private void mediatorIsErrorIDgtODInit() {
        isErrorIDgtOD.addSource(bundtPanDimension2, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                isErrorIDgtOD.setValue(bundtPanDimension1.getValue() < bundtPanDimension2.getValue());
            }
        });
    }

    /**
     * add sources for checking whether OD is less than ID
     * only observe changes to OD value - only want to throw error when editing OD value
     */
    private void mediatorIsErrorODltIDInit() {
        isErrorODltID.addSource(bundtPanDimension1, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                isErrorODltID.setValue(bundtPanDimension1.getValue() < bundtPanDimension2.getValue());
            }
        });
    }

    //Getters
    public LiveData<List<ConversionFactorsRecord>> getConversionFactorsRecordLiveData() { return conversionFactorsRecordLiveData; }
    public LiveData<Integer> getPanTypeMutable() {
        return panTypeMutable;
    }
    public LiveData<Double> getRectangularPanDimension1() {
        return rectangularPanDimension1;
    }
    public LiveData<Double> getRectangularPanDimension2() {
        return rectangularPanDimension2;
    }
    public LiveData<Double> getCircularPanDimension() {
        return circularPanDimension;
    }
    public LiveData<Double> getBundtPanDimension1() {
        return bundtPanDimension1;
    }
    public LiveData<Double> getBundtPanDimension2() {
        return bundtPanDimension2;
    }
    public LiveData<Boolean> getIsErrorIDgtOD() {
        return isErrorIDgtOD;
    }
    public LiveData<Boolean> getIsErrorODltID() {
        return isErrorODltID;
    }
    public LiveData<ConversionFactorsRecord> getBundtConversionFactor() { return bundtConversionFactor; }
    public LiveData<ConversionFactorsRecord> getRectangularConversionFactor() { return rectangularConversionFactor; }
    public LiveData<ConversionFactorsRecord> getCircularConversionFactor() { return circularConversionFactor; }

    //Setters
    public void setPanTypeMutable(Integer input) {
        panTypeMutable.setValue(input);
    }
    public void setRectangularPanDimension1(Double input) {
        rectangularPanDimension1.setValue(input);
    }
    public void setRectangularPanDimension2(Double input) {
        rectangularPanDimension2.setValue(input);
    }
    public void setCircularPanDimension(Double input) {
        circularPanDimension.setValue(input);
    }
    public void setBundtPanDimension1(Double input) {
        bundtPanDimension1.setValue(input);
    }
    public void setBundtPanDimension2(Double input) {
        bundtPanDimension2.setValue(input);
    }
    public void setBundtConversionFactor(ConversionFactorsRecord input) {
        bundtConversionFactor.setValue(input);
    }
    public void setRectangularConversionFactor(ConversionFactorsRecord input) {
        rectangularConversionFactor.setValue(input);
    }
    public void setCircularConversionFactor(ConversionFactorsRecord input) {
        circularConversionFactor.setValue(input);
    }

    /**
     * Instantiate the livedata
     */
    private void setupLiveData() {
        conversionFactorsRecordLiveData = dataRepository.getAllDistanceConversionFactors();
        panTypeMutable = new MutableLiveData<>();
        rectangularPanDimension1 = new MutableLiveData<>();
        rectangularPanDimension2 = new MutableLiveData<>();
        circularPanDimension = new MutableLiveData<>();
        bundtPanDimension1 = new MutableLiveData<>();
        bundtPanDimension2 = new MutableLiveData<>();
        bundtConversionFactor = new MutableLiveData<>();
        rectangularConversionFactor = new MutableLiveData<>();
        circularConversionFactor = new MutableLiveData<>();
    }

    /**
     * Set initial livedata values as required
     */
    private void initLiveData() {
        panTypeMutable.setValue(0);
        rectangularPanDimension1.setValue(0.0);
        rectangularPanDimension2.setValue(0.0);
        circularPanDimension.setValue(0.0);
        bundtPanDimension1.setValue(0.0);
        bundtPanDimension2.setValue(0.0);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                bundtConversionFactor.postValue(dataRepository.getSingleConversionFactorNonLive(preferences.getInt(KEY_PREF_DEFAULT_DISTANCE_UNIT,18)));
            }
        });
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                circularConversionFactor.postValue(dataRepository.getSingleConversionFactorNonLive(preferences.getInt(KEY_PREF_DEFAULT_DISTANCE_UNIT,18)));
            }
        });
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                rectangularConversionFactor.postValue(dataRepository.getSingleConversionFactorNonLive(preferences.getInt(KEY_PREF_DEFAULT_DISTANCE_UNIT,18)));
            }
        });
    }

    /**
     * Save the data to the baking fragment viewmodel as input pan sizes
     * @param viewModelBaking ViewModelBaking that data is saved to
     */
    public boolean saveDataAsInput(ViewModelBaking viewModelBaking) {
        viewModelBaking.setPanTypeInputMutable(panTypeMutable.getValue());
        if (panTypeMutable.getValue() == 0) {
            viewModelBaking.setInputConversionFactor(rectangularConversionFactor.getValue());
            viewModelBaking.setInputPanDimension1(rectangularPanDimension1.getValue());
            viewModelBaking.setInputPanDimension2(rectangularPanDimension2.getValue());
            return true;
        } else if (panTypeMutable.getValue() == 1) {
            viewModelBaking.setInputConversionFactor(circularConversionFactor.getValue());
            viewModelBaking.setInputPanDimension1(circularPanDimension.getValue());
            viewModelBaking.setInputPanDimension2(0.0);
            return true;
        } else if (panTypeMutable.getValue() == 2) {
            if (!isErrorODltID.getValue() && !isErrorIDgtOD.getValue()) {
                viewModelBaking.setInputConversionFactor(bundtConversionFactor.getValue());
                viewModelBaking.setInputPanDimension1(bundtPanDimension1.getValue());
                viewModelBaking.setInputPanDimension2(bundtPanDimension2.getValue());
                return true;
            } else {
                Toast.makeText(getApplication(),"Bundt dimensions incorrect", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(getApplication(),"Pan Type Error", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * Save the data to the baking fragment viewmodel as output pan sizes
     * @param viewModelBaking ViewModelBaking that data is saved to
     */
    public boolean saveDataAsOutput(ViewModelBaking viewModelBaking) {
        viewModelBaking.setPanTypeOutputMutable(panTypeMutable.getValue());
        if (panTypeMutable.getValue() == 0) {
            viewModelBaking.setOutputConversionFactor(rectangularConversionFactor.getValue());
            viewModelBaking.setOutputPanDimension1(rectangularPanDimension1.getValue());
            viewModelBaking.setOutputPanDimension2(rectangularPanDimension2.getValue());
            return true;
        } else if (panTypeMutable.getValue() == 1) {
            viewModelBaking.setOutputConversionFactor(circularConversionFactor.getValue());
            viewModelBaking.setOutputPanDimension1(circularPanDimension.getValue());
            viewModelBaking.setOutputPanDimension2(0.0);
            return true;
        } else if (panTypeMutable.getValue() == 2) {
            if (!isErrorODltID.getValue() && !isErrorIDgtOD.getValue()) {
                viewModelBaking.setOutputConversionFactor(bundtConversionFactor.getValue());
                viewModelBaking.setOutputPanDimension1(bundtPanDimension1.getValue());
                viewModelBaking.setOutputPanDimension2(bundtPanDimension2.getValue());
                return true;
            } else {
                Toast.makeText(getApplication(),"Bundt dimensions incorrect", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(getApplication(),"Pan Type Error", Toast.LENGTH_LONG).show();
            return false;
        }

    }

}
