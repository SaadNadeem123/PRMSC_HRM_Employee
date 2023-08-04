package com.lmkr.prmscemployeeapp.data.webservice.api;

import com.google.gson.JsonObject;
import com.lmkr.prmscemployeeapp.data.webservice.models.AttendanceHistoryResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.CreateLeaveRequestResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.LeaveRequestResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    @POST("employee/leaverequest/create")
    Call<CreateLeaveRequestResponse> leaveRequestMultipart(
            @Header("Authorization") String standardHeaders,
            @Part MultipartBody.Part file,
            @Part("employee_id")RequestBody employee_id,
            @Part("leave_type_id")RequestBody leave_type_id,
            @Part("from_date")RequestBody from_date,
            @Part("to_date")RequestBody to_date,
            @Part("from_time")RequestBody form_time,
            @Part("to_time")RequestBody to_time,
            @Part("total_days")RequestBody total_days,
            @Part("reason")RequestBody reason,
            @Part("emergency_contact")RequestBody emergency_contact,
            @Part("lat")RequestBody lat,
            @Part("lng")RequestBody lng,
            @Part("source")RequestBody source,
            @Part("first_approver")RequestBody first_approver,
            @Part("status") RequestBody status);

    @Multipart
    @POST("attendance/attendance")
    Call<JsonObjectResponse> checkInMultipart(@Header("Authorization") String standardHeaders,
                                              @Part MultipartBody.Part file,
                                                      @Part("employee_id")RequestBody employee_id,
                                                      @Part("checkin_time")RequestBody checkin_time,
                                                      @Part("lat")RequestBody lat,
                                                      @Part("longitude")RequestBody longitude,
                                                      @Part("source")RequestBody source,
                                                      @Part("file_name")RequestBody file_name,
                                                      @Part("file_path")RequestBody file_path);

    @POST("attendance/attendance")
    Call<JsonObjectResponse> checkIn(@Header("Authorization") String standardHeaders, @Body JsonObject body);

}
