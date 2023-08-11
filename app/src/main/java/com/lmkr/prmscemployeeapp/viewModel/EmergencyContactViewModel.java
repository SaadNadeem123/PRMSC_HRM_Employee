package com.lmkr.prmscemployeeapp.viewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lmkr.prmscemployeeapp.data.webservice.api.ApiCalls;
import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
import com.lmkr.prmscemployeeapp.data.webservice.models.EmergencyApiResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.EmergencyContact;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmergencyContactViewModel extends ViewModel {

    private MutableLiveData<List<EmergencyContact>> emergencyContactsLiveData;

    public LiveData<List<EmergencyContact>> getEmergencyContacts(String token, int employeeId) {
        if (emergencyContactsLiveData == null) {
            emergencyContactsLiveData = new MutableLiveData<>();
        }

        loadEmergencyContacts(token, employeeId);
        return emergencyContactsLiveData;
    }

    public void loadEmergencyContacts(String token, int employeeId) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_EMERGENCY_URL).addConverterFactory(GsonConverterFactory.create()).build();
        Urls employeeApi = retrofit.create(Urls.class);
        Call<EmergencyApiResponse> call = employeeApi.getEmergencyContact(token, employeeId);
        call.enqueue(new Callback<EmergencyApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<EmergencyApiResponse> call, @NonNull Response<EmergencyApiResponse> response) {
                if (response.isSuccessful()) {
                    EmergencyApiResponse emergencyApiResponse = response.body();
                    if (emergencyApiResponse != null) {
                        List<EmergencyContact> emergencyContacts = emergencyApiResponse.getEmergencyContacts();
                        emergencyContactsLiveData.postValue(emergencyContacts);
                    }
                } else {
                    Log.e("APIError", "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<EmergencyApiResponse> call, Throwable t) {
                Log.e("APIError", "Network error: " + t.getMessage());
            }
        });
    }
}

