package com.lmkr.prmscemployee.ui.utilities.networkUtils;

import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NetworkMonitorListener {
    private static final String TAG = "NetworkStateManager";
    private static NetworkMonitorListener INSTANCE;
    private static final MutableLiveData<Boolean> activeNetworkStatusMLD = new MutableLiveData<>();
    private static final MutableLiveData<NetworkObject> activeNetworkObject = new MutableLiveData<>();
    private static final MutableLiveData<String> activeNetworkStatusDetails = new MutableLiveData<>();
    private NetworkMonitorListener() {}

    public static synchronized NetworkMonitorListener getInstance() {
        if (INSTANCE == null) {
            Log.d(TAG, "getInstance() called: Creating new instance");
            INSTANCE = new NetworkMonitorListener();
        }
        return INSTANCE;
    }

    /**
     * Updates the active network status live-data
     */
    public void setNetworkConnectivityStatus(boolean connectivityStatus) {
        Log.d(TAG, "setNetworkConnectivityStatus() called with: connectivityStatus = [" + connectivityStatus + "]");
        if (Looper.myLooper() == Looper.getMainLooper()) {
            activeNetworkStatusMLD.setValue(connectivityStatus);
        } else {
            activeNetworkStatusMLD.postValue(connectivityStatus);
        }
    }

    public void setNetworkConnectivityStatus( String wifiInfo) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            if(wifiInfo!=null)
            {
                activeNetworkStatusDetails.setValue(wifiInfo);
            }
        } else {
            if(wifiInfo!=null)
            {
                activeNetworkStatusDetails.postValue(wifiInfo);
            }
        }
    }
    public void setNetworkObject(NetworkObject networkObject) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
//            if(networkObject!=null)
//            {
                activeNetworkObject.setValue(networkObject);
//            }
        } else {
//            if(networkObject!=null)
//            {
                activeNetworkObject.postValue(networkObject);
//            }
        }
    }

    /**
     * Returns the current network status
     */
    public LiveData<NetworkObject> getNetworkObject() {
        Log.d(TAG, "getNetworkConnectivityStatus() called");
        return activeNetworkObject;
    }
    public LiveData<Boolean> getNetworkConnectivityStatus() {
        Log.d(TAG, "getNetworkConnectivityStatus() called");
        return activeNetworkStatusMLD;
    }
    public LiveData<String> getNetworkConnectivityStatusDetails() {
        Log.d(TAG, "getNetworkConnectivityStatus() called");
        return activeNetworkStatusDetails;
    }
}
