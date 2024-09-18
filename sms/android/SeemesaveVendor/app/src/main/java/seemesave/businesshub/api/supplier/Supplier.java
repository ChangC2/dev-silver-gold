package seemesave.businesshub.api.supplier;

import seemesave.businesshub.model.res.CommonRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Supplier {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("vendor/suppliers/all/")
    Call<CommonRes> getSupplierList(@Header("Authorization") String token,
                                    @Header("Content-Language") String portal_token,
                                    @Query("offset") int offset,
                                    @Query("page_size") int page_size,
                                    @Query("keyword") String keyword);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("supplier/supplier_follow/all/")
    Call<CommonRes> getVendorList(@Header("Authorization") String token,
                                    @Header("Content-Language") String portal_token,
                                    @Query("offset") int offset,
                                    @Query("page_size") int page_size,
                                    @Query("keyword") String keyword);

}
