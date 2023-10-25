package com.lmkr.prmscemployeeapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPreferenceHelper.getBoolean(SharedPreferenceHelper.IS_LOGGED_IN, SplashActivity.this)) {
                    AppUtils.switchActivity(SplashActivity.this, NotificationActivity.class, getIntent().getExtras());
//                    AppUtils.switchActivity(SplashActivity.this, Notification.class, getIntent().getExtras());
                } else {
                    AppUtils.switchActivity(SplashActivity.this, LoginActivity.class, null);
                }
                finish();
            }
        },3000);
    }
}