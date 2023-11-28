package com.lmkr.prmscemployee.data.webservice.models;

public class UserObject {

    private int id;                                            //": 1,
    private String application_access;                         //": "yes",
    private String name;                                       //": "Hassan",
    private String father_name;                                //": "Tahir",
    private String cnic_number;                                //": "0123456789101",
    private String designation;                                //": "HR",
    private String dob;                                        //": "2000-06-21T00:00:00.000Z",
    private String domicile;                                   //": "Jhelum",
    private String marital_status;                             //": "unmarried",
    private String email;                                      //": "hassan@lmkt.com",
    private String mobile;                                     //": "03032191991",
    private String landline;                                   //": "0544271112",
    private String address;                                    //": "House no 510 G9/3",
    private String next_to_kin_name;                           //": "Hamza Tahir",
    private String next_to_kin_contact;                        //": "03365759858",
    private int status;                                        //": 1,
    private String facelock;                                   //": "yes",
    private String geofence;                                   //": "yes",
    private String have_ssid;                                   //": "yes",
    private String list;                                        //": "XX:XX:XX:XX:XX:XX",
    private int ssid;                                        //": 1,
    private String checkout_check;                             //": "yes",

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getHave_ssid() {
        return have_ssid;
    }

    public void setHave_ssid(String have_ssid) {
        this.have_ssid = have_ssid;
    }

    public int getSsid() {
        return ssid;
    }

    public void setSsid(int ssid) {
        this.ssid = ssid;
    }

    private int geofence_id;                                   //": 9,
    private String geofence_name;                              //": "Location 1",
    private String geofencepoint_text;                         //": "POLYGON((73.056740942452 33.7107174921365,73.0583792473862 33.711411375713,73.0576155903876 33.7124996679882,73.0561669888723 33.7117338304433,73.056740942452 33.7107174921365))",
    private int manager_id;                                    //": 16,
    private String manager_name;                               //": "Hammad Mahmood",
    private int department_id;                                 //": 1,
    private String department_name;                            //": "HR",
    private int created_by;                                    //": 1
    private String hire_date;
    private String employment_status;
    private String wing_name;

    public String getCheckout_check() {
        return checkout_check;
    }

    public void setCheckout_check(String checkout_check) {
        this.checkout_check = checkout_check;
    }

    public String getHire_date() {
        return hire_date;
    }

    public void setHire_date(String hire_date) {
        this.hire_date = hire_date;
    }

    public String getEmployment_status() {
        return employment_status;
    }

    public void setEmployment_status(String employment_status) {
        this.employment_status = employment_status;
    }

    public String getWing_name() {
        return wing_name;
    }

    public void setWing_name(String wing_name) {
        this.wing_name = wing_name;
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

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getCnic_number() {
        return cnic_number;
    }

    public void setCnic_number(String cnic_number) {
        this.cnic_number = cnic_number;
    }

    public String getApplication_access() {
        return application_access;
    }

    public void setApplication_access(String application_access) {
        this.application_access = application_access;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDomicile() {
        return domicile;
    }

    public void setDomicile(String domicile) {
        this.domicile = domicile;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNext_to_kin_name() {
        return next_to_kin_name;
    }

    public void setNext_to_kin_name(String next_to_kin_name) {
        this.next_to_kin_name = next_to_kin_name;
    }

    public String getNext_to_kin_contact() {
        return next_to_kin_contact;
    }

    public void setNext_to_kin_contact(String next_to_kin_contact) {
        this.next_to_kin_contact = next_to_kin_contact;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFacelock() {
        return facelock;
    }

    public void setFacelock(String facelock) {
        this.facelock = facelock;
    }

    public String getGeofence() {
        return geofence;
    }

    public void setGeofence(String geofence) {
        this.geofence = geofence;
    }

    public int getGeofence_id() {
        return geofence_id;
    }

    public void setGeofence_id(int geofence_id) {
        this.geofence_id = geofence_id;
    }

    public String getGeofence_name() {
        return geofence_name;
    }

    public void setGeofence_name(String geofence_name) {
        this.geofence_name = geofence_name;
    }

    public String getGeofencepoint_text() {
        return geofencepoint_text;
    }

    public void setGeofencepoint_text(String geofencepoint_text) {
        this.geofencepoint_text = geofencepoint_text;
    }

    public int getManager_id() {
        return manager_id;
    }

    public void setManager_id(int manager_id) {
        this.manager_id = manager_id;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }
}