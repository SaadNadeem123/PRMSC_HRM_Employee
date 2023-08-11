package com.lmkr.prmscemployeeapp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lmkr.prmscemployeeapp.ui.myinfo.benefitsUi.BenefitsFragment;
import com.lmkr.prmscemployeeapp.ui.myinfo.documentsUi.DocumentsFragment;
import com.lmkr.prmscemployeeapp.ui.myinfo.emergencyUi.EmergencyFragment;
import com.lmkr.prmscemployeeapp.ui.myinfo.jobUi.JobFragment;
import com.lmkr.prmscemployeeapp.ui.myinfo.performanceUi.performanceFragment;
import com.lmkr.prmscemployeeapp.ui.myinfo.personalUi.personalFragment;


public  class ViewPagerAdapter extends FragmentPagerAdapter {

    private final Fragment[] fragments = {new personalFragment(), new JobFragment() ,
            new performanceFragment(),new EmergencyFragment(),new DocumentsFragment(),new BenefitsFragment()};
    private final String[] titles = {"Personal", "Job", "Performance","Emergency","Documents","Benefits"};

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
