package seemesave.businesshub.api.follower;

import seemesave.businesshub.model.res.CommonRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Follower {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("vendor/follower/store/")
    Call<CommonRes> getFollowerList(@Header("Authorization") String token,
                                    @Header("Content-Language") String portal_token,
                                    @Query("id") int id,
                                    @Query("offset") int offset,
                                    @Query("page_size") int page_size,
                                    @Query("keyword") String keyword);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("supplier/follower/brand/")
    Call<CommonRes> getSupplierFollowerList(@Header("Authorization") String token,
                                    @Header("Content-Language") String portal_token,
                                    @Query("id") int id,
                                    @Query("offset") int offset,
                                    @Query("page_size") int page_size,
                                    @Query("keyword") String keyword);

}
