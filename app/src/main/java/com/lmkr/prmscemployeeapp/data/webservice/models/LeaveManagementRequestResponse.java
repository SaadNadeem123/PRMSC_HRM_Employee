package com.lmkr.prmscemployeeapp.data.webservice.models;

import java.util.List;

public class LeaveManagementRequestResponse {
    private String message;
    private List<LeaveManagementModel> leaveRequest;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LeaveManagementModel> getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(List<LeaveManagementModel> leaveRequest) {
        this.leaveRequest = leaveRequest;
    }
}
