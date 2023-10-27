package com.lmkr.prmscemployeeapp.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.ui.bulletin.BulletinDetailActivity;
import com.lmkr.prmscemployeeapp.data.webservice.models.BulletinModel;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;

import java.util.List;

public class BulletinRecyclerAdapter extends RecyclerView.Adapter<BulletinRecyclerAdapter.BulletinViewHolder> {

    private List<BulletinModel> bulletinList;
    private Context context;

    public BulletinRecyclerAdapter(List<BulletinModel> bulletinList, Context context) {

        this.bulletinList = bulletinList;
        this.context = context;
    }

    @NonNull
    @Override
    public BulletinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bulletin, parent, false);
        return new BulletinViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BulletinViewHolder holder, int position) {

        BulletinModel bulletin = bulletinList.get(position);
        holder.titleBulletin.setText(bulletin.getTitle());
        holder.descriptionBulletin.setText(bulletin.getDescription());
        holder.dateBulletin.setText(AppUtils.formatDate(bulletin.getDate()));
//        holder.dateBulletin.setText(AppUtils.printCountDownDifference(context,bulletin.getDate(),AppUtils.getCurrentDateTimeGMT5String()));
        holder.dateBulletin.setText(AppUtils.getDifferenceBetweenDates(context,bulletin.getDate(),AppUtils.getCurrentDateTimeString()));

        holder.cardView.setOnClickListener(v -> {

            Intent intent = new Intent(context, BulletinDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("title", bulletin.getTitle());
            bundle.putString("description", bulletin.getDescription());
            bundle.putString("date", bulletin.getDate());

            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bulletinList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setBulletinList(List<BulletinModel> bulletinList) {
        this.bulletinList = bulletinList;
        notifyDataSetChanged();
    }

    static class BulletinViewHolder extends RecyclerView.ViewHolder {
        private TextView titleBulletin,descriptionBulletin,dateBulletin;
        private CardView cardView;


        public BulletinViewHolder(@NonNull View itemView) {
            super(itemView);
            titleBulletin = itemView.findViewById(R.id.titleBulletin);
            descriptionBulletin = itemView.findViewById(R.id.descriptionBulletin);
            dateBulletin = itemView.findViewById(R.id.dateBulletin);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
