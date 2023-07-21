package com.lmkr.prmscemployeeapp.ui.myinfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class myinfoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public myinfoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my info fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}