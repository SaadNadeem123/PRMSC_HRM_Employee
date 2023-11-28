package com.lmkr.prmscemployee.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lmkr.prmscemployee.R;
import com.lmkr.prmscemployee.data.webservice.models.LeaveCount;
import com.lmkr.prmscemployee.ui.utilities.AppUtils;

import java.util.List;

public class LeaveTypeSpinnerAdapter extends BaseAdapter {

    List<LeaveCount> leaveCounts;
    Context context;

    public LeaveTypeSpinnerAdapter(List<LeaveCount> leaveCounts, Context context) {
        this.leaveCounts = leaveCounts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return leaveCounts.size();
    }

    @Override
    public Object getItem(int i) {
        return leaveCounts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item_leave_type, null);

        TextView leaveType = convertView.findViewById(R.id.leave_type);
        leaveType.setText(leaveCounts.get(position).getType());

        return convertView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.dropdown_spinner_item_leave_type, null);

        TextView leaveType = convertView.findViewById(R.id.leave_type);
        if(leaveCounts.get(position).getId()==-1||leaveCounts.get(position).getType().equals(context.getResources().getString(R.string.select)))
        {
            leaveType.setText(leaveCounts.get(position).getType());
        }
        else
        {
            leaveType.setText(leaveCounts.get(position).getType() + " ( " + AppUtils.getFloatOrInteger(leaveCounts.get(position).getRemaining()) + " ) ");
        }
        return convertView;
    }
}
