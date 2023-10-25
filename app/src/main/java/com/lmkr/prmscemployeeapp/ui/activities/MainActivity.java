package com.lmkr.prmscemployeeapp.ui.activities;

import static com.lmkr.prmscemployeeapp.ui.locationUtils.LocationAlertDialogFragment.LOACTION_ENABLE_REQUEST;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.data.webservice.TokenBoundService;
import com.lmkr.prmscemployeeapp.data.webservice.api.ApiCalls;
import com.lmkr.prmscemployeeapp.data.webservice.api.ApiManager;
import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
import com.lmkr.prmscemployeeapp.data.webservice.models.ApiBaseResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;
import com.lmkr.prmscemployeeapp.databinding.ActivityMainBinding;
import com.lmkr.prmscemployeeapp.ui.bulletin.BulletinFragment;
import com.lmkr.prmscemployeeapp.ui.home.HomeFragment;
import com.lmkr.prmscemployeeapp.ui.leaverequest.LeaveRequestFragment;
import com.lmkr.prmscemployeeapp.ui.locationUtils.LocationService;
import com.lmkr.prmscemployeeapp.ui.myinfo.MyInfoFragment;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.AppWideWariables;
import com.lmkr.prmscemployeeapp.ui.utilities.PermissionsRequest;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends BaseActivity {
	
	private static final int REQUEST_LOCATION = 1;
	String latitude = null, longitude = null;
	LocationManager locationManager;
	private ActivityMainBinding binding;
	private String provider;
	private boolean mServiceBound = false;
	private boolean mServiceBoundToken = false;
	private LocationService mBoundService;
	private TokenBoundService mBoundServiceToken;
	private final ServiceConnection mServiceConnectionToken = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mServiceBoundToken = false;
		}
		
		@Override
		public void onServiceConnected(ComponentName name , IBinder service) {
			TokenBoundService.MyBinder myBinder = (TokenBoundService.MyBinder) service;
			mBoundServiceToken = myBinder.getService();
			mServiceBoundToken = true;
		}
	};
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mServiceBound = false;
		}
		
		@Override
		public void onServiceConnected(ComponentName name , IBinder service) {
			LocationService.MyBinder myBinder = (LocationService.MyBinder) service;
			mBoundService = myBinder.getService();
			mServiceBound = true;
			if (mBoundService != null) {
				if (!AppUtils.canGetLocation(MainActivity.this)) {
					AppUtils.showLocationSettingsAlert(MainActivity.this);
				}
			}
			
		}
	};
	private ProgressBar loadingProgressBar;
	private NavController navController = null;
	private NavHostFragment navHostFragment;
	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context , Intent intent) {
			Log.i("INTENT RECIEVED" , " = INTENT REC IEVED");
			if (intent.hasExtra(LocationService.MESSAGE)) {
				String s = intent.getStringExtra(LocationService.MESSAGE);
				if (s == "1") {
					latitude = SharedPreferenceHelper.getString("lat" , MainActivity.this);
					longitude = SharedPreferenceHelper.getString("long" , MainActivity.this);

//                    AppUtils.makeNotification("Lat -> " + latitude + " , Long -> " + longitude, MainActivity.this);
//                    AppUtils.hideNotification(MainActivity.this);
					try {
						if (navHostFragment.getChildFragmentManager() != null && navHostFragment.getChildFragmentManager().getFragments() != null && navHostFragment.getChildFragmentManager().getFragments().size() > 0 && navHostFragment.getChildFragmentManager().getFragments().get(0) instanceof HomeFragment) {
							((HomeFragment) navHostFragment.getChildFragmentManager().getFragments().get(0)).enableCheckinButton(true);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

//                    unBindService();
				}
			}
		}
		// do something here.
	};
	private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context , Intent intent) {
			if (navHostFragment.getChildFragmentManager() != null && navHostFragment.getChildFragmentManager().getFragments() != null && navHostFragment.getChildFragmentManager().getFragments().size() > 0 && navHostFragment.getChildFragmentManager().getFragments().get(0) instanceof HomeFragment) {
				((HomeFragment) navHostFragment.getChildFragmentManager().getFragments().get(0)).refreshApiCalls();
			} else if (navHostFragment.getChildFragmentManager() != null && navHostFragment.getChildFragmentManager().getFragments() != null && navHostFragment.getChildFragmentManager().getFragments().size() > 0 && navHostFragment.getChildFragmentManager().getFragments().get(0) instanceof LeaveRequestFragment) {
				((LeaveRequestFragment) navHostFragment.getChildFragmentManager().getFragments().get(0)).refreshApiCalls();
			} else if (navHostFragment.getChildFragmentManager() != null && navHostFragment.getChildFragmentManager().getFragments() != null && navHostFragment.getChildFragmentManager().getFragments().size() > 0 && navHostFragment.getChildFragmentManager().getFragments().get(0) instanceof BulletinFragment) {
				((BulletinFragment) navHostFragment.getChildFragmentManager().getFragments().get(0)).refreshApiCalls();
			} /*else if (navHostFragment.getChildFragmentManager() != null && navHostFragment.getChildFragmentManager().getFragments() != null && navHostFragment.getChildFragmentManager().getFragments().size() > 0 && navHostFragment.getChildFragmentManager().getFragments().get(0) instanceof MyInfoFragment) {
                ((MyInfoFragment) navHostFragment.getChildFragmentManager().getFragments().get(0)).refreshApiCalls();
            }*/
		}
	};
	
	public void unBindService() {
		try {
			if (mServiceConnection != null) {
				unbindService(mServiceConnection);
				mBoundService.stopSelf();
				mServiceConnection = null;
				mServiceBound = false;
				Log.i("Service UnBinded" , " true");
			} else {
				Log.i("Service UnBinded" , " already");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean bindService() {
		boolean isBinded = false;
		try {
			if (!mServiceBound) {
				Intent intent = new Intent(this , LocationService.class);
				startService(intent);
				bindService(intent , mServiceConnection , Context.BIND_AUTO_CREATE);
				
				LocalBroadcastManager.getInstance(this).registerReceiver((receiver) , new IntentFilter(LocationService.SERVICE_NAME));
				
				Log.i("Service Binded" , " true");
//                AppUtils.makeNotification(getResources().getString(R.string.wait_fetching_location), MainActivity.this);
				isBinded = true;
			} else {
				Log.i("Service Binded" , " already");
				isBinded = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isBinded;
	}
	
	private void bindTokenService() {
		try {
			Intent intentToken = new Intent(this , TokenBoundService.class);
			startService(intentToken);
			bindService(intentToken , mServiceConnectionToken , Context.BIND_AUTO_CREATE);
			IntentFilter intentFilter = new IntentFilter(TokenBoundService.REFRESH_TOKEN);
			registerReceiver(messageReceiver , intentFilter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		Log.i("RESUME = " , "RESUME");
		if (!AppUtils.canGetLocation(MainActivity.this)) {
			AppUtils.showLocationSettingsAlert(MainActivity.this);
		}
		
		bindTokenService();
		
		refreshApiCalls();
	}
	
	
	@Override
	public void callApi() {
		
		if (!AppUtils.checkNetworkState(MainActivity.this)) {
			return;
		}
		
		JsonObject body = new JsonObject();
		body.addProperty("email" , SharedPreferenceHelper.getLoggedinUser(MainActivity.this).getBasicData().get(0).getEmail());
		body.addProperty("password" , SharedPreferenceHelper.getString(SharedPreferenceHelper.PASSWORD , MainActivity.this));  //3132446990
		body.addProperty("source" , AppWideWariables.SOURCE_MOBILE);  //3132446990
		
		SharedPreferenceHelper.saveString(SharedPreferenceHelper.PASSWORD , SharedPreferenceHelper.getString(SharedPreferenceHelper.PASSWORD , MainActivity.this) , MainActivity.this);
		Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
		Urls urls = retrofit.create(Urls.class);
		
		Call<UserData> call = urls.loginUserApi(body);
		call.enqueue(new Callback<UserData>() {
			@Override
			public void onResponse(Call<UserData> call , Response<UserData> response) {
				Log.i("response" , response.toString());
				
				if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_GET , response , MainActivity.this)) {
					if (!response.isSuccessful()) {
//                    tv.setText("Code :" + response.code());
						return;
					}
					
					if (response.body() != null && response.body().getMessage() == null) {
						if (response.body().getBasicData() != null && response.body().getBasicData().size() > 0 && response.body().getBasicData().get(0).getApplication_access().equalsIgnoreCase("yes")) {
							SharedPreferenceHelper.setLoggedinUser(getApplicationContext() , response.body());
						} else {
							AppUtils.makeNotification(getString(R.string.app_access_denied) , MainActivity.this);
						}
					}

//                    refreshApiCalls();
				}
			}
			
			@Override
			public void onFailure(Call<UserData> call , Throwable t) {
				t.printStackTrace();
				AppUtils.ApiError(t , MainActivity.this);
//                AppUtils.makeNotification(t.toString(), MainActivity.this);
			}
		});
		
	}
	
	public void refreshApiCalls() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (navHostFragment.getChildFragmentManager() != null && navHostFragment.getChildFragmentManager().getFragments() != null && navHostFragment.getChildFragmentManager().getFragments().size() > 0 && navHostFragment.getChildFragmentManager().getFragments().get(0) instanceof HomeFragment) {
					((HomeFragment) navHostFragment.getChildFragmentManager().getFragments().get(0)).refreshApiCalls();
				} else if (navHostFragment.getChildFragmentManager() != null && navHostFragment.getChildFragmentManager().getFragments() != null && navHostFragment.getChildFragmentManager().getFragments().size() > 0 && navHostFragment.getChildFragmentManager().getFragments().get(0) instanceof LeaveRequestFragment) {
					((LeaveRequestFragment) navHostFragment.getChildFragmentManager().getFragments().get(0)).refreshApiCalls();
				} else if (navHostFragment.getChildFragmentManager() != null && navHostFragment.getChildFragmentManager().getFragments() != null && navHostFragment.getChildFragmentManager().getFragments().size() > 0 && navHostFragment.getChildFragmentManager().getFragments().get(0) instanceof BulletinFragment) {
					((BulletinFragment) navHostFragment.getChildFragmentManager().getFragments().get(0)).refreshApiCalls();
				} else if (navHostFragment.getChildFragmentManager() != null && navHostFragment.getChildFragmentManager().getFragments() != null && navHostFragment.getChildFragmentManager().getFragments().size() > 0 && navHostFragment.getChildFragmentManager().getFragments().get(0) instanceof MyInfoFragment) {
				
				}
			}
		} , 1000);
	}
	
	@Override
	protected void onActivityResult(int requestCode , int resultCode , @Nullable Intent data) {
		super.onActivityResult(requestCode , resultCode , data);
		
		if (requestCode == LOACTION_ENABLE_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				if (navHostFragment.getChildFragmentManager() != null && navHostFragment.getChildFragmentManager().getFragments() != null && navHostFragment.getChildFragmentManager().getFragments().size() > 0 && navHostFragment.getChildFragmentManager().getFragments().get(0) instanceof HomeFragment) {
					((HomeFragment) navHostFragment.getChildFragmentManager().getFragments().get(0)).enableCheckinButton(true);
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				//Write your code if there's no result
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		unBindService();
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		unbindServiceToken();
		super.onPause();
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode , permissions , grantResults);
		
		if (requestCode == PermissionsRequest.LOCATION_REQUEST_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				bindService();
			}
		}
		
	}
	
	private void callApiUpdateFirebaseToken(String token) {
		UserData user = SharedPreferenceHelper.getLoggedinUser(MainActivity.this);
		
		ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this , R.style.CustomProgressDialog);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setMessage("Please Wait...");
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		
		JsonObject body = new JsonObject();
		body.addProperty("device_name" , Build.MANUFACTURER + " " + Build.MODEL);
		body.addProperty("device_token" , token);
//		body.addProperty("device_token" , SharedPreferenceHelper.getString(SharedPreferenceHelper.FIRE_BASE_TOKEN , MainActivity.this));
		
		Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
		Urls urls = retrofit.create(Urls.class);
		
		Urls jsonPlaceHolderApi = retrofit.create(Urls.class);
		
		Call<ApiBaseResponse> call = jsonPlaceHolderApi.updateFirebaseToken(AppUtils.getStandardHeaders(user) , body);
		
		call.enqueue(new Callback<ApiBaseResponse>() {
			@Override
			public void onResponse(Call<ApiBaseResponse> call , Response<ApiBaseResponse> response) {
				Log.i("response" , response.toString());
				if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_POST , response , MainActivity.this)) {
					
					if (!response.isSuccessful()) {
						//tv.setText("Code :" + response.code());
						return;
					}
					
				}
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						
						if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
					}
				} , 2000);
			}
			
			@Override
			public void onFailure(Call<ApiBaseResponse> call , Throwable t) {
				t.printStackTrace();
				if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
				Log.i("response" , t.toString());
				AppUtils.ApiError(t , MainActivity.this);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						
						if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
					}
				} , 2000);
			}
		});
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (ActivityCompat.checkSelfPermission(this , android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this , android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this , PermissionsRequest.LOCATION_PERMISSIONS , PermissionsRequest.LOCATION_REQUEST_CODE);
		} else {
			bindService();
		}
		
		UpdateFirebaseToken();
		
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		BottomNavigationView navView = findViewById(R.id.nav_view);
		// Passing each menu ID as a set of Ids because each
		// menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_timeoff, R.id.navigation_bulletin, R.id.navigation_myinfo)
//                .build();
		navController = Navigation.findNavController(this , R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
		NavigationUI.setupWithNavController(binding.navView , navController);
		
		navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
		
		navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
			@Override
			public void onDestinationChanged(@NonNull NavController navController , @NonNull NavDestination navDestination , @Nullable Bundle bundle) {
				ApiManager.getInstance().getToken();
			}
		});
		
	}
	
	private void UpdateFirebaseToken() {
		
		FirebaseMessaging.getInstance().subscribeToTopic("PRMSC");
		/*FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
			@Override
			public void onComplete(@NonNull Task<String> task) {
				if(!task.isSuccessful()){
					return;
				}
				// Get new Instance ID token
				String token = task.getResult();
				callApiUpdateFirebaseToken(token);
			}
		});*/
		
/*
		FirebaseTokenListener.getInstance().getFirebaseUpdatedToken().observe(MainActivity.this, new Observer<String>() {
			@Override
			public void onChanged(String token) {
				if(token!=null && !token.isEmpty())
				{
					callApiUpdateFirebaseToken(token);
				}
			}
		});
*/
	
	}
	
	
	private void unbindServiceToken() {
		
		try {
			if (mServiceBoundToken) {
				unbindService(mServiceConnectionToken);
				mServiceBoundToken = false;
			}
			Intent intent = new Intent(MainActivity.this , TokenBoundService.class);
			stopService(intent);
			
			unregisterReceiver(messageReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.getExtras().getBoolean(AppWideWariables.NOTIFICATION)) {
			if (navHostFragment.getChildFragmentManager() != null && navHostFragment.getChildFragmentManager().getFragments() != null && navHostFragment.getChildFragmentManager().getFragments().size() > 0) {
				if (navHostFragment.getChildFragmentManager().getFragments().get(0) instanceof BulletinFragment) {
					refreshApiCalls();
				} else {
					binding.navView.setSelectedItemId(binding.navView.getMenu().getItem(2).getItemId());
				}
			}
		}
		else {
			refreshApiCalls();
		}
	}
	
	@Override
	public void initializeViews() {
	
	}
	
	@Override
	public void setListeners() {
	
	}
	
	@Override
	public void handleIntent() {
	
	}
	
	@Override
	public void internetConnectionChangeListener(boolean isConnected) {
	
	}
	
}