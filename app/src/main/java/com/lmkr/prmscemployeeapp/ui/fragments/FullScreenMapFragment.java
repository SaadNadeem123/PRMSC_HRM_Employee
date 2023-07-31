package com.lmkr.prmscemployeeapp.ui.fragments;

import static androidx.camera.core.impl.utils.ContextUtil.getApplicationContext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.maps.android.PolyUtil;
import com.lmkr.prmscemployeeapp.App;
import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.data.webservice.models.Locations;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;
import com.lmkr.prmscemployeeapp.ui.locationUtils.LocationService;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.List;

public class FullScreenMapFragment extends BaseDialogFragment implements OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {
    private static FullScreenMapFragment INSTANCE = null;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private UserData userdata = null;
    private TextView close;
    private TextView title;
    private Button proceed;
    private ImageView animationCross;
    private LottieAnimationView animation;
    private LinearLayout ll;


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

        String latitude = SharedPreferenceHelper.getString("lat", getActivity());
        String longitude = SharedPreferenceHelper.getString("long", getActivity());


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

        builder.include(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)));
        if (mMap != null) {
            mMap.clear();


//            Polyline polyline1 = googleMap.addPolyline(options);
            Polygon polygon = mMap.addPolygon(optionsPolygon);

            // Set listeners for click events.
            mMap.setOnPolylineClickListener(this);
            mMap.setOnPolygonClickListener(this);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.valueOf(latitude),Double.valueOf(longitude)))
                    .icon(AppUtils.BitmapFromVector(getActivity(),R.drawable.baseline_my_location_24))
                    .title(getString(R.string.current_location)));
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

        boolean isInGeofence = PolyUtil.containsLocation(Double.valueOf(latitude), Double.valueOf(longitude), optionsPolygon.getPoints(), true);
        if (isInGeofence) {
            animationCross.setVisibility(View.GONE);
            animation.cancelAnimation();
            animation.setVisibility(View.GONE);
            proceed.setEnabled(true);
            title.setText("");
            ll.setBackground(null);
        } else {
            animationCross.setVisibility(View.VISIBLE);
            animation.setVisibility(View.VISIBLE);
            animation.cancelAnimation();
            proceed.setEnabled(false);
            ll.setBackgroundColor(getResources().getColor(R.color.grey_overlay_dark, null));
            title.setText(getResources().getText(R.string.not_in_defined_area));
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
                destroy();
            }
        });

    }

    private void destroy() {
        INSTANCE = null;
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);

        dismissAllowingStateLoss();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                updateCurrentLocation();
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