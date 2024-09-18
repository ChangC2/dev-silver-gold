package com.mobile.seemesave.api.product;

import com.mobile.seemesave.model.res.CommonRes;
import com.mobile.seemesave.model.res.ProductRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Product {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("product/detail/")
    Call<ProductRes> getProductDetail(@Header("Authorization") String token,
                                      @Query("barcode") String barcode);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("product/promotions/")
    Call<CommonRes> getProductPromotion(@Header("Authorization") String token,
                                      @Query("barcode") String barcode);

}
