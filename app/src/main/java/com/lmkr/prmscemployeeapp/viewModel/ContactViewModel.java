package com.lmkr.prmscemployeeapp.viewModel;

import static com.lmkr.prmscemployeeapp.data.webservice.api.ApiCalls.BASE_EMERGENCY_URL;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
import com.lmkr.prmscemployeeapp.data.webservice.models.AddContactModel;
import com.lmkr.prmscemployeeapp.data.webservice.models.ApiBaseResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactViewModel extends ViewModel {
    private MutableLiveData<Boolean> createContactResult = new MutableLiveData<>();

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_EMERGENCY_URL)
            .addConverterFactory(GsonConverterFactory.create()).build();
    private Urls contactServiceUrls = retrofit.create(Urls.class);




    public void createEmergencyContact(String bearerToken, AddContactModel data, Callback<ApiBaseResponse> callback) {
        Call<ApiBaseResponse> call = contactServiceUrls.createEmergencyContact(bearerToken, data.getEmployeeId(), data);
        call.enqueue(callback);
    }

    public void updateEmergencyContact(String bearerToken, int contactId, AddContactModel contact, Callback<ApiBaseResponse> callback) {
        Call<ApiBaseResponse> call = contactServiceUrls.updateEmergencyContact(bearerToken, contactId, contact);
        call.enqueue(callback);
    }
}
