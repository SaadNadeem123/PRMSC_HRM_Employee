package com.lmkr.prmscemployee.ui.utilities.networkUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class NetworkMonitor extends ConnectivityManager.NetworkCallback {

    private static final String TAG = "NetworkMonitoringUtil";
    private static NetworkMonitor Instance = null;
    private final NetworkRequest mNetworkRequest;
    private final ConnectivityManager mConnectivityManager;
    private final Context context;
    private WifiManager wifiManager;
    private NetworkInfo networkInfo;

    // Constructor
    private NetworkMonitor(Context context) {
        mNetworkRequest = new NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).addTransportType(NetworkCapabilities.TRANSPORT_WIFI).addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).build();
//      mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        mConnectivityManager = context.getSystemService(ConnectivityManager.class);
        mConnectivityManager.requestNetwork(mNetworkRequest, NetworkMonitor.this);

        this.context = context;

        checkNetworkState();
        registerNetworkCallbackEvents();

    }

    public static void initialize(Context context) {
        if (Instance == null) {
            Instance = new NetworkMonitor(context);
        }
    }

    private void checkNetworkState() {
        try {
            networkInfo = mConnectivityManager.getActiveNetworkInfo();
            // Set the initial value for the live-data
            NetworkMonitorListener.getInstance().setNetworkConnectivityStatus(networkInfo != null && networkInfo.isConnected());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onAvailable(@NonNull Network n) {
        super.onAvailable(n);
        Log.d(TAG, "onAvailable() called: Connected to network");
        showNetworkState(n, null);
    }

    @Override
    public void onLosing(@NonNull Network network, int maxMsToLive) {
        super.onLosing(network, maxMsToLive);
        NetworkMonitorListener.getInstance().setNetworkConnectivityStatus(false);
        NetworkMonitorListener.getInstance().setNetworkConnectivityStatus("");
        NetworkMonitorListener.getInstance().setNetworkObject(null);
    }

    @Override
    public void onUnavailable() {
        super.onUnavailable();
        NetworkMonitorListener.getInstance().setNetworkConnectivityStatus(false);
        NetworkMonitorListener.getInstance().setNetworkConnectivityStatus("");
        NetworkMonitorListener.getInstance().setNetworkObject(null);
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        Log.e(TAG, "onLost() called: Lost network connection");
        NetworkMonitorListener.getInstance().setNetworkConnectivityStatus(false);
        NetworkMonitorListener.getInstance().setNetworkConnectivityStatus("");
        NetworkMonitorListener.getInstance().setNetworkObject(null);

    }


    @Override
    public void onCapabilitiesChanged(@NonNull Network n, @NonNull NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(n, networkCapabilities);
        showNetworkState(n, networkCapabilities);
    }

    /**
     * Registers the Network-Request callback
     * (Note: Register only once to prevent duplicate callbacks)
     */
    private void registerNetworkCallbackEvents() {
        Log.d(TAG, "registerNetworkCallbackEvents() called");
        mConnectivityManager.registerDefaultNetworkCallback(this);
        mConnectivityManager.registerNetworkCallback(mNetworkRequest, this);

    }

    private void showNetworkState(Network n, NetworkCapabilities networkCapabilities) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Network activeNetwork = connectivityManager.getActiveNetwork();
            NetworkCapabilities nC = networkCapabilities != null ? networkCapabilities : connectivityManager.getNetworkCapabilities(activeNetwork);


            if (activeNetwork!=null && nC != null && connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null) {
                int downSpeed = nC.getLinkDownstreamBandwidthKbps();
                int upSpeed = nC.getLinkUpstreamBandwidthKbps();
                String speedUnit = " Kbps";

                if (nC.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    if (connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI) {
                        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                        WifiInfo info = wifiManager.getConnectionInfo();

                        NetworkInfo netInfo = connectivityManager.getNetworkInfo(n);
                        String networkType;
                        if (netInfo.getTypeName().equalsIgnoreCase("WIFI")) {
                            networkType = NetworkObject.NETWORK_TYPE_WIFI;
                        } else {
                            networkType = netInfo.getTypeName();
                        }

                        if (info != null && !TextUtils.isEmpty(info.getSSID())) {
                            String ipAddress = Formatter.formatIpAddress(info.getIpAddress());
                            int linkSpeed = info.getLinkSpeed();
                            int networkID = info.getNetworkId();
                            String ssid = info.getSSID();
                            boolean hasHiddenSSID = info.getHiddenSSID();
                            String bssid = info.getBSSID();


                            String str = "\nNetworkID = " + networkID + "\nSSID = " + ssid + "\nHasHiddenSSID = " + hasHiddenSSID + "\nBSSID = " + bssid + "\nIs Internet Connectivity limited = " + isInternetConnectivityLimited();

                            NetworkObject networkObject = new NetworkObject();
                            networkObject.setNetworkType(networkType);
                            networkObject.setiPAddress(ipAddress);
                            networkObject.setUpSpeed(upSpeed);
                            networkObject.setDownSpeed(downSpeed);
                            networkObject.setSpeedUnit(speedUnit);
                            networkObject.setLinkSpeed(linkSpeed);
                            networkObject.setNetworkId(networkID);
                            networkObject.setSsid(ssid);
                            networkObject.setHasHiddenSSID(hasHiddenSSID);
                            networkObject.setBssid(bssid);
                            networkObject.setInternetLimited(isInternetConnectivityLimited());

                            NetworkMonitorListener.getInstance().setNetworkConnectivityStatus(str);
                            NetworkMonitorListener.getInstance().setNetworkObject(networkObject);
                        }
                    }
                } else if (nC.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    if (connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE) {
                        String connectivityType = "";
                        switch (connectivityManager.getActiveNetworkInfo().getSubtype()) {
                            case TelephonyManager.NETWORK_TYPE_GPRS:
                            case TelephonyManager.NETWORK_TYPE_EDGE:
                            case TelephonyManager.NETWORK_TYPE_CDMA:
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                            case TelephonyManager.NETWORK_TYPE_IDEN:
                            case TelephonyManager.NETWORK_TYPE_GSM:
                                connectivityType = "2G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            case TelephonyManager.NETWORK_TYPE_EHRPD:
                            case TelephonyManager.NETWORK_TYPE_HSPAP:
                            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                                connectivityType = "3G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_LTE:
                            case TelephonyManager.NETWORK_TYPE_IWLAN:
                            case 19://TelephonyManager.NETWORK_TYPE_LTE_CA:
                                connectivityType = "4G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_NR:
                                connectivityType = "5G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                                connectivityType = "NETWORK TYPE UNKNOWN";
                                break;
                            default:
                                connectivityType = "N/A";
                                break;
                        }
                        NetworkInfo netInfo = connectivityManager.getNetworkInfo(n);
                        String networkType;
                        if (netInfo.getTypeName().equalsIgnoreCase("MOBILE")) {
                            networkType = NetworkObject.NETWORK_TYPE_MOBILE;
                        } else {
                            networkType = netInfo.getTypeName();
                        }

                        String str = "NetworkType = " + networkType + "\nConnectivity Type = " + connectivityType + "\nUpSpeed = " + upSpeed + speedUnit + "\nDownSpeed = " + downSpeed + speedUnit + "\nIs Internet Connectivity limited = " + isInternetConnectivityLimited();
                        NetworkObject networkObject = new NetworkObject();
                        networkObject.setNetworkType(networkType);
                        networkObject.setiPAddress(null);
                        networkObject.setUpSpeed(upSpeed);
                        networkObject.setDownSpeed(downSpeed);
                        networkObject.setSpeedUnit(speedUnit);
                        networkObject.setLinkSpeed(-1);
                        networkObject.setNetworkId(-1);
                        networkObject.setSsid(null);
                        networkObject.setHasHiddenSSID(false);
                        networkObject.setBssid(null);
                        networkObject.setInternetLimited(isInternetConnectivityLimited());

                        NetworkMonitorListener.getInstance().setNetworkConnectivityStatus(str);
                        NetworkMonitorListener.getInstance().setNetworkObject(networkObject);
                    }
                }
                /*else if (nC.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {

                } else if (nC.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {

                } else if (nC.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {

                }*/
                else {
                    NetworkMonitorListener.getInstance().setNetworkConnectivityStatus("");
                    NetworkMonitorListener.getInstance().setNetworkObject(null);
                }
            }
            else {
                NetworkMonitorListener.getInstance().setNetworkConnectivityStatus("");
                NetworkMonitorListener.getInstance().setNetworkObject(null);
            }
        }
        NetworkMonitorListener.getInstance().setNetworkConnectivityStatus(isInternetConnectivityLimited());
    }


    //Constants.REACHABILITY_SERVER = "https://www.google.com"
    private boolean isInternetConnectivityLimited() {
        try {
            URLConnection connection = new URL("https://www.google.com").openConnection();
            connection.setRequestProperty("User-Agent", "ConnectionTest");
            connection.setRequestProperty("Connection", "close");
            connection.setConnectTimeout(1000); // configurable
            connection.connect();
            return false;
        } catch (IOException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return true;
    }


}