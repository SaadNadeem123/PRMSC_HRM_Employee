package com.lmkr.prmscemployeeapp.ui.bulletin;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BulletinViewModel extends ViewModel {

    private MutableLiveData<List<BulletinModel>> bulletinList = new MutableLiveData<>();
    private static final String BASE_URL = "http://20.55.70.106:8000/api/v1/";

    public LiveData<List<BulletinModel>> getBulletinList() {
        return bulletinList;
    }

    public void fetchBulletinData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BulletinService service = retrofit.create(BulletinService.class);
        Call<BulletinResponse> call = service.getBulletinList();

        call.enqueue(new Callback<BulletinResponse>() {
            @Override
            public void onResponse(@NonNull Call<BulletinResponse> call, @NonNull Response<BulletinResponse> response) {
                if (response.isSuccessful()) {
                    BulletinResponse bulletinResponse = response.body();
                    assert bulletinResponse != null;
                    List<BulletinModel> bulletins = bulletinResponse.getNews();
                    bulletinList.setValue(bulletins);
                } else {
                    // Handle the error
                }
            }

            @Override
            public void onFailure(@NonNull Call<BulletinResponse> call, @NonNull Throwable t) {
                // Handle the failure
                t.printStackTrace();
            }
        });
    }
}