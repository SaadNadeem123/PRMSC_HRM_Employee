package com.lmkr.prmscemployeeapp.ui.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.lmkr.prmscemployeeapp.App;
import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;

public class SharedPreferenceHelper {

    public static final String BID_ID = "bid_id";
    public static String TOKEN = "Token";
    public static String IS_LOGGED_IN = "isLoggedIn";
    public static String FIRE_BASE_TOKEN = "firebaseToken";
    public static String IS_FIRST_TIME = "isFirstTime";
    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    public static String PREFRENCESNAME = App.getInstance().getResources().getString(R.string.app_name);
    public static String ATTEMPTS_START_TIME = "attemptStartTime";
    public static String ATTEMPTS_COUNT = "attemptCount";
    public static String USERDATA = "userData";

    public static String IS_CHECKED_IN = "isCheckedIn";

    public static final String ORIGIN_CITY = "origin";
    public static final String DESTINATION_CITY = "destination";
    public static final String VEHICLE_SIZE = "vehicleSize";
    public static final String VEHICLE_TYPE = "vehicleType";
    public static final String SHOULD_REFRESH_TOKEN = "shouldRefreshToken";
    public static final String BID_STATUS = "bidStatus";
    public static final String BID_COUNT = "bidCount";


    public static void saveFloat(String key, float value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFRENCESNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static float getFloat(String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFRENCESNAME, Context.MODE_PRIVATE);
//        return preferences.getString(key, "NaN");
        return preferences.getFloat(key, 0);
    }

    public static void saveString(String key, String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFRENCESNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveInt(String key, int value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFRENCESNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getString(String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFRENCESNAME, Context.MODE_PRIVATE);
//        return preferences.getString(key, "NaN");
        if (preferences.contains(key)) {
            return preferences.getString(key, "");
        } else {
            return "";
        }
    }

    public static int getInt(String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFRENCESNAME, Context.MODE_PRIVATE);
//        return preferences.getString(key, "NaN");
        if (preferences.contains(key)) {
            return preferences.getInt(key, 0);
        } else {
            return 0;
        }
    }

    public static void saveBoolean(String key, boolean value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFRENCESNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFRENCESNAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    public static void saveBooleanFile(String key, boolean value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFRENCESNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void saveSyncBoolean(String key, boolean value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFRENCESNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getSyncBoolean(String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFRENCESNAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, true);
    }

    public static void clearPrefrences(Context context) {

        String firebaseToken = getString(FIRE_BASE_TOKEN, context);
        SharedPreferences preferences = context.getSharedPreferences(PREFRENCESNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();
        editor.commit();
        saveString(FIRE_BASE_TOKEN, firebaseToken, context);
    }

    public static void remove(String key, Context context) {

        SharedPreferences preferences = context.getSharedPreferences(PREFRENCESNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key).commit();
        editor.commit();
    }

    public static boolean has(String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFRENCESNAME, Context.MODE_PRIVATE);
        return preferences.contains(key);
    }

    public static UserData getLoggedinUser(Context context) {
        String userdata = getString(USERDATA, context);

        UserData user = (new Gson()).fromJson(userdata, UserData.class);

        return user;
    }

    public static void setLoggedinUser(Context context, UserData userdata) {
        saveString(USERDATA, (new Gson()).toJson(userdata), context);
        saveBoolean(SharedPreferenceHelper.IS_LOGGED_IN,true,context);
    }

    public static boolean isCheckedIn(Context context) {
        Boolean isCheckedIn = getBoolean(IS_CHECKED_IN, context);

        return isCheckedIn;
    }
}

