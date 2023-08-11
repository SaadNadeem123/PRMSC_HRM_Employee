package com.lmkr.prmscemployeeapp.ui.myinfo.jobUi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lmkr.prmscemployeeapp.databinding.FragmentJobBinding;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;

public class JobFragment extends Fragment {

    private FragmentJobBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentJobBinding.inflate(inflater, container, false);
        bindViews();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void bindViews(){

        String date = AppUtils.formatDate(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getHire_date());

        binding.hireDate.setText(date);
        binding.employmentStatusDate1.setText(date);
        binding.employmentStatusDate2.setText(date);
        binding.jobTitle.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getDesignation());
        binding.reportsTo.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getManager_name());
        binding.department.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getDepartment_name());
        binding.employeeStatus.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getEmployment_status());
        binding.wing.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getWing_name());
    }
}
