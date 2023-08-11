package com.lmkr.prmscemployeeapp.ui.myinfo.personalUi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lmkr.prmscemployeeapp.databinding.FragmentPersonalBinding;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;

import java.util.Objects;

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

    private void bindViews(){

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

        binding.copyButton.setOnClickListener(v ->{
            if(!binding.mobilePhone.getText().toString().isEmpty())
                AppUtils.copyTextToClipboard(binding.mobilePhone.getText().toString(), requireContext());
        });
        binding.messageButton.setOnClickListener(v ->{
            if(!binding.mobilePhone.getText().toString().isEmpty())
                AppUtils.sendSMS(binding.mobilePhone.getText().toString(), requireContext());
        });

        binding.phoneButton.setOnClickListener(v ->{

            if(!binding.mobilePhone.getText().toString().isEmpty())
                AppUtils.callPhoneNumber(binding.mobilePhone.getText().toString(), requireContext());
        });
    }
}
