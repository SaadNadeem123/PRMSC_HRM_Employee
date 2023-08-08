package com.lmkr.prmscemployeeapp.ui.myinfo.addContact;

import androidx.lifecycle.ViewModel;

import retrofit2.Call;
import retrofit2.Callback;

public class ContactViewModel extends ViewModel {
    private ContactApiService contactApiService = ContactRetrofitClient.getRetrofitInstance().create(ContactApiService.class);

    public void createEmergencyContact(String bearerToken, int employeeId, AddContactModel contact, Callback<Void> callback) {
        Call<Void> call = contactApiService.createEmergencyContact(bearerToken, employeeId, contact);
        call.enqueue(callback);
    }

    public void updateEmergencyContact(String bearerToken, int contactId, AddContactModel contact, Callback<Void> callback) {
        Call<Void> call = contactApiService.updateEmergencyContact(bearerToken, contactId, contact);
        call.enqueue(callback);
    }
}

