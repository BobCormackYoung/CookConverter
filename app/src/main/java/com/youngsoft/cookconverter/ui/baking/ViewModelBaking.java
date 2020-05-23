package com.youngsoft.cookconverter.ui.baking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelBaking extends ViewModel {

    private MutableLiveData<String> mText;

    public ViewModelBaking() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}