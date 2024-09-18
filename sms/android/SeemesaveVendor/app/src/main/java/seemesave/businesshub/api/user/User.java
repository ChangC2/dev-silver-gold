package seemesave.businesshub.api.user;

import seemesave.businesshub.model.req.LoginReq;
import seemesave.businesshub.model.res.CommonRes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface User {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("auth/login/")
    Call<CommonRes> login(@Body LoginReq req);

    @POST("auth/create/")
    @Multipart
    Call<CommonRes> createPortal(@Part("user_id") RequestBody user_id,
                                 @Part("dest_portal") RequestBody dest_portal,
                                 @Part MultipartBody.Part new_logo,
                                 @Part("name") RequestBody name,
                                 @Part("email") RequestBody email,
                                 @Part("phone") RequestBody phone,
                                 @Part("address") RequestBody address,
                                 @Part("website") RequestBody website
    );


//
//    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @POST("user/register/")
//    Call<CommonRes> signup(@Body SignupReq req);
//
//    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @PUT("user/register/")
//    Call<CommonRes> sendOTP(@Body SignupReq req);
//
//    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @PUT("user/user-mgr/")
//    Call<CommonRes> updateUserInfo(@Header("Authorization") String token, @Body JsonObject req);
//
//    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @PUT("user/user-mgr/")
//    Call<CommonRes> updateUserProfile(@Header("Authorization") String token, @Body JsonObject req);
//
//    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @GET("user/profile/")
//    Call<CommonRes> getUserInfo(@Header("Authorization") String token);
//
//    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @GET("user/mobile-notifications/")
//    Call<AlertCountRes> getAlertCount(@Header("Authorization") String token);
//
//    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @GET("user/user-base-info/")
//    Call<CommonRes> getUserBaseInfo(@Header("Authorization") String token);
}
