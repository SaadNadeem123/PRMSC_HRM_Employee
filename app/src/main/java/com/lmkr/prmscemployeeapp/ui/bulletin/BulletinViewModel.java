package com.lmkr.prmscemployeeapp.ui.bulletin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BulletinViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BulletinViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is bulletin fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}