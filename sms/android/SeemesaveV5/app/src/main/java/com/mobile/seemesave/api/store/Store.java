package com.mobile.seemesave.api.store;

import com.mobile.seemesave.model.res.CommonRes;
import com.mobile.seemesave.model.res.StoreRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Store {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("store/all/")
    Call<StoreRes> getStoreList(@Header("Authorization") String token,
                                @Query("offset") int offset,
                                @Query("page_size") int page_size,
                                @Query("keyword") String keyword);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("store/all/")
    Call<StoreRes> getStoreListFromLocation(@Header("Authorization") String token,
                                @Query("lat") double lat,
                                @Query("lng") double lng);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("store/promotions/")
    Call<CommonRes> getStorePromotion(@Header("Authorization") String token,
                                @Query("id") int id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("store/all/")
    Call<StoreRes> getStoreListFromCategory(@Header("Authorization") String token,
                                @Query("category_id") int id);
}
