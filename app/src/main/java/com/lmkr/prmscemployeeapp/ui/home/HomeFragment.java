package com.lmkr.prmscemployeeapp.ui.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.JsonObject;
import com.lmkr.prmscemployeeapp.App;
import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.data.database.models.AttendanceHistory;
import com.lmkr.prmscemployeeapp.data.webservice.api.ApiCalls;
import com.lmkr.prmscemployeeapp.data.webservice.api.ApiManager;
import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
import com.lmkr.prmscemployeeapp.data.webservice.models.ApiBaseResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.AttendanceHistoryResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;
import com.lmkr.prmscemployeeapp.databinding.FragmentHomeBinding;
import com.lmkr.prmscemployeeapp.ui.activities.CameraXActivity;
import com.lmkr.prmscemployeeapp.ui.adapter.AttendanceHistoryRecyclerAdapter;
import com.lmkr.prmscemployeeapp.ui.adapter.LeavesProgressRecyclerAdapter;
import com.lmkr.prmscemployeeapp.ui.fragments.FullScreenMapFragment;
import com.lmkr.prmscemployeeapp.ui.fragments.LateCommentFragment;
import com.lmkr.prmscemployeeapp.ui.utilities.AlertDialogUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.AppWideWariables;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;
import com.lmkr.prmscemployeeapp.viewModel.AttendanceHistoryViewModel;
import com.lmkr.prmscemployeeapp.viewModel.AttendanceHistoryViewModelFactory;
import com.lmkr.prmscemployeeapp.viewModel.LateCommentAttendanceViewModelListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
	
	private static List<AttendanceHistory> attendanceHistories = new ArrayList<>();
	String lateComment = "";
	private FragmentHomeBinding binding;
	private AttendanceHistoryViewModel attendanceHistoryViewModel;
	private boolean oneTimeCheckinAnimationCompleted = false;
	private boolean oneTimeCheckoutAnimationCompleted = false;
	private ObjectAnimator objectAnimator = null;
	private final Observer<? super List<AttendanceHistory>> attendanceHistoryObserver = new Observer<List<AttendanceHistory>>() {
		@Override
		public void onChanged(List<AttendanceHistory> attendanceHistories) {
			HomeFragment.attendanceHistories = attendanceHistories;
			checkState(attendanceHistories);
//            loadAttendanceHistoryData();
		}
	};
	
	private void checkState(List<AttendanceHistory> attendanceHistories) {
		if (attendanceHistories == null) {
			return;
		}
		boolean isCheckedIn = false;
		boolean isCheckedOut = false;
		AttendanceHistory attendanceHistory = null;
		for (AttendanceHistory attendance : attendanceHistories) {
			if (attendance.getCheckin_time() != null && !TextUtils.isEmpty(attendance.getCheckin_time()) && AppUtils.getConvertedDateFromOneFormatToOther(attendance.getCheckin_time() , AppUtils.FORMAT19 , AppUtils.FORMAT3).equals(AppUtils.getCurrentDate())) {
				if (attendance.getCheckin_time() != null && !TextUtils.isEmpty(attendance.getCheckin_time())) {
					isCheckedIn = true;
				}
				if (attendance.getCheckout_time() != null && !TextUtils.isEmpty(attendance.getCheckout_time())) {
					isCheckedOut = true;
				}
				attendanceHistory = attendance;
				break;
			}
		}
		
		binding.contentHome.recyclerViewAttendanceHistory.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL , false));
		AttendanceHistoryRecyclerAdapter adapter = new AttendanceHistoryRecyclerAdapter(getActivity() , attendanceHistories);
		adapter.setListener(new AttendanceHistoryRecyclerAdapter.OnItemClickListener() {
			@Override
			public void OnItemClickListener(AttendanceHistory attendanceHistory) {
				if (attendanceHistory.getLateReason() != null && !TextUtils.isEmpty(attendanceHistory.getLateReason())) {
					AlertDialogUtils.showPopUpMessageDialog(getContext() , getString(R.string.late_reason) , attendanceHistory.getLateReason() , null);
				}
			}
		});

//		binding.contentHome.recyclerViewAttendanceHistory.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
		binding.contentHome.recyclerViewAttendanceHistory.setAdapter(adapter);
		
		updateCheckInCheckOutProgress(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getCheckout_check() , isCheckedIn , isCheckedOut , attendanceHistory);
	}
	
	private void updateCheckInCheckOutProgress(String checkoutCheck , boolean isCheckedIn , boolean isCheckedOut , AttendanceHistory attendanceHistory) {
		if (isCheckedIn && !isCheckedOut) {
			if (!oneTimeCheckinAnimationCompleted) {
				
				if (objectAnimator != null && objectAnimator.isRunning()) {
					return;
				}
				
				if (binding.animationTick != null && binding.animationTick.isAnimating()) {
					return;
				}
				
				binding.circleAnimation.setProgress(0);
				binding.circleAnimation.setMax(1000);
				binding.checkedIn.setText(getResources().getText(R.string.checkedin));
				binding.checkin.setText(getResources().getText(R.string.checkout));
				binding.time.setText(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getCheckin_time() , AppUtils.FORMAT19 , AppUtils.FORMAT5));
				binding.closeImg.setVisibility(View.GONE);
				binding.animationTick.cancelAnimation();
				binding.animationTick.setVisibility(View.VISIBLE);
				
				if (checkoutCheck.equals(AppWideWariables.NO)) {
					binding.checkin.setVisibility(View.GONE);
				} else {
					binding.checkin.setVisibility(View.VISIBLE);
				}
				
				objectAnimator = ObjectAnimator.ofInt(binding.circleAnimation , "progress" , 1000).setDuration(3000);
				objectAnimator.addListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(@NonNull Animator animation) {
					
					}
					
					@Override
					public void onAnimationEnd(@NonNull Animator animation) {
						if (binding == null || binding.animationTick == null) {
							return;
						}
						binding.animationTick.setAnimation(R.raw.tick);
						binding.animationTick.playAnimation();
						binding.animationTick.addAnimatorListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								super.onAnimationEnd(animation);
								oneTimeCheckinAnimationCompleted = true;
								objectAnimator = null;
							}
						});
					}
					
					@Override
					public void onAnimationCancel(@NonNull Animator animation) {
					
					}
					
					@Override
					public void onAnimationRepeat(@NonNull Animator animation) {
					
					}
				});
				objectAnimator.start();
			} else {
				binding.circleAnimation.setProgress(1000);
				binding.checkedIn.setText(getResources().getText(R.string.checkedin));
				binding.checkin.setText(getResources().getText(R.string.checkout));
				binding.time.setText(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getCheckin_time() , AppUtils.FORMAT19 , AppUtils.FORMAT5));
				binding.closeImg.setVisibility(View.GONE);
//                binding.animationTick.cancelAnimation();
//                binding.animationTick.setAnimation(R.raw.tick);
				binding.animationTick.setVisibility(View.VISIBLE);
				if (checkoutCheck.equals(AppWideWariables.NO)) {
					binding.checkin.setVisibility(View.GONE);
				} else {
					binding.checkin.setVisibility(View.VISIBLE);
				}
				
			}
		} else if (isCheckedIn && isCheckedOut && checkoutCheck.equals(AppWideWariables.YES)) {
			if (!oneTimeCheckoutAnimationCompleted) {
				if (objectAnimator != null && objectAnimator.isRunning()) {
					return;
				}
				
				if (binding.animationTick != null && binding.animationTick.isAnimating()) {
					return;
				}
				
				binding.circleAnimation.setProgress(0);
				binding.circleAnimation.setMax(1000);
				binding.checkedIn.setText(getResources().getText(R.string.checkedout));
				binding.checkin.setVisibility(View.VISIBLE);
				binding.checkin.setText(getResources().getText(R.string.checkin));
				binding.time.setText(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getCheckout_time() , AppUtils.FORMAT19 , AppUtils.FORMAT5));
				binding.closeImg.setVisibility(View.GONE);
				binding.animationTick.cancelAnimation();
				binding.animationTick.setVisibility(View.VISIBLE);
				
				objectAnimator = ObjectAnimator.ofInt(binding.circleAnimation , "progress" , 1000).setDuration(3000);
				objectAnimator.addListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(@NonNull Animator animation) {
					
					}
					
					@Override
					public void onAnimationEnd(@NonNull Animator animation) {
						if (binding == null || binding.animationTick == null) {
							return;
						}
						binding.animationTick.setAnimation(R.raw.tick);
						binding.animationTick.playAnimation();
						binding.animationTick.addAnimatorListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								super.onAnimationEnd(animation);
								oneTimeCheckoutAnimationCompleted = true;
							}
						});
					}
					
					@Override
					public void onAnimationCancel(@NonNull Animator animation) {
					
					}
					
					@Override
					public void onAnimationRepeat(@NonNull Animator animation) {
					
					}
				});
				objectAnimator.start();
			} else {
				binding.circleAnimation.setProgress(1000);
				binding.checkedIn.setText(getResources().getText(R.string.checkedout));
				binding.checkin.setVisibility(View.VISIBLE);
				binding.checkin.setText(getResources().getText(R.string.checkin));
				binding.time.setText(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getCheckout_time() , AppUtils.FORMAT19 , AppUtils.FORMAT5));
				binding.closeImg.setVisibility(View.GONE);
//                binding.animationTick.cancelAnimation();
//                binding.animationTick.setAnimation(R.raw.tick);
				binding.animationTick.setVisibility(View.VISIBLE);
			}
		} else {
			binding.checkedIn.setText(getResources().getText(R.string.checkedout));
			binding.checkin.setVisibility(View.VISIBLE);
			binding.checkin.setText(getResources().getText(R.string.checkin));
			binding.time.setText("");
			binding.circleAnimation.setProgress(0);
			binding.closeImg.setVisibility(View.VISIBLE);
			binding.animationTick.cancelAnimation();
			binding.animationTick.setVisibility(View.GONE);
		}
	}
	
	public View onCreateView(@NonNull LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState) {
		
		binding = FragmentHomeBinding.inflate(inflater , container , false);
		View root = binding.getRoot();

//        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
		return root;
	}
	
	@Override
	public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view , savedInstanceState);
		
		attendanceHistoryViewModel = new ViewModelProvider(this , new AttendanceHistoryViewModelFactory(App.getInstance() , "%" + AppUtils.getCurrentDate() + "%")).get(AttendanceHistoryViewModel.class);
		attendanceHistoryViewModel.getAttendanceHistory().observe(getViewLifecycleOwner() , attendanceHistoryObserver);
		
		
		binding.checkin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtils.hideNotification(getActivity());
				
				if (AppUtils.isDateTimeAuto(getActivity())) {
					try {
						boolean allowToProceed = true;
						if (SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getHave_ssid().equals("yes")) {
							allowToProceed = AppUtils.wifiLockValidate();
						}
						
						if (allowToProceed) {
							if (SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getGeofence().equals("yes")) {
								FullScreenMapFragment fragment = FullScreenMapFragment.getInstance();
								if (!fragment.isAdded()) {
									fragment.setStyle(DialogFragment.STYLE_NO_FRAME , R.style.Dialog_NoTitle);
									fragment.show(getActivity().getSupportFragmentManager() , "MapFragment");
								}
							} else if (SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getFacelock().equals("yes")) {
								getActivity().startActivity(new Intent(getActivity() , CameraXActivity.class));
							} else {
								SharedPreferenceHelper.saveString(AppWideWariables.ATTENDANCE_TIME , AppUtils.getAttendanceTime() , getActivity());
								allowToProceed();
							}
						} else {
							AppUtils.makeNotification(getResources().getString(R.string.connect_to_binded_wifi) , getActivity());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		UserData userData = SharedPreferenceHelper.getLoggedinUser(getActivity());
		
		binding.contentHome.recyclerviewLeaveProgress.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL , false));
		LeavesProgressRecyclerAdapter adapter = new LeavesProgressRecyclerAdapter(getActivity() , userData.getLeaveCount());
		binding.contentHome.recyclerviewLeaveProgress.setAdapter(adapter);
		
		
		// updateProgressBar() method sets
		// the progress of ProgressBar in text
		binding.textHome.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getName());
//        updateProgressBar();
//        getAttendanceHistory();
		refreshApiCalls();
		enableCheckinButton(true);
	}
	
	private void allowToProceed() {
		if (binding.checkin.getText().equals(getResources().getString(R.string.checkout))) {
			callCheckOutApi();
		} else if (binding.checkin.getText().equals(getResources().getString(R.string.checkin))) {
			if (AppUtils.isLate(getContext() , SharedPreferenceHelper.getString(AppWideWariables.ATTENDANCE_TIME , getActivity()))) {
				LateCommentAttendanceViewModelListener.getInstance().getComment().observe(this , lateCommentObserver);
				
				try {
					LateCommentFragment lateCommentFragment = LateCommentFragment.getInstance();
					if (!lateCommentFragment.isAdded()) {
						lateCommentFragment.setStyle(DialogFragment.STYLE_NO_FRAME , R.style.Dialog_NoTitle);
						lateCommentFragment.show(getActivity().getSupportFragmentManager() , "LateCommentFragment");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} else {
				if (SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getFacelock().equals("yes")) {
					callCheckInMultiPartApi();
				} else {
					callCheckInApi();
				}
			}
		} else {
		
		}
	}
	
	private void callCheckOutApi() {
		if (!AppUtils.checkNetworkState(getActivity())) {
			return;
		}
		
		Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
		Urls urls = retrofit.create(Urls.class);
		JsonObject body = new JsonObject();
		
		UserData userData = SharedPreferenceHelper.getLoggedinUser(getActivity());
		body.addProperty("checkout_time" , SharedPreferenceHelper.getString(AppWideWariables.ATTENDANCE_TIME , getActivity()));
		Call<ApiBaseResponse> call = urls.checkout(AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity())) , String.valueOf(userData.getBasicData().get(0).getId()) , body);
		
		call.enqueue(new Callback<ApiBaseResponse>() {
			@Override
			public void onResponse(Call<ApiBaseResponse> call , Response<ApiBaseResponse> response) {
				Log.i("response" , response.toString());
				if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_POST , response , getActivity())) {
					if (!response.isSuccessful()) {
						return;
					}
					SharedPreferenceHelper.resetGeofenceAndFaceLock(getActivity());
					oneTimeCheckoutAnimationCompleted = false;
					getAttendanceHistory();
				}
			}
			
			@Override
			public void onFailure(Call<ApiBaseResponse> call , Throwable t) {
				t.printStackTrace();
				AppUtils.ApiError(t , getActivity());
//                AppUtils.makeNotification(t.toString(), getActivity());
				Log.i("response" , t.toString());
			}
		});
	}	private final Observer<? super String> lateCommentObserver = new Observer<String>() {
		@Override
		public void onChanged(String comment) {
			lateComment = comment;
			LateCommentAttendanceViewModelListener.getInstance().getComment().removeObserver(lateCommentObserver);
			if (binding.checkin.getText().equals(getResources().getString(R.string.checkin))) {
				if (SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getFacelock().equals("yes")) {
					callCheckInMultiPartApi();
				} else {
					callCheckInApi();
				}
			}
		}
	};
	
	private void callCheckInApi() {
		
		if (!AppUtils.checkNetworkState(getActivity())) {
			return;
		}
		
		
		Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
		
		Urls urls = retrofit.create(Urls.class);
		
		JsonObject body = new JsonObject();
		
		UserData userData = SharedPreferenceHelper.getLoggedinUser(getActivity());
		
		String latitude = SharedPreferenceHelper.getString("lat" , getActivity());
		String longitude = SharedPreferenceHelper.getString("long" , getActivity());
		
		
		body.addProperty("employee_id" , userData.getBasicData().get(0).getId());
		body.addProperty("checkin_time" , SharedPreferenceHelper.getString(AppWideWariables.ATTENDANCE_TIME , getActivity()));
//		body.addProperty("checkin_time" , AppUtils.getCurrentDateTimeGMT5String());
		if (!TextUtils.isEmpty(lateComment)) {
			body.addProperty("comments" , lateComment);
		}
		body.addProperty("lat" , TextUtils.isEmpty(latitude) ? "0" : latitude);
		body.addProperty("longitude" , TextUtils.isEmpty(longitude) ? "0" : longitude);
		body.addProperty("source" , AppWideWariables.SOURCE_MOBILE);
		body.addProperty("file_name" , "");
		body.addProperty("file_path" , "");

//        File file;// = // initialize file here

//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
		
		
		Call<ApiBaseResponse> call = urls.checkIn(AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity())) , body);
		
		call.enqueue(new Callback<ApiBaseResponse>() {
			@Override
			public void onResponse(Call<ApiBaseResponse> call , Response<ApiBaseResponse> response) {
				Log.i("response" , response.toString());
				if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_POST , response , getActivity())) {
					if (!response.isSuccessful()) {
//                    tv.setText("Code :" + response.code());
						return;
					}
					
					SharedPreferenceHelper.resetGeofenceAndFaceLock(getActivity());
					oneTimeCheckinAnimationCompleted = false;
					oneTimeCheckoutAnimationCompleted = false;
					lateComment = "";
					getAttendanceHistory();
				}
			}
			
			@Override
			public void onFailure(Call<ApiBaseResponse> call , Throwable t) {
				t.printStackTrace();
				AppUtils.ApiError(t , getActivity());
//                AppUtils.makeNotification(t.toString(), getActivity());
				Log.i("response" , t.toString());
//                tv.setText(t.getMessage());
			}
		});
		
	}
	
	private void callCheckInMultiPartApi() {
		
		if (!AppUtils.checkNetworkState(getActivity())) {
			return;
		}
		
		
		UserData userData = SharedPreferenceHelper.getLoggedinUser(getActivity());
		
		ProgressDialog mProgressDialog = new ProgressDialog(getActivity() , R.style.CustomProgressDialog);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setMessage("Please Wait...");
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		
		String latitude = SharedPreferenceHelper.getString("lat" , getActivity());
		String longitude = SharedPreferenceHelper.getString("long" , getActivity());
		
		
		RequestBody fpath = null;
		MultipartBody.Part body = null;
//		AlertDialogUtils.showPopUpMessageDialog(getActivity(),SharedPreferenceHelper.getFaceLockPath(getActivity()));
		File file = new File(SharedPreferenceHelper.getFaceLockPath(getActivity()));
		
		fpath = RequestBody.create(MediaType.parse("image/jpeg") , file);
		body = MultipartBody.Part.createFormData("file" , file.getName() , fpath);
		
		UserData user = SharedPreferenceHelper.getLoggedinUser(getActivity());
		
		RequestBody employee_id = RequestBody.create(MediaType.parse("text/plain") , user.getBasicData().get(0).getId() + "");
		RequestBody checkin_time = RequestBody.create(MediaType.parse("text/plain") , SharedPreferenceHelper.getString(AppWideWariables.ATTENDANCE_TIME , getActivity()) + "");
		RequestBody comments = RequestBody.create(MediaType.parse("text/plain") , lateComment);
		RequestBody lat = RequestBody.create(MediaType.parse("text/plain") , TextUtils.isEmpty(latitude) ? "0" : latitude + "");
		RequestBody lng = RequestBody.create(MediaType.parse("text/plain") , TextUtils.isEmpty(longitude) ? "0" : longitude + "");
		RequestBody source = RequestBody.create(MediaType.parse("text/plain") , AppWideWariables.SOURCE_MOBILE);
		
		OkHttpClient httpClient = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(10 , TimeUnit.MINUTES).readTimeout(10 , TimeUnit.MINUTES).writeTimeout(10 , TimeUnit.MINUTES)
//                .addInterceptor(new NetInterceptor())
/*                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        String token = user.getToken();
                        request = request.newBuilder()
                                .addHeader("Authorization", token)
                                .build();
                        return chain.proceed(request);
                    }
                })*/.build();
		
		
		Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient).build();
		
		Urls urls = retrofit.create(Urls.class);
		
		
		Call<ApiBaseResponse> call = urls.checkInMultipart(AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity())) , body , employee_id , checkin_time , comments , lat , lng , source);
		
		call.enqueue(new Callback<ApiBaseResponse>() {
			@Override
			public void onResponse(Call<ApiBaseResponse> call , Response<ApiBaseResponse> response) {
				Log.i("response" , response.toString());
				if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_POST , response , getActivity())) {
					if (!response.isSuccessful()) {
//                    tv.setText("Code :" + response.code());
						return;
					}
					
					SharedPreferenceHelper.resetGeofenceAndFaceLock(getActivity());
					oneTimeCheckinAnimationCompleted = false;
					oneTimeCheckoutAnimationCompleted = false;
					lateComment = "";
					getAttendanceHistory();
				}
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
					}
				} , 2000);
			}
			
			@Override
			public void onFailure(Call<ApiBaseResponse> call , Throwable t) {
				t.printStackTrace();
				AppUtils.ApiError(t , getActivity());
				AppUtils.makeNotification(t.toString() , getActivity());
				Log.i("response" , t.toString());
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
					}
				} , 2000);
//                tv.setText(t.getMessage());
			}
		});
		
	}
	
	private void getAttendanceHistory() {
		
		if (!AppUtils.checkNetworkState(getActivity())) {
			return;
		}
		
		Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
		
		Urls urls = retrofit.create(Urls.class);
		
		Call<AttendanceHistoryResponse> call = urls.getAttendanceHistory(AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity())) , String.valueOf(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getId()));
		
		call.enqueue(new Callback<AttendanceHistoryResponse>() {
			@Override
			public void onResponse(Call<AttendanceHistoryResponse> call , Response<AttendanceHistoryResponse> response) {
				Log.i("response" , response.toString());
				
				if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_GET , response , getActivity())) {
					{
						if (!response.isSuccessful()) {
//                    tv.setText("Code :" + response.code());
						}
						
						if (response.body() != null && response.body().getResults() != null) {
							attendanceHistoryViewModel.insert(response.body().getResults());
						}
					}
				}
			}
			
			@Override
			public void onFailure(Call<AttendanceHistoryResponse> call , Throwable t) {
				t.printStackTrace();
				AppUtils.ApiError(t , getActivity());
//                AppUtils.makeNotification(t.toString(), getActivity());
				Log.i("response" , t.toString());
//                tv.setText(t.getMessage());
			}
		});
	}
	
	public void setProgressWithAnimation(int total , int left , TextView totalTv , TextView leftTv , ProgressBar progressBar , int progress) {
		
		progressBar.setMax(100);
		leftTv.setText(String.valueOf(left));
		totalTv.setText(String.valueOf(total));
		
		ObjectAnimator anim = ObjectAnimator.ofInt(progressBar , "progress" , progress);
		anim.setDuration(3000);
		anim.setInterpolator(new AccelerateDecelerateInterpolator());
		anim.start();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
	
	public void refreshUI() {
	
	}
	
	public void enableCheckinButton(boolean b) {
		binding.checkin.setEnabled(b);
	}
	
	public void refreshApiCalls() {
		
		ApiManager.getInstance().getToken();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (SharedPreferenceHelper.isInGeofence(getActivity()) && SharedPreferenceHelper.hasFaceLockPath(getActivity())) {
					allowToProceed();
				} else if (SharedPreferenceHelper.isInGeofence(getActivity())) {
					allowToProceed();
				} else if (SharedPreferenceHelper.hasFaceLockPath(getActivity())) {
					allowToProceed();
				}
				getAttendanceHistory();
			}
		} , 1000);
	}
	

	
	
}