package com.lmkr.prmscemployeeapp.ui.myinfo.addContact;


import com.google.gson.annotations.SerializedName;

public class AddContactModel {

    @SerializedName("employee_id")
    private int employeeId;
    private String name;
    private String email;
    private String relation;
    private String mobile;
    @SerializedName("work_number")
    private String workNumber;
    @SerializedName("home_number")
    private String homeNumber;
    private String address;

  /*  public AddEmergencyContact(int employeeId, String name, String email, String relation, String mobile, String workNumber, String homeNumber, String address) {
        this.employeeId = employeeId;
        this.name = name;
        this.email = email;
        this.relation = relation;
        this.mobile = mobile;
        this.workNumber = workNumber;
        this.homeNumber = homeNumber;
        this.address = address;
    }*/

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

