package com.lmkr.prmscemployeeapp.ui.bulletin;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BulletinService {
    @GET("employee/news")
    Call<BulletinResponse> getBulletinList();
}

