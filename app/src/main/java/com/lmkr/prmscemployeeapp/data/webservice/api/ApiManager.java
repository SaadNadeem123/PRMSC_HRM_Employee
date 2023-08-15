package com.lmkr.prmscemployeeapp.data.webservice.api;

import android.util.Log;

import com.google.gson.JsonObject;
import com.lmkr.prmscemployeeapp.App;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.AppWideWariables;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static ApiManager Instance = null;

    public static ApiManager getInstance() {
        if (Instance == null) {
            Instance = new ApiManager();
        }
        return Instance;
    }

    public void getToken() {

        boolean shouldRefresh = SharedPreferenceHelper.getBoolean(SharedPreferenceHelper.SHOULD_REFRESH_TOKEN, App.getInstance());
        if (!shouldRefresh) {
            return;
        }

        UserData userData = SharedPreferenceHelper.getLoggedinUser(App.getInstance());

        JsonObject body = new JsonObject();
        body.addProperty("email", userData.getBasicData().get(0).getEmail());
        body.addProperty("password", SharedPreferenceHelper.getString(SharedPreferenceHelper.PASSWORD, App.getInstance()));  //3132446990
        body.addProperty("source", AppWideWariables.SOURCE_MOBILE);  //3132446990

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        Urls urls = retrofit.create(Urls.class);

        Call<UserData> call = urls.loginUser(body);

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                Log.i("response", response.toString());

                if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_GET, response, null)) {
                    if (!response.isSuccessful()) {
//                    tv.setText("Code :" + response.code());
                        return;
                    }

                    if (response.body() != null && response.body().getMessage() == null) {
                        if (response.body().getBasicData() != null && response.body().getBasicData().size() > 0 && response.body().getBasicData().get(0).getApplication_access().equalsIgnoreCase("yes")) {
                            SharedPreferenceHelper.setLoggedinUser(App.getInstance(), response.body());
                            SharedPreferenceHelper.saveBoolean(SharedPreferenceHelper.SHOULD_REFRESH_TOKEN, false, App.getInstance());
                        } else {
                            SharedPreferenceHelper.saveBoolean(SharedPreferenceHelper.SHOULD_REFRESH_TOKEN, true, App.getInstance());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                t.printStackTrace();
                Log.i("response", t.toString());
                SharedPreferenceHelper.saveBoolean(SharedPreferenceHelper.SHOULD_REFRESH_TOKEN, true, App.getInstance());
                AppUtils.ApiError(t,null);
            }
        });

    }
}
