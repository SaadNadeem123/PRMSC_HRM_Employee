package com.lmkr.prmscemployeeapp.data.webservice.api;

public class ApiCalls {
    public static final String API_VERSION = "v1";
    public static final String BASE_URL_DEV = "http://20.55.70.106:8000/api/" + API_VERSION + "/";
    public static final String BASE_URL_LIVE = "https://backendhrmprmsc.lmkr.com/api/" + API_VERSION+ "/";

    public static final String BASE_DOCUMENT_URL_DEV = "http://20.55.70.106:8000/api/" + API_VERSION+ "/";
    public static final String BASE_DOCUMENT_URL_LIVE = "https://backendhrmprmsc.lmkr.com/api/" + API_VERSION+ "/";

    public static final String BASE_EMERGENCY_URL_DEV= "http://20.55.70.106:8000/api/";
    public static final String BASE_EMERGENCY_URL_LIVE= "https://backendhrmprmsc.lmkr.com/api/";

//    LIVE
//    public static final String BASE_URL = BASE_URL_LIVE;
//    public static final String BASE_DOCUMENT_URL = BASE_DOCUMENT_URL_LIVE;
//    public static final String BASE_EMERGENCY_URL= BASE_EMERGENCY_URL_LIVE + API_VERSION+ "/";
    

//    DEV
    public static final String BASE_URL = BASE_URL_DEV;
    public static final String BASE_DOCUMENT_URL = BASE_DOCUMENT_URL_DEV;
    public static final String BASE_EMERGENCY_URL= BASE_EMERGENCY_URL_DEV + API_VERSION+ "/";
}