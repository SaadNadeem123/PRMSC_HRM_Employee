package com.lmkr.prmscemployeeapp.ui.utilities;

import org.jetbrains.annotations.Nullable;

public class AppWideWariables {
    public static final String LEAVE_TYPE_CASUAL = "Casual";
    public static final String LEAVE_TYPE_SICK = "Medical";
    public static final String LEAVE_TYPE_ANNUAL = "Annual";
    public static final float GEOFENCE_RADIUS_IN_METERS = 500;
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 10000;
    public static final String CHECKIN_LAT = "checkInLat";
    public static final String CHECKIN_LONG = "checkInLong";
    @Nullable
    public static final String CHECKIN_SELFIE_URI = "checkinSelfieUri";
    public static final String IS_CHECKED_IN = "isCheckedIn";
    public static final String SOURCE_MOBILE = "mobile";
    public static final String SOURCE_MOBILE_ENUM = "1";
    public static final long HALF_LEAVE_HOUR_LIMIT = 4;
    public static final int API_METHOD_GET = 1;
    public static final int API_METHOD_POST = 2;
    public static final String IS_IN_GEOFENCE = "isInGeofence";
    @Nullable
    public static final String FACE_LOCK_PATH = "faceLockPath";
    public static final int MINIMUM_TIME_DIFF = 30; //in minutes
    public static final String YES = "yes";
    public static final String NO = "no";

    public static final String PENDING = "pending";
    public static final String APPROVED = "approved";
    public static final String REJECTED = "rejected";
}
