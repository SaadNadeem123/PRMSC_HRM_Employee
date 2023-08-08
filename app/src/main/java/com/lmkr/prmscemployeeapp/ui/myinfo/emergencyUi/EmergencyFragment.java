package com.lmkr.prmscemployeeapp.ui.myinfo.emergencyUi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.lmkr.prmscemployeeapp.databinding.FragmentEmergencyBinding;
import com.lmkr.prmscemployeeapp.ui.myinfo.addContact.AddContactActivity;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;

import java.util.List;

public class EmergencyFragment extends Fragment {

    private FragmentEmergencyBinding binding;
    private EmergencyContactAdapter adapter;
    private EmergencyContactViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmergencyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        viewModel = new ViewModelProvider(this).get(EmergencyContactViewModel.class);


        callApi();
        binding.addContact.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddContactActivity.class));
        });

    }

    public void callApi() {

        String token = AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity()));
        int employeeId = SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getId();

        viewModel.getEmergencyContacts(token, employeeId).observe(getViewLifecycleOwner(), new Observer<List<EmergencyContact>>() {
            @Override
            public void onChanged(List<EmergencyContact> emergencyContacts) {
                Log.d("EmergencyContactFragment", "Number of items in the list: " + emergencyContacts.size());
                adapter = new EmergencyContactAdapter(emergencyContacts, getActivity());
                binding.recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
