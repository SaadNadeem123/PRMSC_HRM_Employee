package com.lmkr.prmscemployeeapp.ui.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.data.webservice.api.ApiCalls;
import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
import com.lmkr.prmscemployeeapp.data.webservice.models.ApiBaseResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.LeaveManagementModel;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;
import com.lmkr.prmscemployeeapp.databinding.ActivityLeaveManagementDetailBinding;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.AppWideWariables;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaveManagementDetailActivity extends BaseFullScreenActivity {
	
	private ActivityLeaveManagementDetailBinding binding;
	private LeaveManagementModel leaveManagementModel = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityLeaveManagementDetailBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		handleIntent();
		initializeViews();
		setListeners();
	}
	
	@Override
	public void initializeViews() {
		
		if (leaveManagementModel == null) {
			return;
		}
		binding.year.setText(AppUtils.getConvertedDateFromOneFormatToOther(leaveManagementModel.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_MONTH_YEAR));
		binding.year.setVisibility(View.VISIBLE);
		
		String totaldays = leaveManagementModel.getTotal_days() + " " + (leaveManagementModel.getTotal_days() > 1 ? getString(R.string.days) : getString(R.string.day));
		binding.date.setText(AppUtils.getConvertedDateFromOneFormatToOther(leaveManagementModel.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_DAY) + " - " + AppUtils.getConvertedDateFromOneFormatToOther(leaveManagementModel.getTo_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_DAY) + " (" + totaldays + ")");
		binding.leaveType.setText(leaveManagementModel.getLeave_type_name());
		binding.comments.setText(leaveManagementModel.getReason());
		binding.name.setText(leaveManagementModel.getEmployee_name());
		
		switch (leaveManagementModel.getLeave_type_name()) {
			case AppWideWariables.LEAVE_TYPE_CASUAL:
				binding.image.setImageResource(R.drawable.casual_leaves);
				break;
			case AppWideWariables.LEAVE_TYPE_SICK:
				binding.image.setImageResource(R.drawable.sick_leaves);
				break;
			case AppWideWariables.LEAVE_TYPE_ANNUAL:
				binding.image.setImageResource(R.drawable.annual_leaves);
				break;
			default:
				binding.image.setImageResource(R.drawable.casual_leaves);
				break;
		}
		switch (leaveManagementModel.getStatus().toLowerCase()) {
			case AppWideWariables.PENDING:
				binding.status.setBackgroundColor(getColor(R.color.grey_dark));
				binding.statusLabel.setTextColor(getColor(R.color.grey_dark));
				break;
			case AppWideWariables.APPROVED:
				binding.status.setBackgroundColor(getColor(R.color.app_green));
				binding.statusLabel.setTextColor(getColor(R.color.app_green));
				break;
			case AppWideWariables.REJECTED:
				binding.status.setBackgroundColor(getColor(R.color.red));
				binding.statusLabel.setTextColor(getColor(R.color.red));
				break;
		}
		binding.statusLabel.setText(leaveManagementModel.getStatus());
	}
	
	@Override
	public void setListeners() {
		
		binding.reject.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				approveRejectLeaves(AppWideWariables.REJECT_LEAVE);
			}
		});
		binding.approve.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				approveRejectLeaves(AppWideWariables.APPROVE_LEAVE);
			}
		});
	}
	
	private void approveRejectLeaves(String status) {
		/*{
			"employee_id": "5",
				"approval_source": "1", //1 mobile //2 web
				"status": "2",  //3 for reject //1 for pending
				"approver_reason": "Accepted"
		}*/
		
		String reason = "";
		switch (status) {
			case AppWideWariables.PENDING_LEAVE -> {
				reason = "";
				break;
			}
			case AppWideWariables.APPROVE_LEAVE -> {
				reason = binding.approverComments.getText().toString().isEmpty() ? getString(R.string.approved) : binding.approverComments.getText().toString();
				break;
			}
			case AppWideWariables.REJECT_LEAVE -> {
				reason = binding.approverComments.getText().toString();
				break;
			}
			default -> {
				reason = "";
			}
		}
		
		if(TextUtils.isEmpty(reason))
		{
			AppUtils.makeNotification(getString(R.string.provide_comments),LeaveManagementDetailActivity.this);
			return;
		}
		
		if (!AppUtils.checkNetworkState(LeaveManagementDetailActivity.this)) {
			return;
		}
		
		
		UserData user = SharedPreferenceHelper.getLoggedinUser(LeaveManagementDetailActivity.this);
		
		ProgressDialog mProgressDialog = new ProgressDialog(LeaveManagementDetailActivity.this , R.style.CustomProgressDialog);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setMessage("Please Wait...");
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		
		JsonObject body = new JsonObject();
		body.addProperty("employee_id" , leaveManagementModel.getEmployee_id());
		body.addProperty("approval_source" , AppWideWariables.SOURCE_MOBILE_ENUM);
		body.addProperty("status" , status);
		body.addProperty("approver_reason" , binding.approverComments.getText().toString());
		
		Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
		
		Urls jsonPlaceHolderApi = retrofit.create(Urls.class);
		
		Call<ApiBaseResponse> call = jsonPlaceHolderApi.approveRejectLeave(AppUtils.getStandardHeaders(user), leaveManagementModel.getId(), body);
		
		call.enqueue(new Callback<ApiBaseResponse>() {
			@Override
			public void onResponse(Call<ApiBaseResponse> call , Response<ApiBaseResponse> response) {
				Log.i("response" , response.toString());
				
				if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_POST , response , LeaveManagementDetailActivity.this)) {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
							finish();
						}
					} , 2000);
					if (!response.isSuccessful()) {
						//tv.setText("Code :" + response.code());
						return;
					}
				}
				else {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
						}
					} , 2000);
				}
			}
			
			@Override
			public void onFailure(Call<ApiBaseResponse> call , Throwable t) {
				t.printStackTrace();
				if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
				Log.i("response" , t.toString());
				AppUtils.ApiError(t , LeaveManagementDetailActivity.this);
			}
		});
	}
	
	@Override
	public void handleIntent() {
		leaveManagementModel = (LeaveManagementModel) getIntent().getExtras().getSerializable(AppWideWariables.LEAVE_MANAGEMENT_OBJECT_KEY);
	}
	
	@Override
	public void callApi() {
	
	}
	
	@Override
	public void internetConnectionChangeListener(boolean isConnected) {
	
	}
	
}