package com.lmkr.prmscemployeeapp.ui.myinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.lmkr.prmscemployeeapp.databinding.FragmentMyinfoBinding;
import com.lmkr.prmscemployeeapp.ui.adapter.ViewPagerAdapter;
import com.lmkr.prmscemployeeapp.ui.myinfo.emergencyUi.EmergencyFragment;

public class MyInfoFragment extends Fragment {

    private FragmentMyinfoBinding binding;
    private ViewPagerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMyinfoBinding.inflate(inflater, container, false);

        setupViewPager();
        return binding.getRoot();
    }

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void refreshApiCalls() {
        if (adapter != null && adapter.getItem(3) != null && adapter.getItem(3) instanceof EmergencyFragment) {
            ((EmergencyFragment) adapter.getItem(3)).callApi();
        }
    }
}
