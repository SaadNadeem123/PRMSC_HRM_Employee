package com.lmkr.prmscemployee.data.webservice.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonObjectResponse {

    private boolean status;//    ": true,
    private String message;//    ": "successfully logged in",
    private JsonElement response;//    ": {}


    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResponse(JsonObject response) {
        this.response = response;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public JsonElement getResponse() {
        return response;
    }

    public String toString()
    {
        String str = "Status = " +status+ " Mesage = "+message+ " Response = "+response;
        return str;
    }

}
