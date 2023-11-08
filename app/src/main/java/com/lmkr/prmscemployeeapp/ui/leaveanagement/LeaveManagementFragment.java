package com.lmkr.prmscemployeeapp.ui.leaveanagement;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lmkr.prmscemployeeapp.data.webservice.api.ApiManager;
import com.lmkr.prmscemployeeapp.data.webservice.models.LeaveManagementModel;
import com.lmkr.prmscemployeeapp.databinding.FragmentLeaveManagementBinding;
import com.lmkr.prmscemployeeapp.ui.activities.LeaveManagementDetailActivity;
import com.lmkr.prmscemployeeapp.ui.adapter.LeaveManagementRecyclerAdapter;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.AppWideWariables;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;
import com.lmkr.prmscemployeeapp.viewModel.LeaveManagementViewModel;

import java.util.ArrayList;

public class LeaveManagementFragment extends Fragment {
	
	private LeaveManagementRecyclerAdapter adapter;
	private LeaveManagementViewModel leaveManagementViewModel;
	private FragmentLeaveManagementBinding binding;
	
	public View onCreateView(@NonNull LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState) {
		binding = FragmentLeaveManagementBinding.inflate(inflater , container , false);
		View view = binding.getRoot();
		
		RecyclerView recyclerView = binding.recyclerView;
		
		adapter = new LeaveManagementRecyclerAdapter(new ArrayList<>() , getActivity());
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		adapter.setListener(new LeaveManagementRecyclerAdapter.OnItemClickListener() {
			@Override
			public void OnItemClickListener(LeaveManagementModel leaveManagementModel) {
				Bundle bundle = new Bundle();
				bundle.putSerializable(AppWideWariables.LEAVE_MANAGEMENT_OBJECT_KEY , leaveManagementModel);
				AppUtils.switchActivity(getContext() , LeaveManagementDetailActivity.class , bundle);
			}
		});
		
		leaveManagementViewModel = new ViewModelProvider(this).get(LeaveManagementViewModel.class);
		leaveManagementViewModel.getLeaveManagementList().observe(getViewLifecycleOwner() , leaveManagementList -> {
			adapter.setLeaveManagement(leaveManagementList);
		});
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refreshApiCalls();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
	
	public void refreshApiCalls() {
		ApiManager.getInstance().getToken();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				leaveManagementViewModel.fetchBulletinData(AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity())));
			}
		} , 1000);
	}
	
}