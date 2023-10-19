package com.lmkr.prmscemployeeapp.data.firebase;

import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FirebaseTokenListener {
    private static final String TAG = "NetworkStateManager";
    private static FirebaseTokenListener INSTANCE;
    private static final MutableLiveData<String> tokenLiveData = new MutableLiveData<>();
    private FirebaseTokenListener() {}

    public static synchronized FirebaseTokenListener getInstance() {
        if (INSTANCE == null) {
            Log.d(TAG, "getInstance() called: Creating new instance");
            INSTANCE = new FirebaseTokenListener();
        }
        return INSTANCE;
    }

    /**
     * Updates the active network status live-data
     */
    public void setFirebaseUpdatedToken(String token) {
        Log.d(TAG, "setFirebaseUpdatedToken() called with: FirebaseUpdatedToken = [" + token + "]");
        if (Looper.myLooper() == Looper.getMainLooper()) {
            tokenLiveData.setValue(token);
        } else {
            tokenLiveData.postValue(token);
        }
    }

    public LiveData<String> getFirebaseUpdatedToken() {
        Log.d(TAG, "getFirebaseUpdatedToken() called");
        return tokenLiveData;
    }
}
