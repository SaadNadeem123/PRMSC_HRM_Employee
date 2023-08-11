package com.lmkr.prmscemployeeapp.ui.myinfo.addContact;

import static com.lmkr.prmscemployeeapp.data.webservice.api.ApiCalls.BASE_EMERGENCY_URL;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
import com.lmkr.prmscemployeeapp.viewModel.EmergencyContactViewModel;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactViewModel extends ViewModel {
    private MutableLiveData<Boolean> createContactResult = new MutableLiveData<>();

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_EMERGENCY_URL)
            .addConverterFactory(GsonConverterFactory.create()).build();
    private Urls contactServiceUrls = retrofit.create(Urls.class);




    public void createEmergencyContact(String bearerToken, AddContactModel data, Callback<Void> callback) {
        Call<Void> call = contactServiceUrls.createEmergencyContact(bearerToken, data.getEmployeeId(), data);
        call.enqueue(callback);
    }

    public void updateEmergencyContact(String bearerToken, int contactId, AddContactModel contact, Callback<Void> callback) {
        Call<Void> call = contactServiceUrls.updateEmergencyContact(bearerToken, contactId, contact);
        call.enqueue(callback);
    }
}

