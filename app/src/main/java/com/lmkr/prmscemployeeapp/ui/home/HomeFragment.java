package com.lmkr.prmscemployeeapp.ui.home;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
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
import com.lmkr.prmscemployeeapp.data.webservice.api.JsonObjectResponse;
import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private static List<AttendanceHistory> attendanceHistories = null;
    private FragmentHomeBinding binding;
    private Observer<? super List<AttendanceHistory>> attendanceHistoryObserver = new Observer<List<AttendanceHistory>>() {
        @Override
        public void onChanged(List<AttendanceHistory> attendanceHistories) {
            HomeFragment.attendanceHistories = attendanceHistories;
            loadAttendanceHistoryData();
        }
    };
    private AttendanceHistoryViewModel attendanceHistoryViewModel;

    private void loadAttendanceHistoryData() {
        binding.contentHome.recyclerViewAttendanceHistory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        AttendanceHistoryRecyclerAdapter adapter = new AttendanceHistoryRecyclerAdapter(getActivity(), attendanceHistories);
        binding.contentHome.recyclerViewAttendanceHistory.setAdapter(adapter);

        boolean isCheckedIn = false;
        for (AttendanceHistory attendanceHistory : attendanceHistories) {
            if (AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getDate(),AppUtils.FORMAT19,AppUtils.FORMAT3).equals(AppUtils.getCurrentDate())) {
                isCheckedIn = true;
            }
        }

        if (isCheckedIn) {
            SharedPreferenceHelper.saveBoolean(SharedPreferenceHelper.IS_CHECKED_IN, true, getActivity());
        } else {
            SharedPreferenceHelper.saveBoolean(SharedPreferenceHelper.IS_CHECKED_IN, false, getActivity());
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

                if (SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getGeofence().equals("yes")) {
                    FullScreenMapFragment fragment = FullScreenMapFragment.getInstance();
                    if (!fragment.isAdded()) {
                        fragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Dialog_NoTitle);
                        fragment.show(getActivity().getSupportFragmentManager(), "MapFragment");
                    }
                } else if (SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getFacelock().equals("yes")) {
                    getActivity().startActivity(new Intent(getActivity(), CameraXActivity.class));
                } else {
                    callCheckInApi();
                }
            }
        });


        // updateProgressBar() method sets
        // the progress of ProgressBar in text
        binding.textHome.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getName());
        updateProgressBar();
        getAttendanceHistory();
        enableCheckinButton(true);
    }

    private void callCheckInApi() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        Urls urls = retrofit.create(Urls.class);

        JsonObject body = new JsonObject();

        UserData userData = SharedPreferenceHelper.getLoggedinUser(getActivity());

        body.addProperty("employee_id", userData.getBasicData().get(0).getId());
        body.addProperty("checkin_time", AppUtils.getCurrentDateTimeGMT5String());
        body.addProperty("lat", 0);
        body.addProperty("longitude", 0);
        body.addProperty("source", AppWideWariables.SOURCE_MOBILE);
        body.addProperty("file_name", "");
        body.addProperty("file_path", "");

//        File file;// = // initialize file here

//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));


        Call<JsonObjectResponse> call = urls.checkIn(AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity())), body);

        call.enqueue(new Callback<JsonObjectResponse>() {
            @Override
            public void onResponse(Call<JsonObjectResponse> call, Response<JsonObjectResponse> response) {
                Log.i("response", response.toString());

                if (!response.isSuccessful()) {
//                    tv.setText("Code :" + response.code());
                    return;
                }

//                SharedPreferenceHelper.saveBoolean(SharedPreferenceHelper.IS_CHECKED_IN, true, getActivity());
//                updateProgressBar();
                getAttendanceHistory();
            }

            @Override
            public void onFailure(Call<JsonObjectResponse> call, Throwable t) {
                t.printStackTrace();
                AppUtils.makeNotification(t.toString(), getActivity());
                Log.i("response", t.toString());
//                tv.setText(t.getMessage());
            }
        });

    }

    private void getAttendanceHistory() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        Urls urls = retrofit.create(Urls.class);

        Call<AttendanceHistoryResponse> call = urls.getAttendanceHistory(AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity())), String.valueOf(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getId()));

        call.enqueue(new Callback<AttendanceHistoryResponse>() {
            @Override
            public void onResponse(Call<AttendanceHistoryResponse> call, Response<AttendanceHistoryResponse> response) {
                Log.i("response", response.toString());

                if (!response.isSuccessful()) {
//                    tv.setText("Code :" + response.code());
                }

                if (response.body() != null && response.body().getResults() != null) {
                    attendanceHistoryViewModel.insert(response.body().getResults());
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

    private void updateProgressBar() {

        if (SharedPreferenceHelper.isCheckedIn(getActivity())) {

            binding.circleAnimation.setMax(1000);
            binding.checkedIn.setText(getResources().getText(R.string.checkedin));
            binding.time.setText("na");
            binding.circleAnimation.setProgress(1000);
            binding.closeImg.setVisibility(View.GONE);
            binding.animationTick.cancelAnimation();
            binding.animationTick.setVisibility(View.VISIBLE);

            ObjectAnimator objectAnimator = ObjectAnimator.ofInt(binding.circleAnimation, "progress", 1000).setDuration(3000);
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animation) {

                }

                @Override
                public void onAnimationEnd(@NonNull Animator animation) {
                    binding.animationTick.setAnimation(R.raw.tick);
                    binding.animationTick.playAnimation();
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
            binding.checkedIn.setText(getResources().getText(R.string.checkedout));
            binding.time.setText("");
            binding.circleAnimation.setProgress(0);
            binding.closeImg.setVisibility(View.VISIBLE);
            binding.animationTick.cancelAnimation();
            binding.animationTick.setVisibility(View.GONE);
        }

        binding.contentHome.recyclerviewLeaveProgress.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        LeavesProgressRecyclerAdapter adapter = new LeavesProgressRecyclerAdapter(getActivity(), SharedPreferenceHelper.getLoggedinUser(getActivity()).getLeaveCount());
        binding.contentHome.recyclerviewLeaveProgress.setAdapter(adapter);

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
        getAttendanceHistory();
    }
}