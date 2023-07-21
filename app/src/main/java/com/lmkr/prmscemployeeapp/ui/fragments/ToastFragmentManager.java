package com.lmkr.prmscemployeeapp.ui.fragments;

import android.app.Activity;

import androidx.fragment.app.DialogFragment;

import com.lmkr.prmscemployeeapp.R;

public class ToastFragmentManager {
    private static ToastFragmentManager instance = null;
    private Activity activity;
    private ToastFragment fragment;

    private ToastFragmentManager() {
    }

    public static ToastFragmentManager getInstance(Activity activity) {
        if (instance == null) {
            instance = new ToastFragmentManager();
        }
        instance.setActivity(activity);
        return instance;

    }

    private void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void showToast(String notification) {
        if (fragment != null && fragment.getActivity() != null && fragment.isAdded()) {
            fragment.dismissAllowingStateLoss();
        }

        fragment = new ToastFragment();
        fragment.setTOAST_LENGTH(ToastFragment.TOAST_DEFAULT_LENGTH);
        fragment.setToastMessage(notification);
        fragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Toast_NoTitle);
        if (activity != null && !activity.isFinishing()) {
            try {
                fragment.show(activity.getFragmentManager(), "toast");
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public void showToast(String notification, long time) {
        if (fragment != null && fragment.getActivity() != null && fragment.isAdded()) {
            fragment.dismissAllowingStateLoss();
        }
        fragment = new ToastFragment();
        fragment.setTOAST_LENGTH(time);
        fragment.setToastMessage(notification);
        fragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Toast_NoTitle);
        if (activity != null && !activity.isFinishing()) {
            try {
                fragment.show(activity.getFragmentManager(), "toast");
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public void hideToast() {
        if (fragment != null && fragment.getActivity() != null && fragment.isAdded()) {
            fragment.dismissAllowingStateLoss();
        }
    }

}
