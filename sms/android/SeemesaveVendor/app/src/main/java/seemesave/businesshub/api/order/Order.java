package seemesave.businesshub.api.order;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import seemesave.businesshub.model.res.CommonRes;
import seemesave.businesshub.model.res.ProductRes;

public interface Order {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("vendor/click-collect/all/")
    Call<CommonRes> getCollectList(@Header("Authorization") String token,
                                               @Header("Content-Language") String portal_token,
                                               @Query("is_ready") boolean is_ready,
                                               @Query("is_finished") boolean is_finished,
                                               @Query("username") String username,
                                               @Query("offset") int offset,
                                               @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("vendor/click-collect/all/")
    Call<CommonRes> getCollectPendingList(@Header("Authorization") String token,
                                   @Header("Content-Language") String portal_token,
                                   @Query("is_pending") boolean is_ready,
                                   @Query("username") String username,
                                   @Query("offset") int offset,
                                   @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("vendor/click-deliver/all/")
    Call<CommonRes> getDeliverList(@Header("Authorization") String token,
                                   @Header("Content-Language") String portal_token,
                                   @Query("keyword") String username,
                                   @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("vendor/click-deliver/all/")
    Call<CommonRes> getDeliverSuccessList(@Header("Authorization") String token,
                                   @Header("Content-Language") String portal_token,
                                   @Query("keyword") String username,
                                   @Query("offset") int offset,
                                   @Query("page_size") int page_size,
                                   @Query("cart_status") int cart_status);
}
