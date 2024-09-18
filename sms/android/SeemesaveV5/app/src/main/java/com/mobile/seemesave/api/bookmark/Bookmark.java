package com.mobile.seemesave.api.bookmark;

import com.mobile.seemesave.model.res.CommonRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Bookmark {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("bookmark/category/get/")
    Call<CommonRes> getBookmarkCategory(@Header("Authorization") String token);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("bookmark/feed/")
    Call<CommonRes> getAllBookmarkPosts(@Header("Authorization") String token,
                                      @Query("category_id") String id,
                                        @Query("offset") int offset,
                                        @Query("page_size") int page_size,
                                        @Query("is_promotion") boolean is_promotion);
}
