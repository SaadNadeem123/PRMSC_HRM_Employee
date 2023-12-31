package com.lmkr.prmscemployee.data.webservice.api;

import com.google.gson.JsonObject;
import com.lmkr.prmscemployee.data.webservice.models.AddContactModel;
import com.lmkr.prmscemployee.data.webservice.models.ApiBaseResponse;
import com.lmkr.prmscemployee.data.webservice.models.AttendanceHistoryResponse;
import com.lmkr.prmscemployee.data.webservice.models.CreateLeaveRequestResponse;
import com.lmkr.prmscemployee.data.webservice.models.LeaveManagementRequestResponse;
import com.lmkr.prmscemployee.data.webservice.models.LeaveRequestResponse;
import com.lmkr.prmscemployee.data.webservice.models.UserData;
import com.lmkr.prmscemployee.data.webservice.models.EmergencyApiResponse;
import com.lmkr.prmscemployee.data.webservice.models.BulletinResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Urls {

    @POST("employee/login")
    Call<UserData> loginUser(@Body JsonObject body);

    @GET("attendance/individual_attendance/{UserID}")
    Call<AttendanceHistoryResponse> getAttendanceHistory(@Header("Authorization") String standardHeaders, @Path("UserID") String user_id);

    @GET("employee/request/leave")
    Call<LeaveRequestResponse> getLeaveRequest(@Header("Authorization") String standardHeaders);

    @GET("employee/manager/leaverequest")
    Call<LeaveManagementRequestResponse> getLeaveRequestManager(@Header("Authorization") String standardHeaders);

    @POST("employee/login")
    Call<UserData> loginUserApi(@Body JsonObject body);

    @POST("employee/changePassword")
    Call<ApiBaseResponse> changePassword(@Header("Authorization") String standardHeaders,@Body JsonObject body);

    @POST("employee/firebasedevice/create")
    Call<ApiBaseResponse> updateFirebaseToken(@Header("Authorization") String standardHeaders,@Body JsonObject body);

    
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
    
    
    @POST("attendance/attendance")
    Call<ApiBaseResponse> checkIn(@Header("Authorization") String standardHeaders, @Body JsonObject body);

    @Multipart
    @POST("attendance/attendance")
    Call<ApiBaseResponse> checkInMultipart(@Header("Authorization") String standardHeaders,
                                           @Part MultipartBody.Part file,
                                           @Part("employee_id")RequestBody employee_id,
                                           @Part("checkin_time")RequestBody checkin_time,
                                           @Part("comments")RequestBody lateComment,
                                           @Part("lat")RequestBody lat,
                                           @Part("longitude")RequestBody longitude,
                                           @Part("source")RequestBody source);
        @Multipart
    @POST("attendance/attendance")
    Call<ApiBaseResponse> checkInMultipart(@Header("Authorization") String standardHeaders,
                                           @Part MultipartBody.Part file,
                                           @Part("employee_id")RequestBody employee_id,
                                           @Part("checkin_time")RequestBody checkin_time,
                                           @Part("lat")RequestBody lat,
                                           @Part("longitude")RequestBody longitude,
                                           @Part("source")RequestBody source);
    
    @PUT("attendance/attendance/{UserID}")
    Call<ApiBaseResponse> checkout(@Header("Authorization") String standardHeaders,@Path("UserID") String user_id,@Body JsonObject body);
    

    @GET("employee/emergencycontact/{id}")
    Call<EmergencyApiResponse> getEmergencyContact(
            @Header("Authorization") String token,
            @Path("id") int employeeId
    );


    @PUT("employee/emergencycontact/{contactId}")
    Call<ApiBaseResponse> updateEmergencyContact(
            @Header("Authorization") String bearerToken,
            @Path("contactId") int contactId,
            @Body JsonObject body
    );

    @POST("employee/emergencycontact/create/{id}")
    Call<ApiBaseResponse> createEmergencyContact(
            @Header("Authorization") String bearerToken,
            @Path("id") int employeeId,
            @Body AddContactModel data
    );

    @GET("employee/news")
    Call<BulletinResponse> getBulletinList();
    
    @PUT("employee/leaveapproval/{id}")
	Call<ApiBaseResponse> approveRejectLeave(
            @Header("Authorization") String bearerToken,
            @Path("id") int leaveRequestId,
            @Body JsonObject body);
	
}
