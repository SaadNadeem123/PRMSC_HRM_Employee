package com.lmkr.prmscemployeeapp.viewModel;

import static com.lmkr.prmscemployeeapp.data.webservice.api.ApiCalls.BASE_URL;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
import com.lmkr.prmscemployeeapp.data.webservice.models.BulletinModel;
import com.lmkr.prmscemployeeapp.data.webservice.models.BulletinResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BulletinViewModel extends ViewModel {

    private MutableLiveData<List<BulletinModel>> bulletinList = new MutableLiveData<>();

    public LiveData<List<BulletinModel>> getBulletinList() {
        return bulletinList;
    }

    public void fetchBulletinData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Urls service = retrofit.create(Urls.class);
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
                t.printStackTrace();
            }
        });
    }
}