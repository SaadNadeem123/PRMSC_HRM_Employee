package com.lmkr.prmscemployeeapp.viewModel;

import androidx.lifecycle.ViewModel;

import com.lmkr.prmscemployeeapp.data.webservice.api.ApiCalls;
import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
import com.lmkr.prmscemployeeapp.data.webservice.models.AddContactModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactViewModel extends ViewModel {

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_ADD_EMERGENCY_URL)
            .addConverterFactory(GsonConverterFactory.create()).build();
    private Urls contactApiService = retrofit.create(Urls.class);

    public void createEmergencyContact(String bearerToken, int employeeId, AddContactModel contact, Callback<Void> callback) {
        Call<Void> call = contactApiService.createEmergencyContact(bearerToken, employeeId, contact);
        call.enqueue(callback);
    }

    public void updateEmergencyContact(String bearerToken, int contactId, AddContactModel contact, Callback<Void> callback) {
        Call<Void> call = contactApiService.updateEmergencyContact(bearerToken, contactId, contact);
        call.enqueue(callback);
    }
}

