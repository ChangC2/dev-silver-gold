package seemesave.businesshub.api.common;

import seemesave.businesshub.model.res.CommonRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import seemesave.businesshub.model.res.PortalProfileInfoRes;

public interface Common {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/base-info/")
    Call<CommonRes> getBaseInfo(@Header("Authorization") String token,
                                       @Header("Content-Language") String portal_token);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/ads/rate-list/")
    Call<CommonRes> getRateList(@Header("Authorization") String token,
                                @Header("Content-Language") String portal_token,
                                @Query("currency") String currency);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/profile/get/")
    Call<PortalProfileInfoRes> getPortalProfile(@Header("Authorization") String token,
                                                @Header("Content-Language") String portal_token);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("auth/token-info/")
    Call<CommonRes> getUserInfo(@Header("Authorization") String token,
                                @Header("Content-Language") String portal_token);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/payment/bank/get/")
    Call<CommonRes> getBankDetail(@Header("Authorization") String token,
                                @Header("Content-Language") String portal_token);
}
