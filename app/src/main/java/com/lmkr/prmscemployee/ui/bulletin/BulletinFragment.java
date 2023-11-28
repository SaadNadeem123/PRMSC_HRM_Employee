package com.lmkr.prmscemployee.ui.bulletin;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lmkr.prmscemployee.data.webservice.api.ApiManager;
import com.lmkr.prmscemployee.databinding.FragmentBulletinBinding;
import com.lmkr.prmscemployee.ui.adapter.BulletinRecyclerAdapter;
import com.lmkr.prmscemployee.viewModel.BulletinViewModel;

import java.util.ArrayList;

public class BulletinFragment extends Fragment {

    private BulletinRecyclerAdapter adapter;
    private BulletinViewModel bulletinViewModel;
    private FragmentBulletinBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBulletinBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerView;

        adapter = new BulletinRecyclerAdapter(new ArrayList<>(),getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        bulletinViewModel = new ViewModelProvider(this).get(BulletinViewModel.class);
        bulletinViewModel.getBulletinList().observe(getViewLifecycleOwner(), bulletinList -> {
            adapter.setBulletinList(bulletinList);
            binding.progress.setVisibility(View.GONE);
        });
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        refreshApiCalls();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    
    public void refreshApiCalls() {
        ApiManager.getInstance().getToken();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.progress.setVisibility(View.VISIBLE);
                bulletinViewModel.fetchBulletinData();
            }
        }, 100);
    }
}