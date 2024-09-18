package com.mobile.seemesave.api.newsfeed;

import com.mobile.seemesave.model.res.CommonRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NewsFeed {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("newsfeed/dashboard-mobile/")
    Call<CommonRes> getNewsFeed(@Header("Authorization") String token,
                                @Query("offset") int offset,
                                @Query("page_size") int page_size,
//                                @Query("lat") double lat,
//                                @Query("lng") double lng,
                                @Query("is_deliver") boolean is_click_collect);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("newsfeed/following/")
    Call<CommonRes> getFollowingFeedList(@Header("Authorization") String token,
                                        @Query("offset") int offset,
                                        @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("newsfeed/ad-list/")
    Call<CommonRes> getAllCanMiss(@Header("Authorization") String token,
                                         @Query("offset") int offset,
                                         @Query("page_size") int page_size,
                                         @Query("is_deliver") boolean is_deliver,
                                         @Query("feed_type") String feed_type);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("newsfeed/ad-detail/")
    Call<CommonRes> getAdDetail(@Header("Authorization") String token,
                                @Query("id") int id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("story/detail/")
    Call<CommonRes> getStoryDetail(@Header("Authorization") String token,
                                  @Query("id") int id);
}
