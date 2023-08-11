package com.lmkr.prmscemployeeapp.ui.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
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
import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
import com.lmkr.prmscemployeeapp.data.webservice.models.ApiBaseResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.AttendanceHistoryResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;
import com.lmkr.prmscemployeeapp.databinding.FragmentHomeBinding;
import com.lmkr.prmscemployeeapp.ui.activities.CameraXActivity;
import com.lmkr.prmscemployeeapp.ui.adapter.AttendanceHistoryRecyclerAdapter;
import com.lmkr.prmscemployeeapp.ui.adapter.LeavesProgressRecyclerAdapter;
import com.lmkr.prmscemployeeapp.ui.fragments.FullScreenMapFragment;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.AppWideWariables;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;
import com.lmkr.prmscemployeeapp.viewModel.AttendanceHistoryViewModel;
import com.lmkr.prmscemployeeapp.viewModel.AttendanceHistoryViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private static List<AttendanceHistory> attendanceHistories = new ArrayList<>();
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
            if (attendance.getCheckin_time() != null && !TextUtils.isEmpty(attendance.getCheckin_time()) && AppUtils.getConvertedDateFromOneFormatToOther(attendance.getCheckin_time(), AppUtils.FORMAT19, AppUtils.FORMAT3).equals(AppUtils.getCurrentDate())) {
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

        binding.contentHome.recyclerViewAttendanceHistory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        AttendanceHistoryRecyclerAdapter adapter = new AttendanceHistoryRecyclerAdapter(getActivity(), attendanceHistories);
        binding.contentHome.recyclerViewAttendanceHistory.setAdapter(adapter);

        updateCheckInCheckOutProgress(isCheckedIn, isCheckedOut, attendanceHistory);
    }

    private void updateCheckInCheckOutProgress(boolean isCheckedIn, boolean isCheckedOut, AttendanceHistory attendanceHistory) {
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
                binding.checkin.setVisibility(View.VISIBLE);
                binding.checkin.setText(getResources().getText(R.string.checkout));
                binding.time.setText(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getCheckin_time(), AppUtils.FORMAT19, AppUtils.FORMAT5));
                binding.closeImg.setVisibility(View.GONE);
                binding.animationTick.cancelAnimation();
                binding.animationTick.setVisibility(View.VISIBLE);

                objectAnimator = ObjectAnimator.ofInt(binding.circleAnimation, "progress", 1000).setDuration(3000);
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
                binding.checkin.setVisibility(View.VISIBLE);
                binding.checkin.setText(getResources().getText(R.string.checkout));
                binding.time.setText(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getCheckin_time(), AppUtils.FORMAT19, AppUtils.FORMAT5));
                binding.closeImg.setVisibility(View.GONE);
//                binding.animationTick.cancelAnimation();
//                binding.animationTick.setAnimation(R.raw.tick);
                binding.animationTick.setVisibility(View.VISIBLE);
            }
        } else if (isCheckedIn && isCheckedOut) {
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
                binding.time.setText(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getCheckout_time(), AppUtils.FORMAT19, AppUtils.FORMAT5));
                binding.closeImg.setVisibility(View.GONE);
                binding.animationTick.cancelAnimation();
                binding.animationTick.setVisibility(View.VISIBLE);

                objectAnimator = ObjectAnimator.ofInt(binding.circleAnimation, "progress", 1000).setDuration(3000);
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
                binding.time.setText(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getCheckout_time(), AppUtils.FORMAT19, AppUtils.FORMAT5));
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        attendanceHistoryViewModel = new ViewModelProvider(this, new AttendanceHistoryViewModelFactory(App.getInstance(), "%" + AppUtils.getCurrentDate() + "%")).get(AttendanceHistoryViewModel.class);
        attendanceHistoryViewModel.getAttendanceHistory().observe(getViewLifecycleOwner(), attendanceHistoryObserver);


        binding.checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideNotification(getActivity());

                if (AppUtils.isDateTimeAuto(getActivity())) {
                    try {
                        if (SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getGeofence().equals("yes")) {
                            FullScreenMapFragment fragment = FullScreenMapFragment.getInstance();
                            if (!fragment.isAdded()) {
                                fragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Dialog_NoTitle);
                                fragment.show(getActivity().getSupportFragmentManager(), "MapFragment");
                            }
                        } else if (SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getFacelock().equals("yes")) {
                            getActivity().startActivity(new Intent(getActivity(), CameraXActivity.class));
                        } else {
                            if (binding.checkin.getText().equals(getResources().getString(R.string.checkout))) {
                                callCheckOutApi();
                            } else if (binding.checkin.getText().equals(getResources().getString(R.string.checkin))) {
                                callCheckInApi();
                            } else {

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        UserData userData = SharedPreferenceHelper.getLoggedinUser(getActivity());

        binding.contentHome.recyclerviewLeaveProgress.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        LeavesProgressRecyclerAdapter adapter = new LeavesProgressRecyclerAdapter(getActivity(), userData.getLeaveCount());
        binding.contentHome.recyclerviewLeaveProgress.setAdapter(adapter);


        // updateProgressBar() method sets
        // the progress of ProgressBar in text
        binding.textHome.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getName());
//        updateProgressBar();
//        getAttendanceHistory();
        enableCheckinButton(true);
    }

    private void callCheckOutApi() {
        if (!AppUtils.checkNetworkState(getActivity())) {
            return;
        }


        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        Urls urls = retrofit.create(Urls.class);

        JsonObject body = new JsonObject();

        UserData userData = SharedPreferenceHelper.getLoggedinUser(getActivity());

        body.addProperty("checkout_time", AppUtils.getCurrentDateTimeGMT5String());

        Call<ApiBaseResponse> call = urls.checkout(AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity())), String.valueOf(userData.getBasicData().get(0).getId()), body);

        call.enqueue(new Callback<ApiBaseResponse>() {
            @Override
            public void onResponse(Call<ApiBaseResponse> call, Response<ApiBaseResponse> response) {
                Log.i("response", response.toString());

                if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_POST, response, getActivity())) {
                    if (!response.isSuccessful()) {
                        return;
                    }
                    SharedPreferenceHelper.resetGeofenceAndFaceLock(getActivity());
                    oneTimeCheckoutAnimationCompleted = false;
                    getAttendanceHistory();
                }
            }

            @Override
            public void onFailure(Call<ApiBaseResponse> call, Throwable t) {
                t.printStackTrace();
                AppUtils.makeNotification(t.toString(), getActivity());
                Log.i("response", t.toString());
            }
        });
    }

    private void callCheckInApi() {

        if (!AppUtils.checkNetworkState(getActivity())) {
            return;
        }


        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        Urls urls = retrofit.create(Urls.class);

        JsonObject body = new JsonObject();

        UserData userData = SharedPreferenceHelper.getLoggedinUser(getActivity());

        String latitude = SharedPreferenceHelper.getString("lat", getActivity());
        String longitude = SharedPreferenceHelper.getString("long", getActivity());


        body.addProperty("employee_id", userData.getBasicData().get(0).getId());
        body.addProperty("checkin_time", AppUtils.getCurrentDateTimeGMT5String());
        body.addProperty("lat", TextUtils.isEmpty(latitude) ? "0" : latitude);
        body.addProperty("longitude", TextUtils.isEmpty(longitude) ? "0" : longitude);
        body.addProperty("source", AppWideWariables.SOURCE_MOBILE);
        body.addProperty("file_name", "");
        body.addProperty("file_path", "");

//        File file;// = // initialize file here

//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));


        Call<ApiBaseResponse> call = urls.checkIn(AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity())), body);

        call.enqueue(new Callback<ApiBaseResponse>() {
            @Override
            public void onResponse(Call<ApiBaseResponse> call, Response<ApiBaseResponse> response) {
                Log.i("response", response.toString());
                if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_POST, response, getActivity())) {
                    if (!response.isSuccessful()) {
//                    tv.setText("Code :" + response.code());
                        return;
                    }

                    SharedPreferenceHelper.resetGeofenceAndFaceLock(getActivity());
                    oneTimeCheckinAnimationCompleted = false;
                    oneTimeCheckoutAnimationCompleted = false;
                    getAttendanceHistory();
                }
            }

            @Override
            public void onFailure(Call<ApiBaseResponse> call, Throwable t) {
                t.printStackTrace();
                AppUtils.makeNotification(t.toString(), getActivity());
                Log.i("response", t.toString());
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

        Call<AttendanceHistoryResponse> call = urls.getAttendanceHistory(AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity())), String.valueOf(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getId()));

        call.enqueue(new Callback<AttendanceHistoryResponse>() {
            @Override
            public void onResponse(Call<AttendanceHistoryResponse> call, Response<AttendanceHistoryResponse> response) {
                Log.i("response", response.toString());

                if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_GET, response, getActivity())) {
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
            public void onFailure(Call<AttendanceHistoryResponse> call, Throwable t) {
                t.printStackTrace();
                AppUtils.makeNotification(t.toString(), getActivity());
                Log.i("response", t.toString());
//                tv.setText(t.getMessage());
            }
        });
    }


    public void setProgressWithAnimation(int total, int left, TextView totalTv, TextView leftTv, ProgressBar progressBar, int progress) {

        progressBar.setMax(100);
        leftTv.setText(String.valueOf(left));
        totalTv.setText(String.valueOf(total));

        ObjectAnimator anim = ObjectAnimator.ofInt(progressBar, "progress", progress);
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

        if (SharedPreferenceHelper.isInGeofence(getActivity()) || SharedPreferenceHelper.hasFaceLockPath(getActivity())) {
            if (binding.checkin.getText().equals(getResources().getString(R.string.checkout))) {
                callCheckOutApi();
            } else if (binding.checkin.getText().equals(getResources().getString(R.string.checkin))) {
                callCheckInApi();
            } else {

            }
        }

        getAttendanceHistory();
    }
}