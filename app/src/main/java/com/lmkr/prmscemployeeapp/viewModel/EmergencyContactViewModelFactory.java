package com.lmkr.prmscemployeeapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EmergencyContactViewModelFactory implements ViewModelProvider.Factory{

    private final Application mApplication;
    private final String date;

    public EmergencyContactViewModelFactory(Application mApplication, String date) {
        this.mApplication = mApplication;
        this.date = date;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EmergencyContactViewModel(mApplication);
    }
}
