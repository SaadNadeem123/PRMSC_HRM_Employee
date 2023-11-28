package com.lmkr.prmscemployee.data.webservice.models;

public class ApiBaseResponse {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
    private ApiBaseResponse body;

    public ApiBaseResponse getBody() {
        return body;
    }

    public void setBody(ApiBaseResponse body) {
        this.body = body;
    }
}
