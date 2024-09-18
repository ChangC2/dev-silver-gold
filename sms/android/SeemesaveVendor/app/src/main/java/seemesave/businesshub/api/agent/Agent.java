package seemesave.businesshub.api.agent;

import seemesave.businesshub.model.res.CommonRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Agent {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("common/agent-mgr/all/")
    Call<CommonRes> getAgentList(@Header("Authorization") String token,
                                    @Header("Content-Language") String portal_token,
                                    @Query("offset") int offset,
                                    @Query("page_size") int page_size,
                                    @Query("keyword") String keyword);

}
