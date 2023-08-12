package com.lmkr.prmscemployeeapp.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lmkr.prmscemployeeapp.data.database.models.AttendanceHistory;
import com.lmkr.prmscemployeeapp.data.database.models.LeaveRequest;
import com.lmkr.prmscemployeeapp.data.repository.AttendanceHistoryRepository;
import com.lmkr.prmscemployeeapp.data.repository.EmergencyContactRepository;
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

public class EmergencyContactViewModel extends AndroidViewModel {

    private LiveData<List<EmergencyContact>> emergencyContactsLiveData;
    private EmergencyContactRepository emergencyContactRepository;

    public EmergencyContactViewModel(@NonNull Application application) {
        super(application);
        emergencyContactRepository = new EmergencyContactRepository(application);
        emergencyContactsLiveData = emergencyContactRepository.getAllEmergencyContacts();
    }


    public void getEmergencyContacts(String token, int employeeId) {
        /*if (emergencyContactsLiveData == null) {
            emergencyContactsLiveData = new MutableLiveData<>();
        }
*/
        loadEmergencyContacts(token, employeeId);
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
                       // emergencyContactsLiveData.postValue(emergencyContacts);
                        insert(emergencyContacts);
                        

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


    public void insert(EmergencyContact emergencyContact) {
        emergencyContactRepository.insert(emergencyContact);
    }

    public void insert(List<EmergencyContact> emergencyContactList) {
        emergencyContactRepository.insert(emergencyContactList);
    }

    public void update(EmergencyContact emergencyContact) {
        emergencyContactRepository.update(emergencyContact);
    }

    public void delete(EmergencyContact emergencyContact) {
        emergencyContactRepository.delete(emergencyContact);
    }

    public void deleteAllAttendanceHistory() {
        emergencyContactRepository.deleteAllEmergencyContacts();
    }


    public LiveData<List<EmergencyContact>> getEmergencyContactsLiveData() {
        return emergencyContactsLiveData;
    }
}

