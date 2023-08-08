package com.lmkr.prmscemployeeapp.ui.myinfo.emergencyUi;

import com.google.gson.annotations.SerializedName;

public class EmergencyContact {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("relation")
    private String relation;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("work")
    private String work;
    @SerializedName("home")
    private String home;
    @SerializedName("address")
    private String address;

    // Constructor
    public EmergencyContact(int id, String name, String email, String relation, String mobile,
                            String work, String home, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.relation = relation;
        this.mobile = mobile;
        this.work = work;
        this.home = home;
        this.address = address;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

