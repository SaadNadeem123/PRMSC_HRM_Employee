package com.lmkr.prmscemployee.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmkr.prmscemployee.R;
import com.lmkr.prmscemployee.data.webservice.models.LeaveCount;
import com.lmkr.prmscemployee.databinding.RecyclerItemLeaveAvailableBinding;
import com.lmkr.prmscemployee.ui.utilities.AppUtils;
import com.lmkr.prmscemployee.ui.utilities.AppWideWariables;

import java.util.List;

public class LeavesRemainingRecyclerAdapter extends RecyclerView.Adapter<LeavesRemainingRecyclerAdapter.ViewHolder> {

    Context context;
    List<LeaveCount> list;

    public LeavesRemainingRecyclerAdapter(Context context, List<LeaveCount> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LeavesRemainingRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerItemLeaveAvailableBinding binding = RecyclerItemLeaveAvailableBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeavesRemainingRecyclerAdapter.ViewHolder holder, int position) {

        LeaveCount leaveCount = list.get(position);

        holder.label.setText(leaveCount.getType());
        holder.leftLeaves.setText(AppUtils.getFloatOrInteger(leaveCount.getRemaining()));


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
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView leftLeaves;
        ImageView image;
        TextView label;

        public ViewHolder(RecyclerItemLeaveAvailableBinding binding) {
            super(binding.getRoot());
            leftLeaves = binding.left;
            image = binding.image;
            label = binding.label;
        }
    }
}
