package com.lmkr.prmscemployee.ui.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.lmkr.prmscemployee.R;
import com.lmkr.prmscemployee.data.database.models.LeaveRequest;
import com.lmkr.prmscemployee.data.webservice.api.ApiCalls;
import com.lmkr.prmscemployee.data.webservice.api.Urls;
import com.lmkr.prmscemployee.data.webservice.models.ApiBaseResponse;
import com.lmkr.prmscemployee.data.webservice.models.LeaveManagementModel;
import com.lmkr.prmscemployee.data.webservice.models.UserData;
import com.lmkr.prmscemployee.databinding.ActivityLeaveManagementDetailBinding;
import com.lmkr.prmscemployee.ui.utilities.AppUtils;
import com.lmkr.prmscemployee.ui.utilities.AppWideWariables;
import com.lmkr.prmscemployee.ui.utilities.SharedPreferenceHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaveManagementDetailActivity extends BaseFullScreenActivity {
	
	private ActivityLeaveManagementDetailBinding binding;
	private LeaveManagementModel leaveManagementModel = null;
	private LeaveRequest leaveRequest = null;
	
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
		
		if (leaveManagementModel != null) {
			binding.title.setText(getResources().getString(R.string.title_leave_management));
			if (leaveManagementModel.getStatus().toLowerCase().equals(AppWideWariables.PENDING)) {
				binding.bottom.setVisibility(View.VISIBLE);
				binding.rejectedReasonLabel.setVisibility(View.GONE);
				binding.rejectedReason.setVisibility(View.GONE);
				binding.rejectedReason.setText("");
			} else {
				binding.bottom.setVisibility(View.GONE);
				binding.rejectedReasonLabel.setVisibility(View.VISIBLE);
				binding.rejectedReason.setVisibility(View.VISIBLE);
				binding.rejectedReason.setText(leaveManagementModel.getApprover_reason());
			}
			
			binding.year.setText(AppUtils.getConvertedDateFromOneFormatToOther(leaveManagementModel.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_MONTH_YEAR));
			binding.year.setVisibility(View.VISIBLE);
			
			String days = leaveManagementModel.getTotal_days()>=1?((int)leaveManagementModel.getTotal_days())+"":leaveManagementModel.getTotal_days()+"";
			String totaldays = days + " " + (leaveManagementModel.getTotal_days() > 1 ? getString(R.string.days) : getString(R.string.day));
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
			
			switch (leaveManagementModel.getStatus()) {
				case AppWideWariables.PENDING:
					binding.status.setBackgroundColor(getColor(R.color.grey_dark));
					binding.statusLabel.setTextColor(getColor(R.color.grey_dark));
					binding.statusLabel.setText(getString(R.string.pending));
					break;
				case AppWideWariables.APPROVED:
					binding.status.setBackgroundColor(getColor(R.color.app_green));
					binding.statusLabel.setTextColor(getColor(R.color.app_green));
					binding.statusLabel.setText(getString(R.string.approved));
					break;
				case AppWideWariables.REJECTED:
					binding.status.setBackgroundColor(getColor(R.color.red));
					binding.statusLabel.setTextColor(getColor(R.color.red));
					binding.statusLabel.setText(getString(R.string.rejected));
					break;
			}
		}
		else if (leaveRequest != null) {
			
			binding.title.setText(getResources().getString(R.string.title_leave_request));
			if (leaveRequest.getStatus().toLowerCase().equals(AppWideWariables.PENDING)) {
				binding.bottom.setVisibility(View.GONE);
				binding.rejectedReasonLabel.setVisibility(View.GONE);
				binding.rejectedReason.setVisibility(View.GONE);
				binding.rejectedReason.setText("");
			} else {
				binding.bottom.setVisibility(View.GONE);
				binding.rejectedReasonLabel.setVisibility(View.VISIBLE);
				binding.rejectedReason.setVisibility(View.VISIBLE);
				binding.rejectedReason.setText(leaveRequest.getApprover_reason());
				if(TextUtils.isEmpty(leaveRequest.getApprover_reason()))
				{
					binding.rejectedReasonLabel.setVisibility(View.GONE);
				}
			}
			
			binding.year.setText(AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_MONTH_YEAR));
			binding.year.setVisibility(View.VISIBLE);
			
			String days = leaveRequest.getTotal_days()>=1?((int)leaveRequest.getTotal_days())+"":leaveRequest.getTotal_days()+"";
			String totaldays = days + " " + (leaveRequest.getTotal_days() > 1 ? getString(R.string.days) : getString(R.string.day));
			binding.date.setText(AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_DAY) + " - " + AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getTo_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_DAY) + " (" + totaldays + ")");
			binding.leaveType.setText(leaveRequest.getLeave_type_name());
			binding.comments.setText(leaveRequest.getReason());
			binding.name.setText(leaveRequest.getEmployee_name());
			
			switch (leaveRequest.getLeave_type_name()) {
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
			
			switch (leaveRequest.getStatus()) {
				case AppWideWariables.PENDING:
					binding.status.setBackgroundColor(getColor(R.color.grey_dark));
					binding.statusLabel.setTextColor(getColor(R.color.grey_dark));
					binding.statusLabel.setText(getString(R.string.pending));
					break;
				case AppWideWariables.APPROVED:
					binding.status.setBackgroundColor(getColor(R.color.app_green));
					binding.statusLabel.setTextColor(getColor(R.color.app_green));
					binding.statusLabel.setText(getString(R.string.approved));
					break;
				case AppWideWariables.REJECTED:
					binding.status.setBackgroundColor(getColor(R.color.red));
					binding.statusLabel.setTextColor(getColor(R.color.red));
					binding.statusLabel.setText(getString(R.string.rejected));
					break;
			}
		}
		
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
		
		if (TextUtils.isEmpty(reason)) {
			AppUtils.makeNotification(getString(R.string.provide_comments) , LeaveManagementDetailActivity.this);
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
		
		Urls jsonPlacebindingApi = retrofit.create(Urls.class);
		
		Call<ApiBaseResponse> call = jsonPlacebindingApi.approveRejectLeave(AppUtils.getStandardHeaders(user) , leaveManagementModel.getId() , body);
		
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
				} else {
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
		leaveRequest = (LeaveRequest) getIntent().getExtras().getSerializable(AppWideWariables.LEAVE_REQUEST_OBJECT_KEY);
	}
	
	@Override
	public void callApi() {
	
	}
	
	@Override
	public void internetConnectionChangeListener(boolean isConnected) {
	
	}
	
}