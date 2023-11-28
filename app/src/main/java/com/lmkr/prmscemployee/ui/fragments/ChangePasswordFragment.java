package com.lmkr.prmscemployee.ui.fragments;

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
import com.lmkr.prmscemployee.R;
import com.lmkr.prmscemployee.data.database.AppDatabase;
import com.lmkr.prmscemployee.data.webservice.api.ApiCalls;
import com.lmkr.prmscemployee.data.webservice.api.Urls;
import com.lmkr.prmscemployee.data.webservice.models.ApiBaseResponse;
import com.lmkr.prmscemployee.data.webservice.models.UserData;
import com.lmkr.prmscemployee.databinding.FragmentChangePasswordBinding;
import com.lmkr.prmscemployee.ui.activities.SplashActivity;
import com.lmkr.prmscemployee.ui.utilities.AppUtils;
import com.lmkr.prmscemployee.ui.utilities.AppWideWariables;
import com.lmkr.prmscemployee.ui.utilities.SharedPreferenceHelper;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePasswordFragment extends BaseDialogFragment {
	
	private static ChangePasswordFragment fragment = null;
	private FragmentChangePasswordBinding binding;
	
	public static ChangePasswordFragment getInstance() {
		if (fragment == null) {
			fragment = new ChangePasswordFragment();
		}
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container ,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		binding = FragmentChangePasswordBinding.inflate(getLayoutInflater() , container , false);
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
		binding.confirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					validate();
				}
				return false;
			}
		});
		
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
		
		binding.currentPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence , int i , int i1 , int i2) {
			
			}
			
			@Override
			public void onTextChanged(CharSequence charSequence , int i , int i1 , int i2) {
				binding.currentPassword.setError(null);
			}
			
			@Override
			public void afterTextChanged(Editable editable) {
			
			}
		});
		binding.newPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence , int i , int i1 , int i2) {
			
			}
			
			@Override
			public void onTextChanged(CharSequence charSequence , int i , int i1 , int i2) {
				binding.newPassword.setError(null);
			}
			
			@Override
			public void afterTextChanged(Editable editable) {
			
			}
		});
		binding.confirmPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence , int i , int i1 , int i2) {
			
			}
			
			@Override
			public void onTextChanged(CharSequence charSequence , int i , int i1 , int i2) {
				binding.confirmPassword.setError(null);
			}
			
			@Override
			public void afterTextChanged(Editable editable) {
			
			}
		});
	}
	
	private void validate() {
		boolean shouldProceed = true;
		
		if (!binding.currentPassword.getText().toString().equalsIgnoreCase(SharedPreferenceHelper.getString(SharedPreferenceHelper.PASSWORD , getActivity()))) {
			binding.currentPassword.setError(getString(R.string.wrong_current_password));
			shouldProceed = false;
		}
		if (binding.newPassword.getText().toString().isEmpty()
				|| binding.newPassword.getText().toString().length() < 8
				|| !AppUtils.isValidPassword(binding.newPassword.getText().toString())) {
			binding.newPassword.setError(getString(R.string.password_format_error));
			shouldProceed = false;
		}
		if (binding.confirmPassword.getText().toString().isEmpty()
				|| binding.confirmPassword.getText().toString().length() < 8
				|| !AppUtils.isValidPassword(binding.confirmPassword.getText().toString())) {
			binding.confirmPassword.setError(getString(R.string.password_format_error));
			shouldProceed = false;
		}else if (!binding.confirmPassword.getText().toString().equals(binding.newPassword.getText().toString())) {
			binding.confirmPassword.setError(getString(R.string.password_match_error));
			shouldProceed = false;
		}
		
		if(shouldProceed)
		{
			callApi();
		}
		
	}
	
	private void callApi() {
		UserData user = SharedPreferenceHelper.getLoggedinUser(getActivity());
		
		ProgressDialog mProgressDialog = new ProgressDialog(getActivity(), R.style.CustomProgressDialog);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setMessage("Please Wait...");
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		
		JsonObject body = new JsonObject();
		body.addProperty("oldPassword", binding.currentPassword.getText().toString().trim());
		body.addProperty("newPassword", binding.newPassword.getText().toString().trim());
		body.addProperty("source", AppWideWariables.SOURCE_MOBILE);
		
		Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
		
		Urls jsonPlaceHolderApi = retrofit.create(Urls.class);
		
		Call<ApiBaseResponse> call = jsonPlaceHolderApi.changePassword(AppUtils.getStandardHeaders(user), body);
		
		call.enqueue(new Callback<ApiBaseResponse>() {
			@Override
			public void onResponse(Call<ApiBaseResponse> call, Response<ApiBaseResponse> response) {
				Log.i("response", response.toString());
				
				if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_POST, response, getActivity())) {
					
					if (!response.isSuccessful()) {
						//tv.setText("Code :" + response.code());
						return;
					}
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							
							if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
							logout();
						}
					},2000);
				}
			}
			@Override
			public void onFailure(Call<ApiBaseResponse> call, Throwable t) {
				t.printStackTrace();
				if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
				Log.i("response", t.toString());
				AppUtils.ApiError(t, getActivity());
			}
		});
	}
	
	private void logout() {
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
		SharedPreferenceHelper.saveBoolean(SharedPreferenceHelper.IS_LOGGED_IN , false , getActivity());
		AppUtils.switchActivity(getActivity() , SplashActivity.class , null);
		finishAffinity(getActivity());
		destroy();
	}
	
	private void destroy() {
		fragment = null;
		dismissAllowingStateLoss();
	}
	
}
