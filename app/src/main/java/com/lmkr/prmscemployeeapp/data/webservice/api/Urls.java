package com.lmkr.prmscemployeeapp.data.webservice.api;

import com.google.gson.JsonObject;
import com.lmkr.prmscemployeeapp.data.webservice.models.AttendanceHistoryResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.LeaveRequestResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Urls {

    @POST("employee/login")
    Call<JsonObjectResponse> loginUser(@Body JsonObject body);

    @GET("attendance/attendance/{UserID}")
    Call<AttendanceHistoryResponse> getAttendanceHistory(@Header("Authorization") String standardHeaders, @Path("UserID") String user_id);

    @GET("employee/request/leave")
    Call<LeaveRequestResponse> getLeaveRequest(@Header("Authorization") String standardHeaders);

    @POST("employee/login")
    Call<UserData> loginUserApi(@Body JsonObject body);

    @Multipart
    @POST("attendance/attendance")
    Call<JsonObjectResponse> checkInMultipart(@Header("Authorization") String standardHeaders, @Part MultipartBody.Part filePart, @Part JsonObject body);

    @POST("attendance/attendance")
    Call<JsonObjectResponse> checkIn(@Header("Authorization") String standardHeaders, @Body JsonObject body);

}
