package com.youngsoft.cookconverter.ui.measures;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelMeasures extends ViewModel {

    private MutableLiveData<String> mText;

    public ViewModelMeasures() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}