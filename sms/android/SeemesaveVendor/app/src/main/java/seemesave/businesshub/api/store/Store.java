package seemesave.businesshub.api.store;

import seemesave.businesshub.model.res.CommonRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import seemesave.businesshub.model.res.StoreRes;

public interface Store {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("vendor/stores/")
    Call<CommonRes> getStoreList(@Header("Authorization") String token,
                                @Header("Content-Language") String portal_token,
                                @Query("offset") int offset,
                                @Query("page_size") int page_size,
                                @Query("keyword") String keyword);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("vendor/stores/")
    Call<StoreRes> getStoreDetail(@Header("Authorization") String token,
                                    @Header("Content-Language") String portal_token,
                                    @Query("id") int id);

}
