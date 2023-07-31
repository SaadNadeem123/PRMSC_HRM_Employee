package com.lmkr.prmscemployeeapp.ui.timeoff;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lmkr.prmscemployeeapp.App;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;

public class TimeoffViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TimeoffViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is time off fragment");
        mText.setValue(SharedPreferenceHelper.getLoggedinUser(App.getInstance()).getBasicData().get(0).getName());
    }

    public LiveData<String> getText() {
        return mText;
    }
}