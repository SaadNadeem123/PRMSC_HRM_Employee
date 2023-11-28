package com.lmkr.prmscemployee.ui.myinfo.emergencyUi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lmkr.prmscemployee.App;
import com.lmkr.prmscemployee.data.webservice.models.EmergencyContact;
import com.lmkr.prmscemployee.databinding.FragmentEmergencyBinding;
import com.lmkr.prmscemployee.ui.adapter.EmergencyContactAdapter;
import com.lmkr.prmscemployee.ui.myinfo.addContact.AddContactActivity;
import com.lmkr.prmscemployee.ui.utilities.AppUtils;
import com.lmkr.prmscemployee.ui.utilities.SharedPreferenceHelper;
import com.lmkr.prmscemployee.viewModel.EmergencyContactViewModel;
import com.lmkr.prmscemployee.viewModel.EmergencyContactViewModelFactory;

import java.util.List;

public class EmergencyFragment extends Fragment {

    private FragmentEmergencyBinding binding;
    private EmergencyContactAdapter adapter;
    private EmergencyContactViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmergencyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new EmergencyContactViewModelFactory(App.getInstance(),"")).get(EmergencyContactViewModel.class);
        viewModel.getEmergencyContactsLiveData().observe(getViewLifecycleOwner(),new Observer<List<EmergencyContact>>()
        {
            @Override
            public void onChanged(List<EmergencyContact> emergencyContacts) {
                adapter = new EmergencyContactAdapter(emergencyContacts, getActivity());
                binding.recyclerView.setAdapter(adapter);
            }
        });


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        callApi();
        binding.addContact.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddContactActivity.class));
        });

    }



    public void callApi() {

        String token = AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity()));
        int employeeId = SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getId();

        viewModel.getEmergencyContacts(token,employeeId);
        /*.observe(getViewLifecycleOwner(), new Observer<List<EmergencyContact>>() {
            @Override
            public void onChanged(List<EmergencyContact> emergencyContacts) {
                Log.d("EmergencyContactFragment", "Number of items in the list: " + emergencyContacts.size());
                adapter = new EmergencyContactAdapter(emergencyContacts, getActivity());
                binding.recyclerView.setAdapter(adapter);
            }
        });*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
