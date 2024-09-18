package seemesave.businesshub.api.store;


import android.text.TextUtils;


import seemesave.businesshub.api.ApiConstants;
import seemesave.businesshub.api.RetrofitClient;
import seemesave.businesshub.application.App;
import seemesave.businesshub.model.common.ApiErrorModel;
import seemesave.businesshub.model.res.CommonRes;
import seemesave.businesshub.model.res.StoreListRes;
import seemesave.businesshub.model.res.StoreRes;
import seemesave.businesshub.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreApi {
    public static void getStoreList(int offset, int page_size, String keyword) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Store.class)
                .getStoreList(App.getToken(), App.getPortalToken(), offset, page_size, keyword)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        StoreListRes res = new StoreListRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, StoreListRes.class);
                            } else {
                                res.setMessage(result.body().getMessage());
                            }
                            res.setStatus(result.body().isStatus());
                        } else if (result.errorBody() != null) {
                            ResponseBody errorBody = result.errorBody();
                            if (result.code() == 200) {
                                ApiErrorModel errorModel = null;
                                try {
                                    if (TextUtils.isEmpty(errorBody.string()) || errorBody.string().equalsIgnoreCase("Internal Server Error")) {
                                        res.setMessage("Something went wrong");
                                    } else {
                                        try {
                                            errorModel = GsonUtils.getInstance().fromJson(errorBody.string(), ApiErrorModel.class);
                                            res.setMessage(errorModel.getMessage());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                res.setMessage("Something went wrong");
                            }
                        }
                        EventBus.getDefault().post(res);
                    }

                    @Override
                    public void onFailure(Call<CommonRes> call, Throwable t) {
                        EventBus.getDefault().post(new StoreListRes());
                    }
                });

    }
    public static void getStoreDetail(int id) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Store.class)
                .getStoreDetail(App.getToken(), App.getPortalToken(), id)
                .enqueue(new Callback<StoreRes>() {
                    @Override
                    public void onResponse(Call<StoreRes> call, Response<StoreRes> result) {
                        StoreRes res = new StoreRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                res.setData(result.body().getData());
                            } else {
                                res.setMessage(result.body().getMessage());
                            }
                            res.setStatus(result.body().isStatus());
                        } else if (result.errorBody() != null) {
                            ResponseBody errorBody = result.errorBody();
                            if (result.code() == 200) {
                                ApiErrorModel errorModel = null;
                                try {
                                    if (TextUtils.isEmpty(errorBody.string()) || errorBody.string().equalsIgnoreCase("Internal Server Error")) {
                                        res.setMessage("Something went wrong");
                                    } else {
                                        try {
                                            errorModel = GsonUtils.getInstance().fromJson(errorBody.string(), ApiErrorModel.class);
                                            res.setMessage(errorModel.getMessage());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                res.setMessage("Something went wrong");
                            }
                        }
                        EventBus.getDefault().post(res);
                    }

                    @Override
                    public void onFailure(Call<StoreRes> call, Throwable t) {
                        EventBus.getDefault().post(new StoreRes());
                    }
                });

    }
}
