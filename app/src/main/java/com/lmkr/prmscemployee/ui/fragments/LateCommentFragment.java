package com.lmkr.prmscemployee.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lmkr.prmscemployee.R;
import com.lmkr.prmscemployee.databinding.FragmentChangePasswordBinding;
import com.lmkr.prmscemployee.databinding.FragmentLateCommentBinding;
import com.lmkr.prmscemployee.viewModel.LateCommentAttendanceViewModelListener;

import org.jetbrains.annotations.NotNull;

public class LateCommentFragment extends BaseDialogFragment {
	
	private static LateCommentFragment fragment = null;
	private FragmentLateCommentBinding binding;
	
	public static LateCommentFragment getInstance() {
		if (fragment == null) {
			fragment = new LateCommentFragment();
		}
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container ,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		binding = FragmentLateCommentBinding.inflate(getLayoutInflater() , container , false);
		return binding.getRoot();
	}
	
	@Override
	public void onViewCreated(@NonNull @NotNull View view , @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
		super.onViewCreated(view , savedInstanceState);
		setListeners();
	}
	
	
	@Override
	public void initializeViews(View view) {
	
	}
	
	@Override
	public void setListeners() {
		binding.close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				destroy();
			}
		});
		
		binding.btnYes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				validate();
			}
		});
		
		binding.reason.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence , int i , int i1 , int i2) {
			
			}
			
			@Override
			public void onTextChanged(CharSequence charSequence , int i , int i1 , int i2) {
				binding.btnYes.setError(null);
			}
			
			@Override
			public void afterTextChanged(Editable editable) {
			
			}
		});
	}
	
	private void validate() {
		boolean shouldProceed = true;
		
		
		if (binding.reason.getText().toString().isEmpty()|| binding.reason.getText().toString().length() < 10|| binding.reason.getText().toString().length() > 250) {
			binding.reason.setError(getString(R.string.comments_length));
			shouldProceed = false;
		}
		
		if(shouldProceed)
		{
			LateCommentAttendanceViewModelListener.getInstance().updateComment(binding.reason.getText().toString());
			dismissAllowingStateLoss();
		}
		
	}
	
	private void destroy() {
		fragment = null;
		dismissAllowingStateLoss();
	}
	
}
