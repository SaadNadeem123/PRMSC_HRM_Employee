package com.lmkr.prmscemployee.viewModel;

import static com.lmkr.prmscemployee.data.webservice.api.ApiCalls.BASE_URL;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lmkr.prmscemployee.data.webservice.models.LeaveManagementModel;
import com.lmkr.prmscemployee.data.webservice.api.Urls;
import com.lmkr.prmscemployee.data.webservice.models.LeaveManagementRequestResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaveManagementViewModel extends ViewModel {

    private MutableLiveData<List<LeaveManagementModel>> leaveManagementList = new MutableLiveData<>();

    public LiveData<List<LeaveManagementModel>> getLeaveManagementList() {
        return leaveManagementList;
    }

    public void fetchLeaveManagementData(String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Urls service = retrofit.create(Urls.class);
        Call<LeaveManagementRequestResponse> call = service.getLeaveRequestManager(token);

        call.enqueue(new Callback<LeaveManagementRequestResponse>() {
            @Override
            public void onResponse(@NonNull Call<LeaveManagementRequestResponse> call, @NonNull Response<LeaveManagementRequestResponse> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        leaveManagementList.setValue(response.body().getLeaveRequest());
                    }
            }

            @Override
            public void onFailure(@NonNull Call<LeaveManagementRequestResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}