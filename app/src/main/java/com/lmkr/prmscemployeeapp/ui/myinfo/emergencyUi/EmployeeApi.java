package com.lmkr.prmscemployeeapp.ui.myinfo.emergencyUi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface EmployeeApi {
    @GET("employee/emergencycontact/{id}")
    Call<ApiResponse> getEmergencyContact(
            @Header("Authorization") String token,
            @Path("id") int employeeId
    );
}
