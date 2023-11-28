package com.lmkr.prmscemployee.data.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lmkr.prmscemployee.App;
import com.lmkr.prmscemployee.R;
import com.lmkr.prmscemployee.ui.activities.MainActivity;
import com.lmkr.prmscemployee.ui.activities.NotificationActivity;
import com.lmkr.prmscemployee.ui.utilities.AppWideWariables;
import com.lmkr.prmscemployee.ui.utilities.SharedPreferenceHelper;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
	private static final String TAG = "MyFirebaseMessagingService";
	
	private static MyFirebaseMessagingService INSTANCE;
	
	public static MyFirebaseMessagingService getInstance() {
		if(INSTANCE==null)
		{
			INSTANCE = new MyFirebaseMessagingService();
		}
		return INSTANCE;
	}
	
	/**
	 * Called when message is received.
	 *
	 * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
	 */
	// [START receive_message]
	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		// [START_EXCLUDE]
		// There are two types of messages data messages and notification messages. Data messages
		// are handled
		// here in onMessageReceived whether the app is in the foreground or background. Data
		// messages are the type
		// traditionally used with GCM. Notification messages are only received here in
		// onMessageReceived when the app
		// is in the foreground. When the app is in the background an automatically generated
		// notification is displayed.
		// When the user taps on the notification they are returned to the app. Messages
		// containing both notification
		// and data payloads are treated as notification messages. The Firebase console always
		// sends notification
		// messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
		// [END_EXCLUDE]
		
		// TODO(developer): Handle FCM messages here.
		// Not getting messages here? See why this may be: https://goo.gl/39bRNJ
		Log.d(TAG, "From: " + remoteMessage.getFrom());
		/*
		
		
		Intent dialogIntent = new Intent(this, NotificationActivity.class);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		dialogIntent.putExtra("msg", remoteMessage);
		startActivity(dialogIntent);
		
*/
		
		
		// Check if message contains a notification payload.
		if (remoteMessage.getNotification() != null) {
			Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
			String notificationTitle = remoteMessage.getNotification().getTitle();
			String notificationBody = remoteMessage.getNotification().getBody();
			if (remoteMessage.getNotification().getBody() != null) {
				try{
					sendNotification(notificationTitle,notificationBody);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		// Check if message contains a data payload.
		if (remoteMessage.getData().size() > 0) {
			
			Intent intentNotification = new Intent();
			intentNotification.setAction("com.from.notification");
			sendBroadcast(intentNotification);
			
			Log.d(TAG, "Message data payload: " + remoteMessage.getData());
			/*try{
				Intent dialogIntent = new Intent(MyFirebaseMessagingService.this , MainActivity.class);
				dialogIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				dialogIntent.putExtra(AppWideWariables.NOTIFICATION , true);
				startActivity(dialogIntent);
			}
			catch(Exception e) {
				Intent dialogIntent = new Intent(MyFirebaseMessagingService.this , MainActivity.class);
				dialogIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				dialogIntent.putExtra(AppWideWariables.NOTIFICATION , true);
				startActivity(dialogIntent);
			}*/
			
			/*
			if (true) { //Check if data needs to be processed by long running job
		// For long-running tasks (10 seconds or more) use WorkManager.
				scheduleJob();
			} else {
				// Handle message within 10 seconds
				handleNow();
			}
*/
		
		
		}
		
		
		
		// Also if you intend on generating your own notifications as a result of a received FCM
		// message, here is where that should be initiated. See sendNotification method below.
	
	}
	// [END receive_message]
	
	
	// [START on_new_token]
	/**
	 * There are two scenarios when onNewToken is called:
	 * 1) When a new token is generated on initial app startup
	 * 2) Whenever an existing token is changed
	 * Under #2, there are three scenarios when the existing token is changed:
	 * A) App is restored to a new device
	 * B) User uninstalls/reinstalls the app
	 * C) User clears app data
	 */
	@Override
	public void onNewToken(String token) {
		Log.d(TAG, "Refreshed token: " + token);
		
		// If you want to send messages to this application instance or
		// manage this apps subscriptions on the server side, send the
		// FCM registration token to your app server.
		SharedPreferenceHelper.saveString(SharedPreferenceHelper.FIRE_BASE_TOKEN , token,getApplicationContext());
		sendRegistrationToServer(token);
	}
	// [END on_new_token]
	
	
	/**
	 * Schedule async work using WorkManager.
	 */
	private void scheduleJob() {
		// [START dispatch_job]
		OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class).build();
		WorkManager.getInstance(this).beginWith(work).enqueue();
		// [END dispatch_job]
	}
	
	/**
	 * Handle time allotted to BroadcastReceivers.
	 */
	private void handleNow() {
		Log.d(TAG, "Short lived task is done.");
	}
	
	/**
	 * Persist token to third-party servers.
	 *
	 * Modify this method to associate the user's FCM registration token with any
	 * server-side account maintained by your application.
	 *
	 * @param token The new token.
	 */
	private void sendRegistrationToServer(String token) {
		// TODO: Implement this method to send token to your app server.
		FirebaseTokenListener.getInstance().setFirebaseUpdatedToken(token);
	}
	
	/**
	 * Create and show a simple notification containing the received FCM message.
	 *
	 * @param messageBody FCM message body received.
	 */
	

	private void sendNotification(String title,String messageBody) {
		
		int notifyId = (int) System.currentTimeMillis();
		Intent intent = new Intent(App.getInstance(), MainActivity.class);
		intent.putExtra(AppWideWariables.NOTIFICATION,true);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, notifyId /* Request code */, intent,
//				PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
				PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
		
		Spannable titleBold = new SpannableString(title);
		titleBold.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// ...
		
		String channelId = getString(R.string.default_notification_channel_id);
		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		AudioAttributes audioAttributes = new AudioAttributes.Builder()
				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
				.setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(this, channelId)
						.setSmallIcon(R.drawable.ic_stat_ic_notification_icon)
//						.setContentTitle(getString(R.string.fcm_message))
						.setContentTitle(titleBold)
						.setContentText(messageBody)
						.setSound(defaultSoundUri)
						.setDefaults(Notification.DEFAULT_ALL)
//						.setFullScreenIntent(pendingIntent, true)
						.setPriority(NotificationCompat.PRIORITY_MAX)
						.setContentIntent(pendingIntent);
		
		Notification notification = notificationBuilder.build();
		
		// Since android Oreo notification channel is needed.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(channelId,
					"Channel human readable title",
					NotificationManager.IMPORTANCE_HIGH);
			channel.enableVibration(true);
			channel.enableLights(true);
			channel.setLightColor(Color.GREEN);
			channel.enableVibration(true);
			channel.setSound(defaultSoundUri,audioAttributes);
			channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
			channel.setShowBadge(true);
			channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			if (notificationManager != null) {
				notificationManager.createNotificationChannel(channel);
				notificationManager.notify(notifyId, notification);
			}
		}
		else{
			NotificationManager mNotifyMgr =   (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			if (mNotifyMgr != null) {
				mNotifyMgr.notify(notifyId, notification);
			}
		}
//		notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
	}
	private void sendNotification(FirebaseObject firebaseObject, String body , RemoteMessage remoteMessage) {
		Intent intent = new Intent(this, NotificationActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(AppWideWariables.BUNDLE_TITLE_BODY,body);
		intent.putExtra("msg", remoteMessage);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
				PendingIntent.FLAG_IMMUTABLE);
		
		String channelId = getString(R.string.default_notification_channel_id);
		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(this, channelId)
						.setSmallIcon(R.drawable.ic_stat_ic_notification_icon)
						.setContentTitle("")
//						.setContentTitle(firebaseObject.getTitle())
//						.setContentTitle(getString(R.string.fcm_message))
						.setContentText("")
//						.setContentText(firebaseObject.getBody())
						.setAutoCancel(true)
						.setSound(defaultSoundUri)
						.setContentIntent(pendingIntent);
		
		NotificationManager notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		// Since android Oreo notification channel is needed.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(channelId,
					"Channel human readable title",
					NotificationManager.IMPORTANCE_DEFAULT);
			notificationManager.createNotificationChannel(channel);
		}
		
		notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
	}
}
