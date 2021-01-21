package com.youngsoft.cookconverter.ui.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.youngsoft.cookconverter.R;
import com.youngsoft.cookconverter.data.ConversionFactorsRecord;

import java.util.List;

public class FragmentPreferences extends PreferenceFragmentCompat implements DialogUnitPicker.OnSaveListener {

    Fragment fragment;
    ViewModelPreferences viewModelPreferences;
    SharedPreferences preferences;
    Preference pfDefaultUnit;
    Preference pfDefaultDistanceUnit;

    //public key values for preferences
    public static final String KEY_PREF_DEFAULT_UNIT = "preference_default_unit";
    public static final String KEY_PREF_DEFAULT_DISTANCE_UNIT = "preference_default_distance_unit";

    //conversion factors lists
    List<ConversionFactorsRecord> allConversionFactors;
    List<ConversionFactorsRecord> allDistanceConversionFactors;


    public FragmentPreferences() {
        //Required empty constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences resource file
        setPreferencesFromResource(R.xml.preferences, rootKey);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Instantiate the viewmodel for the preferences fragment
        // This will contain the sublists for the two measurement types
        viewModelPreferences = new ViewModelProvider(this).get(ViewModelPreferences.class);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //set livedata observers for the lists of conversion factors
        setObservers();

        // Get the shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Shared preference for default displayed mass unit
        pfDefaultUnit = findPreference(KEY_PREF_DEFAULT_UNIT);
        pfDefaultUnit.setSummary(" ");

        // Shared preference for default displayed distance unit
        pfDefaultDistanceUnit = findPreference(KEY_PREF_DEFAULT_DISTANCE_UNIT);
        pfDefaultDistanceUnit.setSummary(" ");

        // Set an onclick listener for the mass unit preference
        // Allows picking the mass unit from a list tied to the viewmodel livedata
        pfDefaultUnit.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                DialogUnitPicker massUnitPickerFragment = new DialogUnitPicker(FragmentPreferences.this, KEY_PREF_DEFAULT_UNIT, allConversionFactors, pfDefaultUnit);
                massUnitPickerFragment.show(getChildFragmentManager(), "defaultUnitPickerFragment");
                return true;
            }
        });

        // Set an onclick listener for the distance unit preference
        // Allows picking the mass unit from a list tied to the viewmodel livedata
        pfDefaultDistanceUnit.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                DialogUnitPicker distanceUnitPickerFragment = new DialogUnitPicker(FragmentPreferences.this, KEY_PREF_DEFAULT_DISTANCE_UNIT, allDistanceConversionFactors, pfDefaultDistanceUnit);
                distanceUnitPickerFragment.show(getChildFragmentManager(), "defaultDistanceUnitPickerFragment");
                return true;
            }
        });

    }

    /**
     * search through a list of ConversionFactorRecords for a specific id
     * @param preferenceValue the ConversionFactorRecord ID value of interest
     * @param list the list of ConversionFactorRecords to search
     * @return the name of the ConversionFactorRecord
     */
    private String searchList(int preferenceValue, List<ConversionFactorsRecord> list) {
        for (int i = 0; i < list.size()-1; i++) {
            ConversionFactorsRecord conversionFactorsRecord = list.get(i);
            if (conversionFactorsRecord.getConversionFactorID() == preferenceValue) {
                return conversionFactorsRecord.getName(); //return the conversion factor name
            }
        }
        return "error"; //return an error if can't find the requested list item
    }

    /**
     * set observers for the livedata from the viewmodel
     */
    private void setObservers() {
        //observe changes to the list of mass conversion factors
        viewModelPreferences.getAllConversionFactors().observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
                //massConversionFactors.clear();
                allConversionFactors = conversionFactorsRecords;
                updateUnitPreferenceView(KEY_PREF_DEFAULT_UNIT, allConversionFactors, pfDefaultUnit);
            }
        });

        //observe changes to the list of mass conversion factors
        viewModelPreferences.getAllDistanceConversionFactors().observe(getViewLifecycleOwner(), new Observer<List<ConversionFactorsRecord>>() {
            @Override
            public void onChanged(List<ConversionFactorsRecord> conversionFactorsRecords) {
                //massConversionFactors.clear();
                allDistanceConversionFactors = conversionFactorsRecords;
                updateUnitPreferenceView(KEY_PREF_DEFAULT_DISTANCE_UNIT, allDistanceConversionFactors, pfDefaultDistanceUnit);
            }
        });
    }


    public void updateUnitPreferenceView(String key_pref, List<ConversionFactorsRecord> conversionFactorsList, Preference preference) {
        int preferenceValue = preferences.getInt(key_pref,1);
        String preferenceName = searchList(preferenceValue, conversionFactorsList);
        preference.setSummary(preferenceName);
    }

    @Override
    public void onSave(ConversionFactorsRecord conversionFactorsRecord, String keyPref, Preference preference, List<ConversionFactorsRecord> conversionFactorsList) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(keyPref, (int) conversionFactorsRecord.getConversionFactorID());
        editor.apply();
        updateUnitPreferenceView(keyPref, conversionFactorsList, preference);
    }

}
