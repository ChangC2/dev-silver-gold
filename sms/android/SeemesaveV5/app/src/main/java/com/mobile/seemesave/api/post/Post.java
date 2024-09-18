package com.mobile.seemesave.api.post;

import com.mobile.seemesave.model.res.CommonRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Post {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("post/get-rel-posts/")
    Call<CommonRes> getRelatedMediaList(@Header("Authorization") String token,
                                @Query("post_id") int post_id,
                                @Query("offset") int offset,
                                @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("post/")
    Call<CommonRes> getMyPostList(@Header("Authorization") String token,
                                        @Query("offset") int offset,
                                        @Query("page_size") int page_size);
}
