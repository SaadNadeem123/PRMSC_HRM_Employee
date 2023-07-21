package com.lmkr.prmscemployeeapp.ui.locationUtils;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;


public class LocationService extends Service {

    static final public String SERVICE_NAME = "com.lmkr.prmscemployeeapp.ui.locationUtils.LocationService";
    static final public String MESSAGE = "location_updated";
    private static final String LOG_TAG = "BoundService";
    private static final long INTERVAL = 1000 * 3;
    private static final long FASTEST_INTERVAL = 1000;
    private static final int DISPLACEMENT = 5;
    private final IBinder mBinder = new MyBinder();
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private LocalBroadcastManager broadcaster;
    private LocationRequest locationRequest;

    public void sendResult(String message) {
        Intent intent = new Intent(SERVICE_NAME);
        if (message != null) intent.putExtra(MESSAGE, message);
        broadcaster.sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(LOG_TAG, "in onCreate");
        broadcaster = LocalBroadcastManager.getInstance(this);
        createLocationRequest();
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
        startLocationUpdates();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(LOG_TAG, "in onUnbind");
        stopLocationUpdates();
        return super.onUnbind(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "in onDestroy");
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationClient.flushLocations();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }


    protected void createLocationRequest() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    trackCurrentLocation(location);
                }
            }
        };
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void trackCurrentLocation(Location loc) {
        //save coordinates in shared preferences

        SharedPreferenceHelper.saveString("lat", String.valueOf(loc.getLatitude()), getApplicationContext());
        SharedPreferenceHelper.saveString("long", String.valueOf(loc.getLongitude()), getApplicationContext());
        sendResult("1");

    }

    public class MyBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }
}
