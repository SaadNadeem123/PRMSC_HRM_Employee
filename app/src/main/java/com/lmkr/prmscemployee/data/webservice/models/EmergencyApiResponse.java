package com.lmkr.prmscemployee.data.webservice.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmergencyApiResponse {
    @SerializedName("emergency_contact")
    private List<EmergencyContact> emergencyContacts;

    public List<EmergencyContact> getEmergencyContacts() {
        return emergencyContacts;
    }

}

