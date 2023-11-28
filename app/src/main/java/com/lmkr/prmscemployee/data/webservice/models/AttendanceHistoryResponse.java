package com.lmkr.prmscemployee.data.webservice.models;

import com.lmkr.prmscemployee.data.database.models.AttendanceHistory;

import java.util.List;

public class AttendanceHistoryResponse extends ApiBaseResponse{
    private List<AttendanceHistory> results;

    public List<AttendanceHistory> getResults() {
        return results;
    }

    public void setResults(List<AttendanceHistory> results) {
        this.results = results;
    }
}
