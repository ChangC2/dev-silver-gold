package com.mobile.seemesave.api.brand;

import com.mobile.seemesave.model.res.CommonRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Brand {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("brands/promotions/")
    Call<CommonRes> getBrandPromotion(@Header("Authorization") String token,
                                @Query("id") int id);
}
