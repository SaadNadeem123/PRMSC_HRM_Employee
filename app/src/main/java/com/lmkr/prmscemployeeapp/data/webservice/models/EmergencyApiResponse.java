package com.lmkr.prmscemployeeapp.data.webservice.models;

import com.google.gson.annotations.SerializedName;
import com.lmkr.prmscemployeeapp.data.webservice.models.EmergencyContact;

import java.util.List;

public class EmergencyApiResponse {
    @SerializedName("emergency_contact")
    private List<EmergencyContact> emergencyContacts;

    public List<EmergencyContact> getEmergencyContacts() {
        return emergencyContacts;
    }

}

