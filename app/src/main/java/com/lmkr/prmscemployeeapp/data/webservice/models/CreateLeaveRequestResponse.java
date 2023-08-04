package com.lmkr.prmscemployeeapp.data.webservice.models;

import com.lmkr.prmscemployeeapp.data.database.models.AttendanceHistory;

import java.util.List;

public class CreateLeaveRequestResponse {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
}
