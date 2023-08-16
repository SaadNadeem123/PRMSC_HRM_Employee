package com.lmkr.prmscemployeeapp.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;
import com.lmkr.prmscemployeeapp.App;
import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.data.webservice.api.ApiCalls;
import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
import com.lmkr.prmscemployeeapp.data.webservice.models.ApiBaseResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.Locations;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;
import com.lmkr.prmscemployeeapp.ui.activities.CameraXActivity;
import com.lmkr.prmscemployeeapp.ui.activities.MainActivity;
import com.lmkr.prmscemployeeapp.ui.locationUtils.LocationService;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.AppWideWariables;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;
import com.lmkr.prmscemployeeapp.viewModel.AttendanceHistoryViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FullScreenMapFragment extends BaseDialogFragment implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener {
    private static FullScreenMapFragment INSTANCE = null;
    private final AttendanceHistoryViewModel attendanceHistoryViewModel = null;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private UserData userdata = null;
    private TextView close;
    private TextView title;
    private Button proceed;
    private ImageView animationCross;
    private LottieAnimationView animation;
    private LinearLayout ll;
    private String latitude = "", longitude = "";
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("INTENT RECIEVED", " = INTENT RECIEVED");
            if (intent.hasExtra(LocationService.MESSAGE)) {
                String s = intent.getStringExtra(LocationService.MESSAGE);
                if (s == "1") {

                    updateCurrentLocation();
                }
            }
        }
        // do something here.
    };

    public static FullScreenMapFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FullScreenMapFragment();
        }
        INSTANCE.userdata = SharedPreferenceHelper.getLoggedinUser(App.getInstance());
        return INSTANCE;
    }

    private void updateCurrentLocation() {

        try {
            latitude = SharedPreferenceHelper.getString("lat", getActivity());
            longitude = SharedPreferenceHelper.getString("long", getActivity());

            PolylineOptions options = new PolylineOptions();
            options.clickable(true);
            options.color(getResources().getColor(R.color.black));

            PolygonOptions optionsPolygon = new PolygonOptions();
            optionsPolygon.clickable(true);
            optionsPolygon.fillColor(getResources().getColor(R.color.app_green_overlay));
            optionsPolygon.strokeColor(getResources().getColor(R.color.black));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (Locations location : userdata.getLatLngArray()) {
                builder.include(new LatLng(location.getLatitude(), location.getLongitude()));
                options.add(new LatLng(location.getLatitude(), location.getLongitude()));
                optionsPolygon.add(new LatLng(location.getLatitude(), location.getLongitude()));
            }


            if (mMap != null) {
                mMap.clear();


//            Polyline polyline1 = googleMap.addPolyline(options);
                Polygon polygon = mMap.addPolygon(optionsPolygon);

                // Set listeners for click events.
                mMap.setOnPolylineClickListener(this);
                mMap.setOnPolygonClickListener(this);
                if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                    builder.include(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude))).icon(AppUtils.BitmapFromVector(getActivity(), R.drawable.baseline_my_location_24)).title(getString(R.string.current_location)));
                }
                try {
                    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            try {
                                LatLngBounds bounds = builder.build();
                                int padding = 100; // offset from edges of the map in pixels
                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                                mMap.moveCamera(cu);
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            boolean isInGeofence = false;
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                isInGeofence = PolyUtil.containsLocation(Double.valueOf(latitude), Double.valueOf(longitude), optionsPolygon.getPoints(), true);
            }

            if (isInGeofence) {
                animationCross.setVisibility(View.GONE);
                animation.cancelAnimation();
                animation.setVisibility(View.GONE);
                proceed.setEnabled(true);
                title.setText("");
            } else {
                animationCross.setVisibility(View.VISIBLE);
                animation.setVisibility(View.VISIBLE);
                animation.cancelAnimation();
                proceed.setEnabled(false);
                title.setText(getResources().getText(R.string.not_in_defined_area));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initializeViews(View view) {


        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        close = view.findViewById(R.id.btn_close);
        title = view.findViewById(R.id.title);
        proceed = view.findViewById(R.id.proceed);
        animationCross = view.findViewById(R.id.animationCross);
        animation = view.findViewById(R.id.animation);
        ll = view.findViewById(R.id.ll);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiver), new IntentFilter(LocationService.SERVICE_NAME));

        mapFragment.getMapAsync(this);
        setListeners();
    }

    @Override
    public void setListeners() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceHelper.saveString(AppWideWariables.CHECKIN_LAT, "", getActivity());
                SharedPreferenceHelper.saveString(AppWideWariables.CHECKIN_LONG, "", getActivity());
                destroy();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferenceHelper.saveString(AppWideWariables.CHECKIN_LAT, SharedPreferenceHelper.getString("lat", getActivity()), getActivity());
                SharedPreferenceHelper.saveString(AppWideWariables.CHECKIN_LONG, SharedPreferenceHelper.getString("long", getActivity()), getActivity());

                if (userdata.getBasicData().get(0).getFacelock().equals("yes")) {
                    getActivity().startActivity(new Intent(getActivity(), CameraXActivity.class));
                } else {
                    SharedPreferenceHelper.saveBoolean(AppWideWariables.IS_IN_GEOFENCE, true, getActivity());
                    ((MainActivity) getActivity()).refreshApiCalls();
//                    callCheckInApi();
                }
                dismiss();
            }
        });

    }


    private void callCheckInApi() {

        if (!AppUtils.checkNetworkState(getActivity())) {
            return;
        }


        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        Urls urls = retrofit.create(Urls.class);

        JsonObject body = new JsonObject();

        body.addProperty("employee_id", userdata.getBasicData().get(0).getId());
        body.addProperty("checkin_time", AppUtils.getCurrentDateTimeGMT5String());
        body.addProperty("lat", latitude);
        body.addProperty("longitude", longitude);
        body.addProperty("source", AppWideWariables.SOURCE_MOBILE);
        body.addProperty("file_name", "");
        body.addProperty("file_path", "");


//        File file;// = // initialize file here

//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));


        Call<ApiBaseResponse> call = urls.checkIn(AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity())), body);

        call.enqueue(new Callback<ApiBaseResponse>() {
            @Override
            public void onResponse(Call<ApiBaseResponse> call, Response<ApiBaseResponse> response) {
                Log.i("response", response.toString());
                if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_POST, response, getActivity())) {
                    if (!response.isSuccessful()) {
//                    tv.setText("Code :" + response.code());
                        return;
                    }

                    AppUtils.makeNotification(response.body().getMessage(), getActivity());

//                SharedPreferenceHelper.saveBoolean(SharedPreferenceHelper.IS_CHECKED_IN, true, getActivity());}
                }
            }

            @Override
            public void onFailure(Call<ApiBaseResponse> call, Throwable t) {
                t.printStackTrace();
                AppUtils.ApiError(t, getActivity());
//                AppUtils.makeNotification(t.toString(), getActivity());
                Log.i("response", t.toString());
//                tv.setText(t.getMessage());
            }
        });

    }

    private void callCheckOutApi() {
        if (!AppUtils.checkNetworkState(getActivity())) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        Urls urls = retrofit.create(Urls.class);

        JsonObject body = new JsonObject();

        UserData userData = SharedPreferenceHelper.getLoggedinUser(getActivity());

        body.addProperty("checkout_time", AppUtils.getCurrentDateTimeGMT5String());

        Call<ApiBaseResponse> call = urls.checkout(AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity())), String.valueOf(userData.getBasicData().get(0).getId()), body);

        call.enqueue(new Callback<ApiBaseResponse>() {
            @Override
            public void onResponse(Call<ApiBaseResponse> call, Response<ApiBaseResponse> response) {
                Log.i("response", response.toString());

                if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_POST, response, getActivity())) {
                    if (!response.isSuccessful()) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiBaseResponse> call, Throwable t) {
                t.printStackTrace();
                AppUtils.ApiError(t, getActivity());
//                AppUtils.makeNotification(t.toString(), getActivity());
                Log.i("response", t.toString());
            }
        });
    }

    private void destroy() {
        INSTANCE = null;
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);

        dismissAllowingStateLoss();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fullscreen_map_view, container, false);
        initializeViews(view);

        return view;
    }


    private void zoomMapToPakistan() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mMap != null) {

                    //Pakistan is situated between latitude 24 and 35 degrees North and longitude 62 and 75 degrees East
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();

                    builder.include(new LatLng(24, 62));
                    builder.include(new LatLng(24, 75));
                    builder.include(new LatLng(38, 62));
                    builder.include(new LatLng(38, 75));

                    LatLngBounds bounds = builder.build();

                    int width = getResources().getDisplayMetrics().widthPixels;
                    int height = getResources().getDisplayMetrics().heightPixels;
//                    int height = (int) AppUtils.dp2px(getResources(), 240f);
//                    int padding = 0; // offset from edges of the map in pixels
                    int padding = (int) (width * 0.03); // offset from edges of the map 12% of screen

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, 0);


                    mMap.moveCamera(cu);
                }

            }
        }, 1000);

    }

    public void updateMap() {

        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);

//        zoomMapToPakistan();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    updateCurrentLocation();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 3000);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        updateMap();
    }

    @Override
    public void onPolygonClick(@NonNull Polygon polygon) {

    }

    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {

    }
}