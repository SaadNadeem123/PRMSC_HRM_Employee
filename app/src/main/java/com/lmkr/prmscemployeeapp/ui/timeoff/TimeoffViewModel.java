package com.lmkr.prmscemployeeapp.ui.timeoff;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimeoffViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TimeoffViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is time off fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}