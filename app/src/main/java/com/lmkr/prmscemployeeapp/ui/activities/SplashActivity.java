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
    
        Bundle b = getIntent().getExtras();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPreferenceHelper.getBoolean(SharedPreferenceHelper.IS_LOGGED_IN, SplashActivity.this)) {
                    AppUtils.switchActivity(SplashActivity.this, MainActivity.class, b);
                } else {
                    AppUtils.switchActivity(SplashActivity.this, LoginActivity.class, null);
                }
                finish();
            }
        },3000);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}