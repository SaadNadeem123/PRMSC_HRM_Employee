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
import java.util.List;

public class LeaveManagementFragment extends Fragment {
	
	private LeaveManagementRecyclerAdapter adapter;
	private LeaveManagementViewModel leaveManagementViewModel;
	private FragmentLeaveManagementBinding binding;
	
	public View onCreateView(@NonNull LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState) {
		binding = FragmentLeaveManagementBinding.inflate(inflater , container , false);
		View view = binding.getRoot();
		leaveManagementViewModel = new ViewModelProvider(this).get(LeaveManagementViewModel.class);
		leaveManagementViewModel.getLeaveManagementList().observe(getViewLifecycleOwner() , leaveManagementList -> {
			loadLeaveManagementData(leaveManagementList);
		});
		
		return view;
	}
	private void loadLeaveManagementData(List<LeaveManagementModel> leaveManagementList) {
		if(leaveManagementList==null)
		{
			leaveManagementList=new ArrayList<>();
		}
		adapter = new LeaveManagementRecyclerAdapter(leaveManagementList , getActivity());
		binding.recyclerView.setAdapter(adapter);
		binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		adapter.setListener(new LeaveManagementRecyclerAdapter.OnItemClickListener() {
			@Override
			public void OnItemClickListener(LeaveManagementModel leaveManagementModel) {
//				if(leaveManagementModel.getStatus().toLowerCase().equals(AppWideWariables.PENDING)|| leaveManagementModel.getStatus().toLowerCase().equals(AppWideWariables.REJECTED)) {
					Bundle bundle = new Bundle();
					bundle.putSerializable(AppWideWariables.LEAVE_MANAGEMENT_OBJECT_KEY , leaveManagementModel);
					AppUtils.switchActivity(getContext() , LeaveManagementDetailActivity.class , bundle);
//				}
			}
		});
		binding.progress.setVisibility(View.GONE);
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
				binding.progress.setVisibility(View.VISIBLE);
				leaveManagementViewModel.fetchLeaveManagementData(AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity())));
			}
		} , 100);
	}
	
}