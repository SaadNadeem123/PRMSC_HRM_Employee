package com.lmkr.prmscemployee.data.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "AttendanceHistory")
public class AttendanceHistory {

    @PrimaryKey(autoGenerate = false)
    private int id;                    //": 14,
    private int employee_id;                   //": 1,
    private String checkin_time;                  //": "2023-07-17T06:34:59.000Z",
    private String checkout_time;                 //": null,
    private int String;                   //": null
    private String lateReason;                    //": null
    
    
    public java.lang.String getLateReason() {
        return lateReason;
    }
    
    public void setLateReason(java.lang.String lateReason) {
        this.lateReason = lateReason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public java.lang.String getCheckin_time() {
        return checkin_time;
    }

    public void setCheckin_time(java.lang.String checkin_time) {
        this.checkin_time = checkin_time;
    }

    public java.lang.String getCheckout_time() {
        return checkout_time;
    }

    public void setCheckout_time(java.lang.String checkout_time) {
        this.checkout_time = checkout_time;
    }

    public int getString() {
        return String;
    }

    public void setString(int string) {
        String = string;
    }
}
