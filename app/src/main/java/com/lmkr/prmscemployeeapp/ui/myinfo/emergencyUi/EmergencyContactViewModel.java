package com.lmkr.prmscemployeeapp.ui.myinfo.emergencyUi;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        EmployeeApi employeeApi = RetrofitClient.getRetrofitInstance().create(EmployeeApi.class);
        Call<ApiResponse> call = employeeApi.getEmergencyContact(token, employeeId);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        List<EmergencyContact> emergencyContacts = apiResponse.getEmergencyContacts();
                        emergencyContactsLiveData.postValue(emergencyContacts);
                    }
                } else {
                    // Handle the error response here
                    Log.e("APIError", "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // Handle the network error here
                Log.e("APIError", "Network error: " + t.getMessage());
            }
        });
    }
}

