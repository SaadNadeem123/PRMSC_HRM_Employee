package com.lmkr.prmscemployeeapp.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lmkr.prmscemployeeapp.App;
import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.data.webservice.api.ApiCalls;
import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;
import com.lmkr.prmscemployeeapp.databinding.ActivityLoginBinding;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.AppWideWariables;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();
    }

    @Override
    public void initializeViews() {
        setListeners();
    }

    @Override
    public void setListeners() {
        binding.password.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                }
                return false;
            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    @Override
    public void handleIntent() {

    }

    @Override
    public void callApi() {

        if (!AppUtils.checkNetworkState(LoginActivity.this)) {
            return;
        }


        binding.loading.setVisibility(View.VISIBLE);

        JsonObject body = new JsonObject();
        body.addProperty("email", binding.username.getText().toString());
        body.addProperty("password", binding.password.getText().toString());  //3132446990
        body.addProperty("source", AppWideWariables.SOURCE_MOBILE);  //3132446990

        SharedPreferenceHelper.saveString(SharedPreferenceHelper.PASSWORD, binding.password.getText().toString(), LoginActivity.this);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        Urls urls = retrofit.create(Urls.class);

        Call<UserData> call = urls.loginUserApi(body);
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                Log.i("response", response.toString());
                binding.loading.setVisibility(View.GONE);

                if (!AppUtils.isErrorResponse(response, LoginActivity.this)) {
                    if (!response.isSuccessful()) {
//                    tv.setText("Code :" + response.code());
                        return;
                    }

                    if (response.body() != null && response.body().getMessage() == null) {
                        if(response.body().getBasicData()!=null && response.body().getBasicData().size()>0 && response.body().getBasicData().get(0).getApplication_access().equalsIgnoreCase("yes")) {
                            SharedPreferenceHelper.setLoggedinUser(getApplicationContext(), response.body());
                            AppUtils.switchActivity(LoginActivity.this, MainActivity.class, null);
                            finish();
                        }
                        else {
                            AppUtils.makeNotification(getString(R.string.app_access_denied),LoginActivity.this);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                t.printStackTrace();
                AppUtils.makeNotification(t.toString(), LoginActivity.this);
                binding.loading.setVisibility(View.GONE);
            }
        });

    }/*
    @Override
    public void callApi() {
        binding.loading.setVisibility(View.VISIBLE);


        JsonObject body = new JsonObject();
        body.addProperty("email", binding.username.getText().toString());
        body.addProperty("password", binding.password.getText().toString());  //3132446990

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        Urls urls = retrofit.create(Urls.class);

        Call<JsonObjectResponse> call = urls.loginUser(body);

        call.enqueue(new Callback<JsonObjectResponse>() {
            @Override
            public void onResponse(Call<JsonObjectResponse> call, Response<JsonObjectResponse> response) {
                Log.i("response", response.toString());
                binding.loading.setVisibility(View.GONE);



                if (!response.isSuccessful()) {
//                    tv.setText("Code :" + response.code());
                    return;
                }

                JsonObjectResponse jsonObjectResponse = response.body();

                Log.i("response", jsonObjectResponse.toString());

                if (jsonObjectResponse != null && jsonObjectResponse.getResponse() != null) {
                    if (response.body().isStatus()) {
                        UserData userData = (new Gson()).fromJson(response.body().getResponse(), UserData.class);
                        //                    Log.i("response", userData.getUserToken());
                        SharedPreferenceHelper.setLoggedinUser(getApplicationContext(), userData);
                        AppUtils.switchActivity(LoginActivity.this, MainActivity.class, null);
                        finish();

                    } else {
//                        AppUtils.makeNotification(response.body().getMessage(),LoginActivity.this);
                    }
                } else {
//                    AppUtils.makeNotification(jsonObjectResponse.getMessage(), LoginActivity.this);
                }
            }

            @Override
            public void onFailure(Call<JsonObjectResponse> call, Throwable t) {
                t.printStackTrace();
                AppUtils.makeNotification(t.toString(), LoginActivity.this);
                binding.loading.setVisibility(View.GONE);
                Log.i("response", t.toString());
//                tv.setText(t.getMessage());
            }
        });

    }*/

    @Override
    public void internetConnectionChangeListener(boolean isConnected) {

    }

    private void login() {
        if (binding.username.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(binding.username.getText().toString()).matches()) {
            AppUtils.makeNotification("Enter valid Email address !", LoginActivity.this);
            return;
        }
        if (binding.password.getText().toString().isEmpty() || binding.password.getText().toString().length() < 6) {
            AppUtils.makeNotification("Enter valid password !", LoginActivity.this);
            return;
        }

        callApi();


    }


}