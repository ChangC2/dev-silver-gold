package com.mobile.seemesave.api.notification;

import com.mobile.seemesave.model.res.CommonRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Notification {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("notification/")
    Call<CommonRes> getNotificationAll(@Header("Authorization") String token,
                                @Query("offset") int offset,
                                @Query("page_size") int page_size);
}
