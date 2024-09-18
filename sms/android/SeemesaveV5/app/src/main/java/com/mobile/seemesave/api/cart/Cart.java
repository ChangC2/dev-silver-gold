package com.mobile.seemesave.api.cart;

import com.mobile.seemesave.model.req.CartReq;
import com.mobile.seemesave.model.req.CheckoutDeliverReq;
import com.mobile.seemesave.model.req.CheckoutShoppingReq;
import com.mobile.seemesave.model.res.CommonRes;
import com.mobile.seemesave.model.res.DeliverCartOrderRes;
import com.mobile.seemesave.model.res.ShoppingCartOrderRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Cart {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("shopping-cart/add/")
    Call<CommonRes> addShoppingCart(@Header("Authorization") String token,
                                    @Body CartReq req);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("shopping-cart/all/")
    Call<ShoppingCartOrderRes> getShoppingCartAll(@Header("Authorization") String token);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("shopping-cart/get-by-id/")
    Call<CommonRes> getShoppingCartById(@Header("Authorization") String token,
                                        @Query("id") int cart_id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("shopping-cart/get-exclusive/")
    Call<CommonRes> getExclusiveDealsFromShoppingCart(@Header("Authorization") String token,
                                        @Query("cart_id") int cart_id);
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("shopping-cart/get-available-time/")
    Call<CommonRes> getAvailableTime(@Header("Authorization") String token,
                                           @Query("cart_id") int cart_id);
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("shopping-cart/check-out/")
    Call<CommonRes> checkoutShopping(@Header("Authorization") String token,
                                     @Body CheckoutShoppingReq req);
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("shopping-cart/history/")
    Call<CommonRes> getShoppingHistory(@Header("Authorization") String token,
                                      @Query("offset") int offset,
                                      @Query("page_size") int limit);



    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("deliver-cart/add/")
    Call<CommonRes> addDeliverCart(@Header("Authorization") String token,
                                    @Body CartReq req);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("deliver-cart/all/")
    Call<DeliverCartOrderRes> getDeliverCartAll(@Header("Authorization") String token);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("deliver-cart/get-cart-base-info/")
    Call<CommonRes> getDeliverCartBaseInfo(@Header("Authorization") String token,
                                        @Query("cart_id") int cart_id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("deliver-cart/get-by-id/")
    Call<CommonRes> getDeliverCartById(@Header("Authorization") String token,
                                           @Query("id") int cart_id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("deliver-cart/check-out/")
    Call<CommonRes> checkoutDeliver(@Header("Authorization") String token,
                                   @Body CheckoutDeliverReq req);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @DELETE("deliver-cart/remove/")
    Call<CommonRes> deleteDeliverCart(@Header("Authorization") String token,
                                      @Field("id") String id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("deliver-cart/history/")
    Call<CommonRes> getDeliverHistory(@Header("Authorization") String token,
                                       @Query("offset") int offset,
                                       @Query("page_size") int limit);

}
