package com.lmkr.prmscemployeeapp.ui.myinfo.emergencyUi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {
    @SerializedName("emergency_contact")
    private List<EmergencyContact> emergencyContacts;

    public List<EmergencyContact> getEmergencyContacts() {
        return emergencyContacts;
    }

 /*   public void setEmergencyContacts(List<EmergencyContact> emergencyContacts) {
        this.emergencyContacts = emergencyContacts;
    }*/
}

