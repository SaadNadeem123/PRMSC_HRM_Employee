package com.lmkr.prmscemployee.ui.myinfo.personalUi;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.firebase.messaging.FirebaseMessaging;
import com.lmkr.prmscemployee.R;
import com.lmkr.prmscemployee.data.database.AppDatabase;
import com.lmkr.prmscemployee.data.webservice.api.ApiCalls;
import com.lmkr.prmscemployee.databinding.FragmentPersonalBinding;
import com.lmkr.prmscemployee.ui.activities.SplashActivity;
import com.lmkr.prmscemployee.ui.fragments.ChangePasswordFragment;
import com.lmkr.prmscemployee.ui.utilities.AppUtils;
import com.lmkr.prmscemployee.ui.utilities.SharedPreferenceHelper;

public class personalFragment extends Fragment {

    private FragmentPersonalBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPersonalBinding.inflate(inflater, container, false);
        bindViews();
        listeners();
        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void bindViews() {

        binding.name.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getName());
        binding.designation.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getDesignation());
        binding.department.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getDepartment_name());
        binding.landline.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getLandline());
        binding.mobilePhone.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getMobile());
        binding.workEmail.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getEmail());
        binding.personalEmail.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getEmail());
        binding.employeeId.setText(String.valueOf(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getId()));
        binding.firstName.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getName());
    }

    private void listeners() {

        binding.logout.setOnClickListener(v -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AppDatabase.getInstance(getActivity()).clearAllTables();
                }
            }).start();
            if(ApiCalls.BASE_URL.equals(ApiCalls.BASE_URL_LIVE)) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("PRMSC");
                FirebaseMessaging.getInstance().unsubscribeFromTopic("PRMSC-DEV");
            }
            else if(ApiCalls.BASE_URL.equals(ApiCalls.BASE_URL_DEV)) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("PRMSC-DEV");
                FirebaseMessaging.getInstance().unsubscribeFromTopic("PRMSC");
            }
            SharedPreferenceHelper.clearPrefrences(getActivity());
            SharedPreferenceHelper.saveBoolean(SharedPreferenceHelper.IS_LOGGED_IN, false, getActivity());
            AppUtils.switchActivity(getActivity(), SplashActivity.class, null);
            finishAffinity(getActivity());
        });
        
        binding.changePassword.setOnClickListener(v -> {
            ChangePasswordFragment changePasswordFragment = ChangePasswordFragment.getInstance();
            changePasswordFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Dialog_NoTitle);
            changePasswordFragment.show(getActivity().getSupportFragmentManager(), "ChangePasswordFragment");
        });

        binding.copyButton.setOnClickListener(v -> {
            if (!binding.mobilePhone.getText().toString().isEmpty())
                AppUtils.copyTextToClipboard(binding.mobilePhone.getText().toString(), requireContext());
        });
        binding.messageButton.setOnClickListener(v -> {
            if (!binding.mobilePhone.getText().toString().isEmpty())
                AppUtils.sendSMS(binding.mobilePhone.getText().toString(), requireContext());
        });

        binding.phoneButton.setOnClickListener(v -> {

            if (!binding.mobilePhone.getText().toString().isEmpty())
                AppUtils.callPhoneNumber(binding.mobilePhone.getText().toString(), requireContext());
        });
    }
}
