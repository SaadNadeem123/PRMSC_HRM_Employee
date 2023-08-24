package com.lmkr.prmscemployeeapp.ui.activities;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

   /* @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }*/

    public abstract void initializeViews();

    public abstract void setListeners();

    public abstract void handleIntent();

    public abstract void callApi();

    public abstract void internetConnectionChangeListener(boolean isConnected);

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppUtils.hideNotification(this);
    }
}
