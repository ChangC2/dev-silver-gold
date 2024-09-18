package seemesave.businesshub.api.product;

import android.text.TextUtils;


import seemesave.businesshub.api.ApiConstants;
import seemesave.businesshub.api.RetrofitClient;
import seemesave.businesshub.application.App;
import seemesave.businesshub.model.common.ApiErrorModel;
import seemesave.businesshub.model.res.CommonRes;
import seemesave.businesshub.model.res.ProductListRes;
import seemesave.businesshub.model.res.ProductRes;
import seemesave.businesshub.utils.GsonUtils;

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

public class ProductApi {
    public static void getPromotionProduct(boolean is_mine, boolean is_wish, boolean is_liked, int supplier_id, String keyword, int offset, int page_size) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Product.class)
                .getProductListForPromotion(App.getToken(), App.getPortalToken(), is_mine, is_wish, is_liked, supplier_id, keyword, offset, page_size)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        ProductListRes res = new ProductListRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, ProductListRes.class);
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
                        EventBus.getDefault().post(new ProductListRes());
                    }
                });

    }
    public static void getProductDetail(String barcode) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Product.class)
                .getProductDetail(App.getToken(), App.getPortalToken(), barcode)
                .enqueue(new Callback<ProductRes>() {
                    @Override
                    public void onResponse(Call<ProductRes> call, Response<ProductRes> result) {
                        ProductRes res = new ProductRes();
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
                    public void onFailure(Call<ProductRes> call, Throwable t) {
                        EventBus.getDefault().post(new ProductRes());
                    }
                });

    }
    public static void getPortalProductDetail(String barcode) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Product.class)
                .getPortalProductDetail(App.getToken(), App.getPortalToken(), barcode)
                .enqueue(new Callback<ProductRes>() {
                    @Override
                    public void onResponse(Call<ProductRes> call, Response<ProductRes> result) {
                        ProductRes res = new ProductRes();
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
                    public void onFailure(Call<ProductRes> call, Throwable t) {
                        EventBus.getDefault().post(new ProductRes());
                    }
                });

    }
    public static void getAllProduct(int offset, int page_size, String keyword) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Product.class)
                .getAllProduct(App.getToken(), App.getPortalToken(), keyword, offset, page_size)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        ProductListRes res = new ProductListRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, ProductListRes.class);
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
                        EventBus.getDefault().post(new ProductListRes());
                    }
                });

    }
}
