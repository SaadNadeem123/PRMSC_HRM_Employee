package com.lmkr.prmscemployee.data.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "LeaveRequest")
public class LeaveRequest implements Serializable {

    @PrimaryKey(autoGenerate = false)
    private int id;                       //": 7,
    private int employee_id;                      //": 1,
    private String employee_name;                        //": "Hassan",
    private int leave_type_id;                        //": 1,
    private String leave_type_name;                      //": "Casual",
    private String from_date;                        //": "2023-07-25T00:00:00.000Z",
    private String to_date;                      //": "2023-08-02T00:00:00.000Z",
    private String from_time;                        //": null,
    private String to_time;                      //": null,
    private float total_days;                       //": 7,
    private String attachment;                       //": "123442424",
    private String attachment_name;                      //": null,
    private String reason;                       //": "ABCD ABCD ABCD ABCD ABCD ABCD ",
    private String emergency_contact;                        //": "03232323232",
    private double lat;                      //": 0,
    private double lng;                      //": 0,
    private String status;                       //": "approved",
    private int first_approver_id;                        //": 16,
    private String approver_reason;                      //": "Approved",
    private String first_approver_name;                      //": "Hammad Mahmood",
    private String approved_at;                      //": "2023-07-24T07:11:01.000Z"

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

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public int getLeave_type_id() {
        return leave_type_id;
    }

    public void setLeave_type_id(int leave_type_id) {
        this.leave_type_id = leave_type_id;
    }

    public String getLeave_type_name() {
        return leave_type_name;
    }

    public void setLeave_type_name(String leave_type_name) {
        this.leave_type_name = leave_type_name;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }

    public float getTotal_days() {
        return total_days;
    }

    public void setTotal_days(float total_days) {
        this.total_days = total_days;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getAttachment_name() {
        return attachment_name;
    }

    public void setAttachment_name(String attachment_name) {
        this.attachment_name = attachment_name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEmergency_contact() {
        return emergency_contact;
    }

    public void setEmergency_contact(String emergency_contact) {
        this.emergency_contact = emergency_contact;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFirst_approver_id() {
        return first_approver_id;
    }

    public void setFirst_approver_id(int first_approver_id) {
        this.first_approver_id = first_approver_id;
    }

    public String getApprover_reason() {
        return approver_reason;
    }

    public void setApprover_reason(String approver_reason) {
        this.approver_reason = approver_reason;
    }

    public String getFirst_approver_name() {
        return first_approver_name;
    }

    public void setFirst_approver_name(String first_approver_name) {
        this.first_approver_name = first_approver_name;
    }

    public String getApproved_at() {
        return approved_at;
    }

    public void setApproved_at(String approved_at) {
        this.approved_at = approved_at;
    }
}
