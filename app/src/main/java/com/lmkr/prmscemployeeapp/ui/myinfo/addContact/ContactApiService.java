package com.lmkr.prmscemployeeapp.ui.myinfo.addContact;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ContactApiService {
    @POST("employee/emergencycontact/create/{employeeId}")
    Call<Void> createEmergencyContact(
            @Header("Authorization") String bearerToken,
            @Path("employeeId") int employeeId,
            @Body AddContactModel contact
    );

    @PUT("employee/emergencycontact/{contactId}")
    Call<Void> updateEmergencyContact(
            @Header("Authorization") String bearerToken,
            @Path("contactId") int contactId,
            @Body AddContactModel contact
    );
}

