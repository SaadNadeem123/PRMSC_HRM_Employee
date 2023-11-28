package com.lmkr.prmscemployee.ui.locationUtils;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lmkr.prmscemployee.R;
import com.lmkr.prmscemployee.ui.fragments.BaseDialogFragment;

public class LocationAlertDialogFragment extends BaseDialogFragment {

    public static final int LOACTION_ENABLE_REQUEST = 1;
    private static LocationAlertDialogFragment INSTANCE;
    private final View.OnClickListener OnCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    };

    private final View.OnClickListener OnEnableClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            (getActivity()).startActivityForResult(intent, LOACTION_ENABLE_REQUEST);

            dismiss();
        }
    };

    private Button cancel, enable;

    public LocationAlertDialogFragment() {
        // Required empty public constructor
    }

    public static LocationAlertDialogFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LocationAlertDialogFragment();
        }
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_alert_dialog, container, false);
        initializeViews(view);
        return view;
    }

    @Override
    public void initializeViews(View view) {
        cancel = view.findViewById(R.id.cancel);
        enable = view.findViewById(R.id.enable);

        setListeners();
    }

    @Override
    public void setListeners() {
        cancel.setOnClickListener(OnCancelClickListener);
        enable.setOnClickListener(OnEnableClickListener);
    }
}
