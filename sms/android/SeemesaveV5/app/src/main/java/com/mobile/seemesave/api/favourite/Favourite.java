package com.mobile.seemesave.api.favourite;

import com.mobile.seemesave.model.res.CommonRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Favourite {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("favourites/")
    Call<CommonRes> getFavouriteProduct(@Header("Authorization") String token,
                                         @Query("type") String type,
                                        @Query("offset") int offset,
                                        @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("friend/detail-information/")
    Call<CommonRes> getFollowingProduct(@Header("Authorization") String token,
                                        @Query("user_id") int user_id,
                                        @Query("type") String type,
                                        @Query("offset") int offset,
                                        @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("favourites/")
    Call<CommonRes> getFavouriteBrand(@Header("Authorization") String token,
                                         @Query("type") String type,
                                         @Query("offset") int offset,
                                         @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("friend/detail-information/")
    Call<CommonRes> getFollowingBrand(@Header("Authorization") String token,
                                      @Query("user_id") int user_id,
                                      @Query("type") String type,
                                      @Query("offset") int offset,
                                      @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("favourites/")
    Call<CommonRes> getFavouriteStore(@Header("Authorization") String token,
                                         @Query("type") String type,
                                         @Query("offset") int offset,
                                         @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("friend/detail-information/")
    Call<CommonRes> getFollowingStore(@Header("Authorization") String token,
                                      @Query("user_id") int user_id,
                                      @Query("type") String type,
                                      @Query("offset") int offset,
                                      @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("tag/detail/")
    Call<CommonRes> getFavouriteSearch(@Header("Authorization") String token,
                                      @Query("category") String type,
                                      @Query("tag") String tag,
                                      @Query("offset") int offset,
                                      @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("favourites/")
    Call<CommonRes> getFavouritePost(@Header("Authorization") String token,
                                         @Query("type") String type,
                                         @Query("offset") int offset,
                                         @Query("page_size") int page_size);
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("friend/detail-information/")
    Call<CommonRes> getFollowingPost(@Header("Authorization") String token,
                                     @Query("user_id") int user_id,
                                     @Query("type") String type,
                                     @Query("offset") int offset,
                                     @Query("page_size") int page_size);
}
