package com.lmkr.prmscemployee.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmkr.prmscemployee.R;
import com.lmkr.prmscemployee.data.database.models.LeaveRequest;
import com.lmkr.prmscemployee.databinding.RecyclerItemLeaveRequestBinding;
import com.lmkr.prmscemployee.ui.utilities.AppUtils;
import com.lmkr.prmscemployee.ui.utilities.AppWideWariables;

import java.util.HashMap;
import java.util.List;

public class LeaveRequestRecyclerAdapter extends RecyclerView.Adapter<LeaveRequestRecyclerAdapter.ViewHolder> {
	
	Context context;
	List<LeaveRequest> list;
	private static LeaveRequestRecyclerAdapter.OnItemClickListener listener;
	HashMap<String, Integer> header = new HashMap<>();
	
	public LeaveRequestRecyclerAdapter(Context context , List<LeaveRequest> list) {
		this.context = context;
		this.list = list;
	}
	
	@NonNull
	@Override
	public LeaveRequestRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
		RecyclerItemLeaveRequestBinding binding = RecyclerItemLeaveRequestBinding.inflate(LayoutInflater.from(context) , parent , false);
		return new ViewHolder(binding);
	}
	
	@Override
	public void onBindViewHolder(@NonNull LeaveRequestRecyclerAdapter.ViewHolder holder , int position) {
		
		LeaveRequest leaveRequest = list.get(position);
		
		if (header.containsKey(AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_MONTH_YEAR))) {
			if (header.get(AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_MONTH_YEAR)) == position) {
				holder.year.setText(AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_MONTH_YEAR));
				holder.year.setVisibility(View.VISIBLE);
			} else {
				holder.year.setText("");
				holder.year.setVisibility(View.GONE);
			}
		} else {
			header.put(AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_MONTH_YEAR) , position);
			holder.year.setText(AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_MONTH_YEAR));
			holder.year.setVisibility(View.VISIBLE);
		}
		String days = leaveRequest.getTotal_days()>=1?((int)leaveRequest.getTotal_days())+"":leaveRequest.getTotal_days()+"";
		String totaldays = days + " " + (leaveRequest.getTotal_days() > 1 ? context.getString(R.string.days) : context.getString(R.string.day));
		holder.date.setText(AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_DAY) + " - " + AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getTo_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_DAY) + " (" + totaldays + ")");
//		holder.date.setText(AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getFrom_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_DAY) + " - " + AppUtils.getConvertedDateFromOneFormatToOther(leaveRequest.getTo_date() , AppUtils.FORMAT19 , AppUtils.FORMAT_DAY));
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
			default:
				holder.image.setImageResource(R.drawable.casual_leaves);
				break;
		}
		switch (leaveRequest.getStatus()) {
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
	
	public interface OnItemClickListener {
		void OnItemClickListener(LeaveRequest leaveRequest);
	}
	
	public void setListener(LeaveRequestRecyclerAdapter.OnItemClickListener listener) {
		this.listener = listener;
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
