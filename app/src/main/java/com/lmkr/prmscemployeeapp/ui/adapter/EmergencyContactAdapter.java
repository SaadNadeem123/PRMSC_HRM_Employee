package com.lmkr.prmscemployeeapp.ui.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmkr.prmscemployeeapp.App;
import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.data.webservice.models.EmergencyContact;
import com.lmkr.prmscemployeeapp.ui.myinfo.addContact.AddContactActivity;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;

import java.util.List;

public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.ViewHolder> {

    private List<EmergencyContact> emergencyContacts;
    private Context context;

    public EmergencyContactAdapter(List<EmergencyContact> emergencyContacts,Context context) {

//        emergencyContacts.sort((contact1, contact2) -> Integer.compare(contact1.getId(), contact2.getId()));

        this.emergencyContacts = emergencyContacts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emergency_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmergencyContact emergencyContact = emergencyContacts.get(position);
        holder.bindData(emergencyContact);
        holder.editForm.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putString("Name",emergencyContact.getName());
            bundle.putString("Email",emergencyContact.getEmail());
            bundle.putString("Relation",emergencyContact.getRelation());
            bundle.putString("Mobile",emergencyContact.getMobile());
            bundle.putString("Work",emergencyContact.getWork());
            bundle.putString("Home",emergencyContact.getHome());
            bundle.putString("Address",emergencyContact.getAddress());
            bundle.putInt("Id",emergencyContact.getId());
            bundle.putInt("staticId",100);

            Intent intent = new Intent(context, AddContactActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);


        });

        holder.copyButton.setOnClickListener(v -> {
            AppUtils.copyTextToClipboard(emergencyContact.getMobile(),context);
        });

        holder.messageButton.setOnClickListener(v -> {
            AppUtils.sendSMS(emergencyContact.getMobile(),context);
        });
    }

    @Override
    public int getItemCount() {
        return emergencyContacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView relativeName,relationship,
                workPhone,homePhone,
                mobilePhone,email,address,text1;

        private ImageView editForm,messageButton,copyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeName = itemView.findViewById(R.id.relativeName);
            relationship = itemView.findViewById(R.id.relationship);
            workPhone = itemView.findViewById(R.id.workPhone);
            homePhone = itemView.findViewById(R.id.homePhone);
            mobilePhone = itemView.findViewById(R.id.mobilePhone);
            email = itemView.findViewById(R.id.email);
            address = itemView.findViewById(R.id.address);
            text1 = itemView.findViewById(R.id.text1);
            editForm = itemView.findViewById(R.id.editForm);
            messageButton = itemView.findViewById(R.id.messageButton);
            copyButton = itemView.findViewById(R.id.copyButton);

        }

        public void bindData(EmergencyContact contact) {
            relativeName.setText(contact.getName());
            email.setText(contact.getEmail());
            relationship.setText(contact.getRelation());
            workPhone.setText(contact.getWork());
            homePhone.setText(contact.getHome());
            mobilePhone.setText(contact.getMobile());
            address.setText(contact.getAddress());
           /* if(contact.getId() == 1){
                text1.setVisibility(View.VISIBLE);
            }else {
                text1.setVisibility(View.GONE);
            }*/
        }
    }


}
