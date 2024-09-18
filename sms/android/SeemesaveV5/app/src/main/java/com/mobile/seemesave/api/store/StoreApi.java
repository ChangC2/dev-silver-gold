package com.mobile.seemesave.api.store;

import static com.mobile.seemesave.utils.ParseUtil.parsePromotion;

import android.text.TextUtils;

import com.mobile.seemesave.api.ApiConstants;
import com.mobile.seemesave.api.RetrofitClient;
import com.mobile.seemesave.model.common.ApiErrorModel;
import com.mobile.seemesave.model.common.PromotionOneModel;
import com.mobile.seemesave.model.res.StorePromotionRes;
import com.mobile.seemesave.model.res.CommonRes;
import com.mobile.seemesave.model.res.StoreRes;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreApi {
    public static void getStoreList(int offset, int page_size, String keyword) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Store.class)
                .getStoreList(G.getToken(), offset, page_size, keyword)
                .enqueue(new Callback<StoreRes>() {
                    @Override
                    public void onResponse(Call<StoreRes> call, Response<StoreRes> result) {
                        StoreRes res = new StoreRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                res = result.body();
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
    public static void getStoreListFromLocation(double lat, double lng) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Store.class)
                .getStoreListFromLocation(G.getToken(), lat, lng)
                .enqueue(new Callback<StoreRes>() {
                    @Override
                    public void onResponse(Call<StoreRes> call, Response<StoreRes> result) {
                        StoreRes res = new StoreRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                res = result.body();
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
    public static void getStorePromotion(int id) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Store.class)
                .getStorePromotion(G.getToken(), id)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        StorePromotionRes res = new StorePromotionRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, StorePromotionRes.class);
//                                try {
//                                    JSONObject jsonObject = new JSONObject(dataStr);
//                                    JSONArray jsonArray = new JSONArray(jsonObject.getString("promotionList"));
//                                    ArrayList<PromotionOneModel> collectList = new ArrayList<>();
//                                    ArrayList<PromotionOneModel> deliverList = new ArrayList<>();
//                                    collectList.clear();
//                                    deliverList.clear();
//                                    if (jsonArray.length() != 0) {
//                                        collectList = parsePromotion(jsonArray).get("collect");
//                                        deliverList = parsePromotion(jsonArray).get("deliver");
//                                    }
//                                    res.setCollectList(collectList);
//                                    res.setDeliverList(deliverList);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
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
                        EventBus.getDefault().post(new StorePromotionRes());
                    }
                });

    }

    public static void getStoreListFromCategory(int id) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Store.class)
                .getStoreListFromCategory(G.getToken(), id)
                .enqueue(new Callback<StoreRes>() {
                    @Override
                    public void onResponse(Call<StoreRes> call, Response<StoreRes> result) {
                        StoreRes res = new StoreRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                res = result.body();
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
