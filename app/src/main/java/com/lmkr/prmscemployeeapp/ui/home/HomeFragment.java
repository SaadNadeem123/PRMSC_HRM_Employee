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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.lmkr.prmscemployeeapp.data.webservice.api.ApiCalls;
import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
import com.lmkr.prmscemployeeapp.data.webservice.models.LeaveCount;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;
import com.lmkr.prmscemployeeapp.databinding.FragmentHomeBinding;
import com.lmkr.prmscemployeeapp.ui.activities.CameraXActivity;
import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.ui.activities.GeofenceMapsActivity;
import com.lmkr.prmscemployeeapp.ui.activities.LoginActivity;
import com.lmkr.prmscemployeeapp.ui.activities.MainActivity;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.AppWideWariables;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), GeofenceMapsActivity.class));
//                getActivity().startActivity(new Intent(getActivity(), CameraXActivity.class));
                AppUtils.hideNotification(getActivity());
            }
        });


        // updateProgressBar() method sets
        // the progress of ProgressBar in text
        binding.textHome.setText(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getName());
        updateProgressBar();
        getAttendanceHistory();
    }

    private void getAttendanceHistory() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        Urls urls = retrofit.create(Urls.class);

        Call<UserData> call = urls.getAttendanceHistory(AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity())), String.valueOf(SharedPreferenceHelper.getLoggedinUser(getActivity()).getBasicData().get(0).getId()));

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                Log.i("response", response.toString());

                if (!response.isSuccessful()) {
//                    tv.setText("Code :" + response.code());
                }

//                SharedPreferenceHelper.setLoggedinUser(getActivity(), response.body());
//                AppUtils.switchActivity(LoginActivity.this, MainActivity.class, null);
//                finish();
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                t.printStackTrace();
                AppUtils.makeNotification(t.toString(), getActivity());
                Log.i("response", t.toString());
//                tv.setText(t.getMessage());
            }
        });
    }

    private void updateProgressBar() {

        if(SharedPreferenceHelper.isCheckedIn(getActivity())) {

            binding.circleAnimation.setMax(1000);
            binding.checkedIn.setText(getResources().getText(R.string.checkedin));
            binding.time.setText("10:00 AM");
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
        }
        else
        {
            binding.checkedIn.setText(getResources().getText(R.string.checkedout));
            binding.time.setText("");
            binding.circleAnimation.setProgress(0);
            binding.closeImg.setVisibility(View.VISIBLE);
            binding.animationTick.cancelAnimation();
            binding.animationTick.setVisibility(View.GONE);
        }



        for (LeaveCount leave: SharedPreferenceHelper.getLoggedinUser(getActivity()).getLeaveCount()) {

            if(leave.getType().equals(AppWideWariables.LEAVE_TYPE_CASUAL))
            {
                binding.contentHome.layoutCasual.setVisibility(View.VISIBLE);
                setProgressWithAnimation(leave.getTotal(),leave.getRemaining(),binding.contentHome.totalCasual,binding.contentHome.leftCasual,binding.contentHome.progressBarCasualLeaves,Math.round((leave.getRemaining()*100)/leave.getTotal()));
            }
            if(leave.getType().equals(AppWideWariables.LEAVE_TYPE_SICK))
            {
                binding.contentHome.layoutSick.setVisibility(View.VISIBLE);
                setProgressWithAnimation(leave.getTotal(),leave.getRemaining(),binding.contentHome.totalSick,binding.contentHome.leftSick,binding.contentHome.progressBarSickLeaves,Math.round((leave.getRemaining()*100)/leave.getTotal()));
            }
            if(leave.getType().equals(AppWideWariables.LEAVE_TYPE_ANNUAL))
            {
                binding.contentHome.layoutAnnual.setVisibility(View.VISIBLE);
                setProgressWithAnimation(leave.getTotal(),leave.getRemaining(),binding.contentHome.totalAnnual,binding.contentHome.leftAnnual,binding.contentHome.progressBarAnnualLeaves,Math.round((leave.getRemaining()*100)/leave.getTotal()));
            }
        }

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
}