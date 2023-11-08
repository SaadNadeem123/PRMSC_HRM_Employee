package com.lmkr.prmscemployeeapp.ui.bulletin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.databinding.ActivityBulletinDetailBinding;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;

public class BulletinDetailActivity extends AppCompatActivity {

    ActivityBulletinDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBulletinDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String title = bundle.getString("title");
            String description = bundle.getString("description");
            String date = bundle.getString("date");

            binding.title.setText(title);
            binding.description.setText(description);
            binding.date.setText(AppUtils.getDifferenceBetweenDates(this,date,AppUtils.getCurrentTimeStampGMT5(AppUtils.FORMAT21)));
        }

    }
}