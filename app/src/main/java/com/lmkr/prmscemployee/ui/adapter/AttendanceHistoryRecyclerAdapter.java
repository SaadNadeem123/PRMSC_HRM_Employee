package com.lmkr.prmscemployee.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmkr.prmscemployee.data.database.models.AttendanceHistory;
import com.lmkr.prmscemployee.databinding.RecyclerItemAttendanceHistoryBinding;
import com.lmkr.prmscemployee.ui.utilities.AppUtils;

import java.util.HashMap;
import java.util.List;

public class AttendanceHistoryRecyclerAdapter extends RecyclerView.Adapter<AttendanceHistoryRecyclerAdapter.ViewHolder> {
	
	private static OnItemClickListener listener;
	Context context;
	List<AttendanceHistory> list;
	HashMap<String, Integer> header = new HashMap<>();
	
	public AttendanceHistoryRecyclerAdapter(Context context , List<AttendanceHistory> list) {
		this.context = context;
		this.list = list;
	}
	
	public void setListener(OnItemClickListener listener) {
		AttendanceHistoryRecyclerAdapter.listener = listener;
	}
	
	@NonNull
	@Override
	public AttendanceHistoryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
		RecyclerItemAttendanceHistoryBinding binding = RecyclerItemAttendanceHistoryBinding.inflate(LayoutInflater.from(context) , parent , false);
		return new ViewHolder(binding);
	}
	
	@Override
	public void onBindViewHolder(@NonNull AttendanceHistoryRecyclerAdapter.ViewHolder holder , int position) {
		
		AttendanceHistory attendanceHistory = list.get(position);
		
		if (header.containsKey(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getCheckin_time() , AppUtils.FORMAT19 , AppUtils.FORMAT23))) {
			if (header.get(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getCheckin_time() , AppUtils.FORMAT19 , AppUtils.FORMAT23)) == position) {
				holder.dateLabel.setText(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getCheckin_time() , AppUtils.FORMAT19 , AppUtils.FORMAT23));
				holder.dateLabel.setVisibility(View.VISIBLE);
			} else {
				holder.dateLabel.setText("");
				holder.dateLabel.setVisibility(View.GONE);
			}
		} else {
			header.put(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getCheckin_time() , AppUtils.FORMAT19 , AppUtils.FORMAT23) , position);
			holder.dateLabel.setText(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getCheckin_time() , AppUtils.FORMAT19 , AppUtils.FORMAT23));
			holder.dateLabel.setVisibility(View.VISIBLE);
		}
		
		if (!TextUtils.isEmpty(attendanceHistory.getCheckout_time()) && attendanceHistory.getCheckout_time() != null && !attendanceHistory.getCheckout_time().equals("null")) {
			holder.timeCheckout.setText(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getCheckout_time() , AppUtils.FORMAT19 , AppUtils.FORMAT5));
			holder.lLCheckout.setVisibility(View.VISIBLE);
		} else {
			holder.lLCheckout.setVisibility(View.GONE);
		}
		
		if (attendanceHistory.getLateReason() != null && !TextUtils.isEmpty(attendanceHistory.getLateReason())) {
			holder.reason.setText(attendanceHistory.getLateReason());
			holder.lLReason.setVisibility(View.VISIBLE);
		} else {
			holder.lLReason.setVisibility(View.GONE);
		}
		holder.timeCheckin.setText(AppUtils.getConvertedDateFromOneFormatToOther(attendanceHistory.getCheckin_time() , AppUtils.FORMAT19 , AppUtils.FORMAT5));
	}
	
	@Override
	public int getItemCount() {
		return list.size();
	}
	
	@Override
	public int getItemViewType(int position) {
		return position;
	}
	
	public interface OnItemClickListener {
		
		void OnItemClickListener(AttendanceHistory attendanceHistory);
		
	}
	
	public class ViewHolder extends RecyclerView.ViewHolder {
		
		TextView timeCheckin;
		TextView dateLabel;
		TextView timeCheckout;
		TextView reason;
		LinearLayout lLCheckout;
		LinearLayout lLReason;
		
		public ViewHolder(RecyclerItemAttendanceHistoryBinding binding) {
			super(binding.getRoot());
			timeCheckin = binding.timeCheckin;
			dateLabel = binding.dateLabel;
			timeCheckout = binding.timeCheckout;
			lLCheckout = binding.llCheckout;
			lLReason = binding.llLateReason;
			reason = binding.reason;
			
			AppUtils.setRippleAnimation(context , binding.getRoot());
			
			binding.getRoot().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int pos = getAdapterPosition();
					if (listener != null && pos != RecyclerView.NO_POSITION) {
						listener.OnItemClickListener(list.get(pos));
					}
				}
			});
		}
		
	}
	
}
