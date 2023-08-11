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

                if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_POST, response, LoginActivity.this)) {
                    if (!response.isSuccessful()) {
//                    tv.setText("Code :" + response.code());
                        return;
                    }

                    if (response.body() != null && response.body().getMessage() == null) {
                        if (response.body().getBasicData() != null && response.body().getBasicData().size() > 0 && response.body().getBasicData().get(0).getApplication_access().equalsIgnoreCase("yes")) {
                            SharedPreferenceHelper.setLoggedinUser(getApplicationContext(), response.body());
                            AppUtils.switchActivity(LoginActivity.this, MainActivity.class, null);
                            finish();
                        } else {
                            AppUtils.makeNotification(getString(R.string.app_access_denied), LoginActivity.this);
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

    }
    @Override
    public void internetConnectionChangeListener(boolean isConnected) {

    }

    private void login() {
        if (binding.username.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(binding.username.getText().toString()).matches()) {
            AppUtils.makeNotification(getResources().getString(R.string.email_invalid_error), LoginActivity.this);
            return;
        }
        if (binding.password.getText().toString().isEmpty() || binding.password.getText().toString().length() < 6) {
            AppUtils.makeNotification(getResources().getString(R.string.password_length_error), LoginActivity.this);
            return;
        }

        callApi();


    }


}