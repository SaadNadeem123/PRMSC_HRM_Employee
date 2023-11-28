package com.lmkr.prmscemployee.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmkr.prmscemployee.R;
import com.lmkr.prmscemployee.data.webservice.models.LeaveManagementModel;
import com.lmkr.prmscemployee.databinding.RecyclerItemLeaveManagementBinding;
import com.lmkr.prmscemployee.ui.utilities.AppUtils;
import com.lmkr.prmscemployee.ui.utilities.AppWideWariables;

import java.util.HashMap;
import java.util.List;

public class LeaveManagementRecyclerAdapter extends RecyclerView.Adapter<LeaveManagementRecyclerAdapter.ViewHolder> {
	
	private static OnItemClickListener listener;
	private final Context context;
	HashMap<String, Integer> header = new HashMap<>();
	private final List<LeaveManagementModel> leaveManagement;
	
	public LeaveManagementRecyclerAdapter(List<LeaveManagementModel> leaveManagement , Context context) {
		this.leaveManagement = leaveManagement;
		this.context = context;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
		RecyclerItemLeaveManagementBinding binding = RecyclerItemLeaveManagementBinding.inflate(LayoutInflater.from(context) , parent , false);
		return new ViewHolder(binding);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder , int position) {
		
		LeaveManagementModel leaveManagementModel = leaveManagement.get(position);
		
		if (header.containsKey(AppUtils.getConvertedDateFromOneFormatToOther(leaveManagementModel.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_MONTH_YEAR))) {
			if (header.get(AppUtils.getConvertedDateFromOneFormatToOther(leaveManagementModel.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_MONTH_YEAR)) == position) {
				holder.year.setText(AppUtils.getConvertedDateFromOneFormatToOther(leaveManagementModel.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_MONTH_YEAR));
				holder.year.setVisibility(View.VISIBLE);
			} else {
				holder.year.setText("");
				holder.year.setVisibility(View.GONE);
			}
		} else {
			header.put(AppUtils.getConvertedDateFromOneFormatToOther(leaveManagementModel.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_MONTH_YEAR) , position);
			holder.year.setText(AppUtils.getConvertedDateFromOneFormatToOther(leaveManagementModel.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_MONTH_YEAR));
			holder.year.setVisibility(View.VISIBLE);
		}
		
		String days = leaveManagementModel.getTotal_days()>=1?((int)leaveManagementModel.getTotal_days())+"":leaveManagementModel.getTotal_days()+"";
		String totaldays = days + " " + (leaveManagementModel.getTotal_days() > 1 ? context.getString(R.string.days) : context.getString(R.string.day));
		holder.date.setText(AppUtils.getConvertedDateFromOneFormatToOther(leaveManagementModel.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_DAY) + " - " + AppUtils.getConvertedDateFromOneFormatToOther(leaveManagementModel.getTo_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_DAY) + " (" + totaldays + ")");
		holder.leaveType.setText(leaveManagementModel.getLeave_type_name());
		holder.name.setText(leaveManagementModel.getEmployee_name());
		
		switch (leaveManagementModel.getLeave_type_name()) {
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
		switch (leaveManagementModel.getStatus()) {
			case AppWideWariables.PENDING:
				holder.status.setBackgroundColor(context.getColor(R.color.grey_dark));
				holder.statusLabel.setTextColor(context.getColor(R.color.grey_dark));
				holder.statusLabel.setText(context.getString(R.string.pending));
				break;
			case AppWideWariables.APPROVED:
				holder.status.setBackgroundColor(context.getColor(R.color.app_green));
				holder.statusLabel.setTextColor(context.getColor(R.color.app_green));
				holder.statusLabel.setText(context.getString(R.string.approved));
				break;
			case AppWideWariables.REJECTED:
				holder.status.setBackgroundColor(context.getColor(R.color.red));
				holder.statusLabel.setTextColor(context.getColor(R.color.red));
				holder.statusLabel.setText(context.getString(R.string.rejected));
				break;
		}
	}
	
	@Override
	public int getItemCount() {
		return leaveManagement.size();
	}
	
	@SuppressLint("NotifyDataSetChanged")
	public void setLeaveManagement(List<LeaveManagementModel> leaveManagement) {
		this.leaveManagement.clear();
		if (leaveManagement != null) {
			this.leaveManagement.addAll(leaveManagement);
		}
		notifyDataSetChanged();
	}
	
	public void setListener(OnItemClickListener listener) {
		LeaveManagementRecyclerAdapter.listener = listener;
	}
	
	public interface OnItemClickListener {
		
		void OnItemClickListener(LeaveManagementModel leaveManagementModel);
		
	}
	
	public class ViewHolder extends RecyclerView.ViewHolder {
		
		TextView name;
		TextView year;
		TextView date;
		TextView leaveType;
		TextView statusLabel;
		ImageView image;
		View status;
		
		public ViewHolder(RecyclerItemLeaveManagementBinding binding) {
			super(binding.getRoot());
			name = binding.name;
			year = binding.year;
			date = binding.date;
			leaveType = binding.leaveType;
			image = binding.image;
			status = binding.status;
			statusLabel = binding.statusLabel;
			
			AppUtils.setRippleAnimation(context , binding.getRoot());
			
			binding.getRoot().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int pos = getAdapterPosition();
					if (listener != null && pos != RecyclerView.NO_POSITION) {
						listener.OnItemClickListener(leaveManagement.get(pos));
					}
				}
			});
		}
		
	}
	
}
