package com.lmkr.prmscemployee.viewModel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LeaveRequestViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;
    private final String date;


    public LeaveRequestViewModelFactory(Application application, String date) {
        mApplication = application;
        this.date = date;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new LeaveRequestViewModel(mApplication);
    }
}