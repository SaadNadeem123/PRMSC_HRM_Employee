package com.lmkr.prmscemployeeapp.data.webservice.api;

import com.google.gson.JsonObject;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Urls {

    @POST("employee/login")
    Call<JsonObjectResponse> loginUser(@Body JsonObject body);

    @GET("attendance/attendance/{UserID}")
    Call<UserData> getAttendanceHistory(@Header("Authorization") String standardHeaders,@Path("UserID") String user_id);

    @POST("employee/login")
    Call<UserData> loginUserApi(@Body JsonObject body);

    @POST("url")
    Call<JsonObjectResponse> checkIn(@Header("Authorization") String standardHeaders, @Body JsonObject body);

}
