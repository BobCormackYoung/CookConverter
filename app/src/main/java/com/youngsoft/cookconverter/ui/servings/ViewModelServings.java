package com.youngsoft.cookconverter.ui.servings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelServings extends ViewModel {

    private MutableLiveData<String> mText;

    public ViewModelServings() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}