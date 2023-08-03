package com.lmkr.prmscemployeeapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.data.database.models.AttendanceHistory;
import com.lmkr.prmscemployeeapp.data.database.models.LeaveRequest;
import com.lmkr.prmscemployeeapp.databinding.RecyclerItemAttendanceHistoryBinding;
import com.lmkr.prmscemployeeapp.databinding.RecyclerItemLeaveRequestBinding;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.AppWideWariables;

import java.util.List;

public class LeaveRequestRecyclerAdapter extends RecyclerView.Adapter<LeaveRequestRecyclerAdapter.ViewHolder> {

    Context context;
    List<LeaveRequest> list;

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

        holder.date.setText(AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getFrom_date(),AppUtils.FORMAT19,AppUtils.FORMAT24)+" - "+AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getTo_date(),AppUtils.FORMAT19,AppUtils.FORMAT24));
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

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView leaveType;
        ImageView image;

        public ViewHolder(RecyclerItemLeaveRequestBinding binding) {
            super(binding.getRoot());
            date = binding.date;
            leaveType = binding.leaveType;
            image= binding.image;
        }
    }
}
