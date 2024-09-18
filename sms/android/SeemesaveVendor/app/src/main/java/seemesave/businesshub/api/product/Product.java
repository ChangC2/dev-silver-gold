package seemesave.businesshub.api.product;


import seemesave.businesshub.model.res.CommonRes;
import seemesave.businesshub.model.res.ProductRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Product {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/product/search/")
    Call<CommonRes> getProductListForPromotion(@Header("Authorization") String token,
                                               @Header("Content-Language") String portal_token,
                                               @Query("is_mine") boolean is_mine,
                                               @Query("is_wish") boolean is_wish,
                                               @Query("is_liked") boolean is_liked,
                                               @Query("supplier_id") int supplier_id,
                                               @Query("keyword") String keyword,
                                               @Query("offset") int offset,
                                               @Query("page_size") int page_size);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/product/get-by-barcode/")
    Call<ProductRes> getProductDetail(@Header("Authorization") String token,
                                      @Header("Content-Language") String portal_token,
                                      @Query("barcode") String barcode);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/product/portal/get-by-barcode/")
    Call<ProductRes> getPortalProductDetail(@Header("Authorization") String token,
                                      @Header("Content-Language") String portal_token,
                                      @Query("barcode") String barcode);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/product/portal/all/")
    Call<CommonRes> getAllProduct(@Header("Authorization") String token,
                                               @Header("Content-Language") String portal_token,
                                               @Query("keyword") String keyword,
                                               @Query("offset") int offset,
                                               @Query("page_size") int page_size);
}
