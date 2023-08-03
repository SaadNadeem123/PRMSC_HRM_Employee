package com.lmkr.prmscemployeeapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmkr.prmscemployeeapp.data.database.models.AttendanceHistory;
import com.lmkr.prmscemployeeapp.databinding.RecyclerItemAttendanceHistoryBinding;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;

import java.util.List;

public class AttendanceHistoryRecyclerAdapter extends RecyclerView.Adapter<AttendanceHistoryRecyclerAdapter.ViewHolder> {

    Context context;
    List<AttendanceHistory> list;

    public AttendanceHistoryRecyclerAdapter(Context context, List<AttendanceHistory> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AttendanceHistoryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerItemAttendanceHistoryBinding binding = RecyclerItemAttendanceHistoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceHistoryRecyclerAdapter.ViewHolder holder, int position) {

        AttendanceHistory attendanceHistory = list.get(position);

        holder.date.setText(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getDate(),AppUtils.FORMAT19,AppUtils.FORMAT23));
        holder.time.setText(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getCheckin_time(),AppUtils.FORMAT19,AppUtils.FORMAT5));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView time;

        public ViewHolder(RecyclerItemAttendanceHistoryBinding binding) {
            super(binding.getRoot());
            date = binding.date;
            time = binding.time;
        }
    }
}
