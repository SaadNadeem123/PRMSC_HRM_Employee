package com.lmkr.prmscemployeeapp.ui.models;

import java.util.List;

public class UserData {

    private List<UserObject> userData;
    private List<ConsigneeLocations> consigneeLocations;
    private String token;

    public List<ConsigneeLocations> getConsigneeLocations() {
        return consigneeLocations;
    }

    public List<UserObject> getUserData() {
        return userData;
    }

    public void setUserData(List<UserObject> userData) {
        this.userData = userData;
    }

    public void setConsigneeLocations(List<ConsigneeLocations> consigneeLocations) {
        this.consigneeLocations = consigneeLocations;
    }

    public String getUserToken() {
        return token;
    }

    public void setUserToken(String token) {
        this.token = token;
    }
}

