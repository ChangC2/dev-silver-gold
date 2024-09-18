package seemesave.businesshub.api.promote;


import seemesave.businesshub.model.res.CommonRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Promote {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("vendor/pay-to-promote/search/")
    Call<CommonRes> getPromoteProducts(@Header("Authorization") String token,
                                       @Header("Content-Language") String portal_token,
                                       @Query("promotion_type") int promotion_type,
                                       @Query("store_id") int store_id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("supplier/pay-to-promote/all/")
    Call<CommonRes> getPaytoPromoteList(@Header("Authorization") String token,
                                        @Header("Content-Language") String portal_token,
                                        @Query("keyword") String keyword,
                                        @Query("offset") int offset,
                                        @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("supplier/pay-to-promote/base/")
    Call<CommonRes> getPromoteBaseInfo(@Header("Authorization") String token,
                                       @Header("Content-Language") String portal_token);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("supplier/pay-to-promote/get-by-id/")
    Call<CommonRes> getPromoteDetail(@Header("Authorization") String token,
                                     @Header("Content-Language") String portal_token,
                                     @Query("id") int id);
}
