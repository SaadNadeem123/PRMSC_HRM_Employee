package com.lmkr.prmscemployeeapp.data.webservice.models;

import java.util.List;

public class UserData {

    private List<UserObject> basicData;
    private List<LeaveCount> leaveCount;
    private List<LeaveRequestCount> leaveRequestCount;
    private List<TeamMemberCount> teamMemberCount;
    private String token;

    public List<UserObject> getBasicData() {
        return basicData;
    }

    public void setBasicData(List<UserObject> basicData) {
        this.basicData = basicData;
    }

    public List<LeaveCount> getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(List<LeaveCount> leaveCount) {
        this.leaveCount = leaveCount;
    }

    public List<LeaveRequestCount> getLeaveRequestCount() {
        return leaveRequestCount;
    }

    public void setLeaveRequestCount(List<LeaveRequestCount> leaveRequestCount) {
        this.leaveRequestCount = leaveRequestCount;
    }

    public List<TeamMemberCount> getTeamMemberCount() {
        return teamMemberCount;
    }

    public void setTeamMemberCount(List<TeamMemberCount> teamMemberCount) {
        this.teamMemberCount = teamMemberCount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

