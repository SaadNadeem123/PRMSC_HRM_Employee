package com.lmkr.prmscemployeeapp.ui.bulletin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lmkr.prmscemployeeapp.databinding.FragmentBulletinBinding;
import com.lmkr.prmscemployeeapp.ui.adapter.BulletinRecyclerAdapter;
import com.lmkr.prmscemployeeapp.viewModel.BulletinViewModel;

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
        });

        bulletinViewModel.fetchBulletinData();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}