package com.lmkr.prmscemployee.viewModel;

import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LateCommentAttendanceViewModelListener {
    private static final String TAG = "LateCommentAttendance";
    private static LateCommentAttendanceViewModelListener INSTANCE;
    private static final MutableLiveData<String> comment = new MutableLiveData<>();
    private LateCommentAttendanceViewModelListener() {}

    public static synchronized LateCommentAttendanceViewModelListener getInstance() {
        if (INSTANCE == null) {
            Log.d(TAG, "getInstance() called: Creating new instance");
            INSTANCE = new LateCommentAttendanceViewModelListener();
        }
        return INSTANCE;
    }

    /**
     * Updates the active network status live-data
     */
    public void updateComment(String token) {
        Log.d(TAG, "setFirebaseUpdatedToken() called with: FirebaseUpdatedToken = [" + token + "]");
        if (Looper.myLooper() == Looper.getMainLooper()) {
            comment.setValue(token);
        } else {
            comment.postValue(token);
        }
    }

    public LiveData<String> getComment() {
        Log.d(TAG, "getFirebaseUpdatedToken() called");
        return comment;
    }
}
