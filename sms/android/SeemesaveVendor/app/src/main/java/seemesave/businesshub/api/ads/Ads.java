package seemesave.businesshub.api.ads;

import seemesave.businesshub.model.res.CommonRes;
import seemesave.businesshub.model.res.PostRes;
import seemesave.businesshub.model.res.ProductRes;
import seemesave.businesshub.model.res.PromotionRes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Ads {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("common/promotion/create/")
    @Multipart
    Call<CommonRes> createAds(@Header("Authorization") String token,
                              @Header("Content-Language") String portal_token,
                              @Part("promotion_type") RequestBody promotion_type,
                              @Part("title") RequestBody title,
                              @Part("description") RequestBody description,
                              @Part("start_date") RequestBody start_date,
                              @Part("end_date") RequestBody end_date,
                              @Part("store_id") RequestBody store_id,
                              @Part("total_money") RequestBody total_money,
                              @Part("currency") RequestBody currency,
                              @Part("is_private") RequestBody is_private,
                              @Part("is_deliver") RequestBody is_deliver,
                              @Part("need_data") RequestBody need_data,
                              @Part("tag_list") RequestBody tag_list,
                              @Part("place_id_list") RequestBody place_id_list,
                              @Part("singleString") RequestBody singleString,
                              @Part("comboString") RequestBody comboString,
                              @Part("buy1get1String") RequestBody buy1get1String,
                              @Part("pay_singleString") RequestBody pay_singleString,
                              @Part("pay_comboString") RequestBody pay_comboString,
                              @Part("pay_buy1get1String") RequestBody pay_buy1get1String,
                              @Part MultipartBody.Part sub_media
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/promotion/get/")
    Call<CommonRes> getAdsList(@Header("Authorization") String token,
                               @Header("Content-Language") String portal_token,
                               @Query("promotion_type") String promotion_type,
                               @Query("keyword") String keyword,
                               @Query("from_date") String from_date,
                               @Query("to_date") String to_date,
                               @Query("offset") int offset,
                               @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/promotion/get-by-id/")
    Call<PromotionRes> getAdsDetail(@Header("Authorization") String token,
                                    @Header("Content-Language") String portal_token,
                                    @Query("promotion_type") String promotion_type,
                                    @Query("id") int id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/post/get/")
    Call<CommonRes> getPostList(@Header("Authorization") String token,
                               @Header("Content-Language") String portal_token,
                               @Query("keyword") String keyword,
                               @Query("from_date") String from_date,
                               @Query("to_date") String to_date,
                               @Query("offset") int offset,
                               @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/post/get/")
    Call<PostRes> getPostDetail(@Header("Authorization") String token,
                                @Header("Content-Language") String portal_token,
                                @Query("id") int id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/featured-brand/get/")
    Call<PostRes> getFeaturedBrandDetail(@Header("Authorization") String token,
                                @Header("Content-Language") String portal_token,
                                @Query("id") int id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/home-advert/get/")
    Call<PostRes> getAdvertDetail(@Header("Authorization") String token,
                                         @Header("Content-Language") String portal_token,
                                         @Query("id") int id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/featured-store/get/")
    Call<CommonRes> getFeaturedStoreList(@Header("Authorization") String token,
                                @Header("Content-Language") String portal_token,
                                @Query("keyword") String keyword,
                                @Query("from_date") String from_date,
                                @Query("to_date") String to_date,
                                @Query("offset") int offset,
                                @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/story/get/")
    Call<CommonRes> getStoryList(@Header("Authorization") String token,
                                         @Header("Content-Language") String portal_token,
                                         @Query("keyword") String keyword,
                                         @Query("from_date") String from_date,
                                         @Query("to_date") String to_date,
                                         @Query("offset") int offset,
                                         @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/story/get/")
    Call<PostRes> getStoryDetail(@Header("Authorization") String token,
                                @Header("Content-Language") String portal_token,
                                @Query("id") int id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/featured-brand/get/")
    Call<CommonRes> getFeaturedBrandList(@Header("Authorization") String token,
                               @Header("Content-Language") String portal_token,
                               @Query("promotion_type") String promotion_type,
                               @Query("keyword") String keyword,
                               @Query("from_date") String from_date,
                               @Query("to_date") String to_date,
                               @Query("offset") int offset,
                               @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/home-advert/get/")
    Call<CommonRes> getAdvertList(@Header("Authorization") String token,
                                         @Header("Content-Language") String portal_token,
                                         @Query("promotion_type") String promotion_type,
                                         @Query("keyword") String keyword,
                                         @Query("from_date") String from_date,
                                         @Query("to_date") String to_date,
                                         @Query("offset") int offset,
                                         @Query("page_size") int page_size);
}
