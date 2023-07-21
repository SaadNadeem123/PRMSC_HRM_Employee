package com.lmkr.prmscemployeeapp.ui.bulletin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lmkr.prmscemployeeapp.databinding.FragmentBulletinBinding;

public class BulletinFragment extends Fragment {

    private FragmentBulletinBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BulletinViewModel bulletinViewModel = new ViewModelProvider(this).get(BulletinViewModel.class);

        binding = FragmentBulletinBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        bulletinViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}