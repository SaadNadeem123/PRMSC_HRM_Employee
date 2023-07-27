package com.lmkr.prmscemployeeapp.ui.home;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lmkr.prmscemployeeapp.CameraXActivity;
import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.databinding.FragmentHomeBinding;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;

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
                getActivity().startActivity(new Intent(getActivity(), CameraXActivity.class));
                AppUtils.hideNotification(getActivity());
            }
        });


        // updateProgressBar() method sets
        // the progress of ProgressBar in text
        updateProgressBar(12, 8, 12, 5, 17, 5);

    }

    private void updateProgressBar(int totalCasual, int leftCasual, int totalSick, int leftSick, int totalAnnual, int leftAnnual) {

        binding.circleAnimation.setMax(1000);
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

        setProgressWithAnimation(totalCasual,leftCasual,binding.contentHome.totalCasual,binding.contentHome.leftCasual,binding.contentHome.progressBarCasualLeaves,Math.round((leftCasual*100)/totalCasual));
        setProgressWithAnimation(totalSick,leftSick,binding.contentHome.totalSick,binding.contentHome.leftSick,binding.contentHome.progressBarSickLeaves,Math.round((leftSick*100)/totalSick));
        setProgressWithAnimation(totalAnnual,leftAnnual,binding.contentHome.totalAnnual,binding.contentHome.leftAnnual,binding.contentHome.progressBarAnnualLeaves,Math.round((leftAnnual*100)/totalAnnual));

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