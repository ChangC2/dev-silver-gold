package com.mobile.seemesave.api.promotion;

import static com.mobile.seemesave.utils.ParseUtil.parseOnePromotion;

import android.text.TextUtils;

import com.mobile.seemesave.api.ApiConstants;
import com.mobile.seemesave.api.RetrofitClient;
import com.mobile.seemesave.model.common.ApiErrorModel;
import com.mobile.seemesave.model.common.PromotionOneModel;
import com.mobile.seemesave.model.res.CommonRes;
import com.mobile.seemesave.model.res.ProductListRes;
import com.mobile.seemesave.model.res.ProductRes;
import com.mobile.seemesave.model.res.ProductSingleRes;
import com.mobile.seemesave.model.res.PromotionRes;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromotionApi {
    public static void getPromotionDetail(int id) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Promotion.class)
                .getPromotionDetail(G.getToken(), id)
                .enqueue(new Callback<PromotionRes>() {
                    @Override
                    public void onResponse(Call<PromotionRes> call, Response<PromotionRes> result) {
                        PromotionRes res = new PromotionRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                res.setData(result.body().getData());
                                PromotionOneModel model = parseOnePromotion(res.getData());
                                res.setPromotionData(model);
                            } else {
                                res.setMessage(result.body().getMessage());
                            }
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
                        res.setStatus(result.body().isStatus());
                        EventBus.getDefault().post(res);
                    }

                    @Override
                    public void onFailure(Call<PromotionRes> call, Throwable t) {
                        EventBus.getDefault().post(new PromotionRes());
                    }
                });

    }
    public static void getPromotionSingleProducts(int promotion_id, int category_id, int offset, int page_size) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Promotion.class)
                .getPromotionSingleProducts(G.getToken(), promotion_id, category_id, offset, page_size)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        ProductSingleRes res = new ProductSingleRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, ProductSingleRes.class);
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
                        res.setStatus(result.body().isStatus());
                        EventBus.getDefault().post(res);
                    }

                    @Override
                    public void onFailure(Call<CommonRes> call, Throwable t) {
                        EventBus.getDefault().post(new ProductSingleRes());
                    }
                });

    }

}