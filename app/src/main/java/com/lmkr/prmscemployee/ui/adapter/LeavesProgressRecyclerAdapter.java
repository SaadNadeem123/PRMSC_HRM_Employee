package com.lmkr.prmscemployee.ui.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmkr.prmscemployee.R;
import com.lmkr.prmscemployee.data.webservice.models.LeaveCount;
import com.lmkr.prmscemployee.databinding.RecyclerItemLeaveProgressBinding;
import com.lmkr.prmscemployee.ui.utilities.AppUtils;
import com.lmkr.prmscemployee.ui.utilities.AppWideWariables;

import java.util.List;

public class LeavesProgressRecyclerAdapter extends RecyclerView.Adapter<LeavesProgressRecyclerAdapter.ViewHolder> {

    Context context;
    List<LeaveCount> list;

    public LeavesProgressRecyclerAdapter(Context context, List<LeaveCount> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LeavesProgressRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerItemLeaveProgressBinding binding = RecyclerItemLeaveProgressBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeavesProgressRecyclerAdapter.ViewHolder holder, int position) {

        LeaveCount leaveCount = list.get(position);

        holder.label.setText(leaveCount.getType());
        holder.leftLeaves.setText(AppUtils.getFloatOrInteger(leaveCount.getRemaining()));
        holder.totalLeaves.setText(leaveCount.getTotal()+"");
        holder.progressBar.setMax(100);


        switch (leaveCount.getType()) {
            case AppWideWariables.LEAVE_TYPE_CASUAL:
                holder.image.setImageResource(R.drawable.casual_leaves);
                break;
            case AppWideWariables.LEAVE_TYPE_SICK:
                holder.image.setImageResource(R.drawable.sick_leaves);
                break;
            case AppWideWariables.LEAVE_TYPE_ANNUAL:
                holder.image.setImageResource(R.drawable.annual_leaves);
                break;
            default:
                holder.image.setImageResource(R.drawable.casual_leaves);
                break;
        }

        ObjectAnimator anim = ObjectAnimator.ofInt(holder.progressBar, "progress", Math.round((leaveCount.getRemaining() * 100) / leaveCount.getTotal()));
        anim.setDuration(3000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;
        TextView leftLeaves;
        TextView totalLeaves;
        ImageView image;
        TextView label;

        public ViewHolder(RecyclerItemLeaveProgressBinding binding) {
            super(binding.getRoot());
            progressBar = binding.progressBar;
            leftLeaves = binding.left;
            totalLeaves = binding.total;
            image = binding.image;
            label = binding.label;
        }
    }
}
