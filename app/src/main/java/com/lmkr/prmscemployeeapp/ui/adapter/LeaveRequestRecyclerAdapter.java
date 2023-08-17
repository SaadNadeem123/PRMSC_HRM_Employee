package com.lmkr.prmscemployeeapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.data.database.models.LeaveRequest;
import com.lmkr.prmscemployeeapp.databinding.RecyclerItemLeaveRequestBinding;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.AppWideWariables;

import java.util.HashMap;
import java.util.List;

public class LeaveRequestRecyclerAdapter extends RecyclerView.Adapter<LeaveRequestRecyclerAdapter.ViewHolder> {

    Context context;
    List<LeaveRequest> list;
    HashMap<String, Integer> header = new HashMap<>();

    public LeaveRequestRecyclerAdapter(Context context, List<LeaveRequest> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LeaveRequestRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerItemLeaveRequestBinding binding = RecyclerItemLeaveRequestBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveRequestRecyclerAdapter.ViewHolder holder, int position) {

        LeaveRequest leaveRequest = list.get(position);

        if (header.containsKey(AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getFrom_date(), AppUtils.FORMAT19, AppUtils.FORMAT_MONTH_YEAR))) {
            if (header.get(AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getFrom_date(), AppUtils.FORMAT19, AppUtils.FORMAT_MONTH_YEAR)) == position) {
                holder.year.setText(AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getFrom_date(), AppUtils.FORMAT19, AppUtils.FORMAT_MONTH_YEAR));
                holder.year.setVisibility(View.VISIBLE);
            } else {
                holder.year.setText("");
                holder.year.setVisibility(View.GONE);
            }
        } else {
            header.put(AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getFrom_date(), AppUtils.FORMAT19, AppUtils.FORMAT_MONTH_YEAR), position);
            holder.year.setText(AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getFrom_date(), AppUtils.FORMAT19, AppUtils.FORMAT_MONTH_YEAR));
            holder.year.setVisibility(View.VISIBLE);
        }

        holder.date.setText(AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getFrom_date(), AppUtils.FORMAT19, AppUtils.FORMAT_DAY) + " - " + AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getTo_date(), AppUtils.FORMAT19, AppUtils.FORMAT_DAY));
        holder.leaveType.setText(leaveRequest.getLeave_type_name());

        switch (leaveRequest.getLeave_type_name()) {
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
        switch (leaveRequest.getStatus()) {
            case AppWideWariables.PENDING:
                holder.status.setBackgroundColor(context.getColor(R.color.grey_dark));
                holder.statusLabel.setTextColor(context.getColor(R.color.grey_dark));
                break;
            case AppWideWariables.APPROVED:
                holder.status.setBackgroundColor(context.getColor(R.color.app_green));
                holder.statusLabel.setTextColor(context.getColor(R.color.app_green));
                break;
            case AppWideWariables.REJECTED:
                holder.status.setBackgroundColor(context.getColor(R.color.red));
                holder.statusLabel.setTextColor(context.getColor(R.color.red));
                break;
        }
        holder.statusLabel.setText(leaveRequest.getStatus());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView year;
        TextView date;
        TextView leaveType;
        TextView statusLabel;
        ImageView image;
        View status;

        public ViewHolder(RecyclerItemLeaveRequestBinding binding) {
            super(binding.getRoot());
            year = binding.year;
            date = binding.date;
            leaveType = binding.leaveType;
            image = binding.image;
            status = binding.status;
            statusLabel = binding.statusLabel;
        }
    }
}
