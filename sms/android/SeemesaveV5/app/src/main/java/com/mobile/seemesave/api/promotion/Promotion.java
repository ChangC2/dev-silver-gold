package com.mobile.seemesave.api.promotion;

import com.mobile.seemesave.model.res.CommonRes;
import com.mobile.seemesave.model.res.PromotionRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Promotion {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("promotion/")
    Call<PromotionRes> getPromotionDetail(@Header("Authorization") String token,
                                          @Query("id") int id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("promotion/single/")
    Call<CommonRes> getPromotionSingleProducts(@Header("Authorization") String token,
                                               @Query("id") int id,
                                               @Query("category_id") int category_id,
                                               @Query("offset") int offset,
                                               @Query("page_size") int page_size);


}
