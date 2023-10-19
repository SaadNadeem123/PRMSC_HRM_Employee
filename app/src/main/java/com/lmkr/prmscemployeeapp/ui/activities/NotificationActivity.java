package com.lmkr.prmscemployeeapp.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.RemoteMessage;
import com.lmkr.prmscemployeeapp.R;

public class NotificationActivity extends AppCompatActivity {
	
	private Activity context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		Bundle extras = getIntent().getExtras();
		
		if (extras == null) {
			context.finish();
			return;
		}
		
		RemoteMessage msg = (RemoteMessage) extras.get("msg");
		
		if (msg == null) {
			context.finish();
			return;
		}
		
		RemoteMessage.Notification notification = msg.getNotification();
		
		if (notification == null) {
			context.finish();
			return;
		}
		
		String dialogMessage;
		try {
			dialogMessage = notification.getBody();
		} catch (Exception e) {
			context.finish();
			return;
		}
		String dialogTitle = notification.getTitle();
		if (dialogTitle == null || dialogTitle.length() == 0) {
			dialogTitle = "";
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle(dialogTitle)
				.setMessage(dialogMessage)
				.setCancelable(false)
				.setPositiveButton("Yes" , new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog , int id) {
						Intent dialogIntent = new Intent(NotificationActivity.this, MainActivity.class);
						dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(dialogIntent);
						finish();
					}
				})
				.setNegativeButton("No" , new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog , int id) {
						//  Action for 'NO' Button
						dialog.cancel();
					}
				});
		//Creating dialog box
		AlertDialog alert = builder.create();
		//Setting the title manually
		alert.setTitle("Notification");
		alert.show();
		
	}
	
}