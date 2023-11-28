package com.lmkr.prmscemployee.ui.utilities;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lmkr.prmscemployee.R;

import java.io.File;

public class AlertDialogUtils {
	
	public static final int ALERT_DIALOG_SEND_V_CODE = 0;
	private static AlertDialog.Builder builder = null;
	private static AlertDialog myDialog = null;
	
	
	public static void showPopUpMessageDialog(Context context , String title , String text , final AlertDialogResponseListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		View alertView = inflater.inflate(R.layout.layout_alert_dialog , null);
		((TextView) alertView.findViewById(R.id.title)).setText(title);
		((TextView) alertView.findViewById(R.id.confirmation_msg)).setText(text);
		if (TextUtils.isEmpty(title)) {
			alertView.findViewById(R.id.title).setVisibility(View.GONE);
		}
		if (TextUtils.isEmpty(text)) {
			alertView.findViewById(R.id.confirmation_msg).setVisibility(View.GONE);
		}
		((Button) alertView.findViewById(R.id.yes)).setText(context.getResources().getText(R.string.ok));
		((Button) alertView.findViewById(R.id.no)).setText(context.getResources().getText(R.string.cancel));
		//AppUtils.setMontserratBold(alertView.findViewById(R.id.title));
		//AppUtils.setMontserrat(alertView.findViewById(R.id.confirmation_msg));
		//AppUtils.setMontserrat(alertView.findViewById(R.id.yes));
		//AppUtils.setMontserrat(alertView.findViewById(R.id.no));
		
		alertView.findViewById(R.id.no).setVisibility(View.GONE);
		builder.setView(alertView);
		
		builder.setCancelable(false);
		final AlertDialog myDialog = builder.create();
		myDialog.setCancelable(false);
		
		alertView.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.OnSuccess();
				}
				myDialog.dismiss();
			}
		});
		
		alertView.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.OnCancel();
				}
				myDialog.cancel();
			}
		});
		
		myDialog.show();
	}
	
	public static void showPopUpMessageDialog(Context context , String path) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		View alertView = inflater.inflate(R.layout.layout_alert_dialog_image , null);
		((Button) alertView.findViewById(R.id.yes)).setText(context.getResources().getText(R.string.ok));
		((Button) alertView.findViewById(R.id.no)).setText(context.getResources().getText(R.string.cancel));
		
		alertView.findViewById(R.id.no).setVisibility(View.GONE);
		
//		File imgFile = new File("/storage/emulated/0/DCIM/Camera/IMG_20151102_193132.jpg");
		File imgFile = new File(path);
		if(imgFile.exists()){
			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			((ImageView)alertView.findViewById(R.id.top)).setImageBitmap(myBitmap);
		};
		
		builder.setView(alertView);
		
		builder.setCancelable(false);
		final AlertDialog myDialog = builder.create();
		myDialog.setCancelable(false);
		
		alertView.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myDialog.dismiss();
			}
		});
		
		alertView.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myDialog.cancel();
			}
		});
		
		myDialog.show();
	}
	
	public interface AlertDialogResponseListener {
		
		void OnSuccess();
		
		void OnCancel();
		
	}
	
}
