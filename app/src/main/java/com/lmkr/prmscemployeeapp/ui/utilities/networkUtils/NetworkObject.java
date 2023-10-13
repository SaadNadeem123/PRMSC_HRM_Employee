package com.lmkr.prmscemployeeapp.ui.utilities.networkUtils;

import android.net.wifi.WifiInfo;

public class NetworkObject {
    public static final String NETWORK_TYPE_WIFI = "WIFI";
    public static final String NETWORK_TYPE_MOBILE = "Cellular Network";
    String networkType;
    String connectivityType;
    String iPAddress;
    int upSpeed;
    int downSpeed;
    String speedUnit;

    public int getLinkSpeed() {
        return linkSpeed;
    }

    public void setLinkSpeed(int linkSpeed) {
        this.linkSpeed = linkSpeed;
    }

    int linkSpeed;
    String linkSpeedUnit = WifiInfo.LINK_SPEED_UNITS;
    int networkId;
    String ssid;
    boolean hasHiddenSSID;
    String bssid;
    boolean isInternetLimited;

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getConnectivityType() {
        return connectivityType;
    }

    public void setConnectivityType(String connectivityType) {
        this.connectivityType = connectivityType;
    }

    public String getiPAddress() {
        return iPAddress;
    }

    public void setiPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    public int getUpSpeed() {
        return upSpeed;
    }

    public void setUpSpeed(int upSpeed) {
        this.upSpeed = upSpeed;
    }

    public int getDownSpeed() {
        return downSpeed;
    }

    public void setDownSpeed(int downSpeed) {
        this.downSpeed = downSpeed;
    }

    public String getSpeedUnit() {
        return speedUnit;
    }

    public void setSpeedUnit(String speedUnit) {
        this.speedUnit = speedUnit;
    }


    public String getLinkSpeedUnit() {
        return linkSpeedUnit;
    }

    public int getNetworkId() {
        return networkId;
    }

    public void setNetworkId(int networkId) {
        this.networkId = networkId;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public boolean isHasHiddenSSID() {
        return hasHiddenSSID;
    }

    public void setHasHiddenSSID(boolean hasHiddenSSID) {
        this.hasHiddenSSID = hasHiddenSSID;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public boolean isInternetLimited() {
        return isInternetLimited;
    }

    public void setInternetLimited(boolean internetLimited) {
        isInternetLimited = internetLimited;
    }
}
