package com.lmkr.prmscemployeeapp.ui.utilities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by AhmedAbbas on 12/12/2017.
 */

public class PermissionsRequest {
    public static final int LOCATION_REQUEST_CODE = 0;
    public static final int LOACTION_ENABLE_REQUEST = 1;
    public static final String LOCATION_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String LOCATION_FINE = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String[] LOCATION_PERMISSIONS = new String[]{LOCATION_FINE, LOCATION_COARSE};
    public static final String[] LOCATION_PERMISSIONS_FINE = new String[]{LOCATION_FINE};
    public static final String[] LOCATION_PERMISSIONS_COARSE = new String[]{LOCATION_COARSE};
    public static final int CAMERA_REQUEST = 2;
    public static final int CALL_REQUEST_CODE = 2;
    public static final int SMS_REQUEST_CODE = 3;
    public static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;



    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Storage Permission");
                    alertBuilder.setMessage("External storage permission is necessary to download file");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean checkLocationPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PermissionsRequest.LOCATION_REQUEST_CODE);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
