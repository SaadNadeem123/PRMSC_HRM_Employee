package com.lmkr.prmscemployeeapp.ui.fragments;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.data.database.AppDatabase;
import com.lmkr.prmscemployeeapp.data.webservice.api.ApiCalls;
import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
import com.lmkr.prmscemployeeapp.data.webservice.models.ApiBaseResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;
import com.lmkr.prmscemployeeapp.databinding.FragmentChangePasswordBinding;
import com.lmkr.prmscemployeeapp.databinding.FragmentLateCommentBinding;
import com.lmkr.prmscemployeeapp.ui.activities.SplashActivity;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.AppWideWariables;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;
import com.lmkr.prmscemployeeapp.viewModel.LateCommentAttendanceViewModelListener;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
