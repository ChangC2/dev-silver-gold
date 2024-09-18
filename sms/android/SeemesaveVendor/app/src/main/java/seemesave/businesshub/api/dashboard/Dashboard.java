package seemesave.businesshub.api.dashboard;

import seemesave.businesshub.model.res.CommonRes;
import seemesave.businesshub.model.res.DashboardCommentListRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Dashboard {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("vendor/dashboard/")
    Call<CommonRes> getDashboardInfo(@Header("Authorization") String token,
                                       @Header("Content-Language") String portal_token);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("vendor/dashboard/more-comments/")
    Call<DashboardCommentListRes> getDashboardComments(@Header("Authorization") String token,
                                                       @Header("Content-Language") String portal_token,
                                                       @Query("offset") int offset,
                                                       @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("supplier/dashboard/")
    Call<CommonRes> getSupplierDashboardInfo(@Header("Authorization") String token,
                                     @Header("Content-Language") String portal_token);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("supplier/dashboard/more-comments/")
    Call<DashboardCommentListRes> getSupplierDashboardComments(@Header("Authorization") String token,
                                                       @Header("Content-Language") String portal_token,
                                                       @Query("offset") int offset,
                                                       @Query("page_size") int page_size);

}
