package com.lmkr.prmscemployee.data.webservice.models;

import com.lmkr.prmscemployee.data.database.models.LeaveRequest;

import java.util.List;

public class LeaveRequestResponse {
    private String message;
    private List<LeaveRequest> leaveRequest;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LeaveRequest> getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(List<LeaveRequest> leaveRequest) {
        this.leaveRequest = leaveRequest;
    }
}
