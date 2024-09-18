package com.mobile.seemesave.api.user;

import com.google.gson.JsonObject;
import com.mobile.seemesave.model.req.LoginReq;
import com.mobile.seemesave.model.req.SignupReq;
import com.mobile.seemesave.model.res.AlertCountRes;
import com.mobile.seemesave.model.res.CommonRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface User {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("user/login/")
    Call<CommonRes> login(@Body LoginReq req);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("user/register/")
    Call<CommonRes> signup(@Body SignupReq req);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @PUT("user/register/")
    Call<CommonRes> sendOTP(@Body SignupReq req);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @PUT("user/user-mgr/")
    Call<CommonRes> updateUserInfo(@Header("Authorization") String token, @Body JsonObject req);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @PUT("user/user-mgr/")
    Call<CommonRes> updateUserProfile(@Header("Authorization") String token, @Body JsonObject req);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("user/profile/")
    Call<CommonRes> getUserInfo(@Header("Authorization") String token);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("user/mobile-notifications/")
    Call<AlertCountRes> getAlertCount(@Header("Authorization") String token);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("user/user-base-info/")
    Call<CommonRes> getUserBaseInfo(@Header("Authorization") String token);
}
