package com.lmkr.prmscemployeeapp.ui.adapter;


import android.app.DownloadManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.data.database.models.FileModel;
import com.lmkr.prmscemployeeapp.ui.activities.MainActivity;
import com.lmkr.prmscemployeeapp.ui.leaverequest.LeaveRequestFragment;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.PermissionsRequest;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AttachmentsRecyclerAdapter extends RecyclerView.Adapter<AttachmentsRecyclerAdapter.ViewHolder> {
    List<FileModel> file;
    LeaveRequestFragment context;
    boolean fromAttachment;
    private DownloadManager manager;

    public AttachmentsRecyclerAdapter(List<FileModel> fileModels, LeaveRequestFragment context, boolean fromAttachment) {
        this.file = fileModels;
        this.context = context;
    }

    public boolean isFromAttachment() {
        return fromAttachment;
    }

    public void setFromAttachment(boolean fromAttachment) {
        this.fromAttachment = fromAttachment;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_attachment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        FileModel fileModel = file.get(position);

        if (fromAttachment) {
            holder.deleteAttachment.setVisibility(View.VISIBLE);
        } else {
            holder.deleteAttachment.setVisibility(View.GONE);
        }

        holder.name.setText(fileModel.getFileName());
        /*holder.viewAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromAttachment) {
                    AppUtils.openFile(context, fileModel.getFileName(), fileModel.getMimeType(), fileModel.getPath());
                } else {
                    if (fileModel.getType() != null && !TextUtils.isEmpty(fileModel.getType())) {
                        if (fileModel.getType().equalsIgnoreCase(AppWideWariables.FILE_TYPE_SERVER_IMAGE)) {
                            ((BookingDetailsActivity) context).viewImage(fileModel);

                            *//*StfalconImageViewer.Builder<Image>(context,images){
                            view, image ->
                                        Picasso.get().load(image.url).into(view)
                            }.show();*//*
                        } else {
                            if (PermissionsRequest.checkPermission(context)) {
                                downloadFile(context, fileModel);
                            }
                        }
                    }
                }
            }
        });
*/
        holder.deleteAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LeaveRequestFragment) context).deleteAttachment(fileModel);
            }
        });

    }


    @Override
    public int getItemCount() {
        return file.size();
    }

    public void addItems(List<FileModel> files) {
        file.addAll(files);
        notifyDataSetChanged();
    }

    public void addItem(FileModel files) {
        file.add(files);
        notifyDataSetChanged();
    }

    public void removeItems(List<FileModel> files) {
        file.removeAll(files);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageButton viewAttachment, deleteAttachment;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            viewAttachment = itemView.findViewById(R.id.viewAttachment);
            deleteAttachment = itemView.findViewById(R.id.deleteAttachment);

        }
    }
}
