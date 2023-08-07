
package com.lmkr.prmscemployeeapp.data.webservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Chronometer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lmkr.prmscemployeeapp.App;
import com.lmkr.prmscemployeeapp.data.webservice.api.ApiCalls;
import com.lmkr.prmscemployeeapp.data.webservice.api.JsonObjectResponse;
import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;
import com.lmkr.prmscemployeeapp.ui.broadcastReceivers.ConnectivityReceiver;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.AppWideWariables;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Chronometer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lmkr.prmscemployeeapp.data.webservice.api.ApiCalls;
import com.lmkr.prmscemployeeapp.data.webservice.api.JsonObjectResponse;
import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class TokenBoundService extends Service {
    public static final String REFRESH_TOKEN = "refreshToken";
    private static final String LOG_TAG = "BoundService";
    public static String RESET_TIMER = "resetTimer";
    private final IBinder mBinder = new MyBinder();
    private final Timer timerManualSync = new Timer();
    private final int FIRSTRUN = 0;//FIRST RUN
    //    private int RUNTIMER = 180 * 1000;//timer run one another 3mins
    private final int RUNTIMER = 5 * 1000;//timer run one another 1mins
    private Chronometer mChronometer;
    private Timer timer = new Timer();
    private int count;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(LOG_TAG, "in onCreate");
        resetTimer();
        mChronometer = new Chronometer(this);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
        count = 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "in onBind");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(LOG_TAG, "in onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(LOG_TAG, "in onUnbind");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "in onDestroy");
        mChronometer.stop();
    }

    public String getTimestamp() {
        long elapsedMillis = SystemClock.elapsedRealtime() - mChronometer.getBase();
        int hours = (int) (elapsedMillis / 3600000);
        int minutes = (int) (elapsedMillis - hours * 3600000) / 60000;
        int seconds = (int) (elapsedMillis - hours * 3600000 - minutes * 60000) / 1000;
        int millis = (int) (elapsedMillis - hours * 3600000 - minutes * 60000 - seconds * 1000);
        String timestamp = hours + ":" + minutes + ":" + seconds + ":" + millis;
        Log.i("HitTime = ", timestamp);
        Log.i("HitCount = ", count + "");
        return timestamp;
    }

    public void resetTimer() {

        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = new Timer();
        }
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    sendRequestToServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, FIRSTRUN, RUNTIMER);

    }

    private void sendRequestToServer() {
        if (checkConnection()) {

            getToken();

        }
    }

    private void getToken() {

        boolean shouldRefresh = SharedPreferenceHelper.getBoolean(SharedPreferenceHelper.SHOULD_REFRESH_TOKEN, TokenBoundService.this);
        if (!shouldRefresh) {
            return;
        }


        ++count;
        getTimestamp();

        UserData userData = SharedPreferenceHelper.getLoggedinUser(TokenBoundService.this);

        JsonObject body = new JsonObject();
        body.addProperty("email", userData.getBasicData().get(0).getEmail());
        body.addProperty("password", SharedPreferenceHelper.getString(SharedPreferenceHelper.PASSWORD,TokenBoundService.this));  //3132446990
        body.addProperty("source", AppWideWariables.SOURCE_MOBILE);  //3132446990

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        Urls urls = retrofit.create(Urls.class);

        Call<JsonObjectResponse> call = urls.loginUser(body);

        call.enqueue(new Callback<JsonObjectResponse>() {
            @Override
            public void onResponse(Call<JsonObjectResponse> call, Response<JsonObjectResponse> response) {
                Log.i("response", response.toString());


                if (!response.isSuccessful()) {
//                    tv.setText("Code :" + response.code());
                    return;
                }
                JsonObjectResponse jsonObjectResponse = response.body();

                Log.i("response", jsonObjectResponse.toString());

                if (jsonObjectResponse != null && jsonObjectResponse.getResponse() != null) {

                    UserData userData = (new Gson()).fromJson(response.body().getResponse(), UserData.class);
                    Log.i("response", userData.getToken());
                    SharedPreferenceHelper.setLoggedinUser(getApplicationContext(), userData);

                    SharedPreferenceHelper.saveBoolean(SharedPreferenceHelper.SHOULD_REFRESH_TOKEN, false, TokenBoundService.this);

                    callBroadcastRefreshToken(REFRESH_TOKEN);

                } else {
                    SharedPreferenceHelper.saveBoolean(SharedPreferenceHelper.SHOULD_REFRESH_TOKEN, true, TokenBoundService.this);
                }
            }

            @Override
            public void onFailure(Call<JsonObjectResponse> call, Throwable t) {
                t.printStackTrace();
                Log.i("response", t.toString());
                SharedPreferenceHelper.saveBoolean(SharedPreferenceHelper.SHOULD_REFRESH_TOKEN, true, TokenBoundService.this);
            }
        });

    }

    // Method to manually check connection status
    private boolean checkConnection() {
        return ConnectivityReceiver.isConnected();
    }

    public String getDeviceId() {
        //        return FirebaseInstanceId.getInstance().getToken();
        return null;
    }

    public String getAppVersion() {
        return AppUtils.getAppBuildVersionName();
    }

    private void callBroadcastRefreshToken(String data) {
        try {
            Intent verifyIntent = new Intent(data);
            verifyIntent.putExtra("message", "refreshToken");
            sendBroadcast(verifyIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyBinder extends Binder {
        public TokenBoundService getService() {
            return TokenBoundService.this;
        }
    }

    public class ResetTimerClass extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (TextUtils.equals(intent.getAction(), RESET_TIMER)) {
                resetTimer();
            }
        }
    }
}
