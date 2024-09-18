package com.mobile.seemesave.api.common;

import android.text.TextUtils;

import com.mobile.seemesave.api.ApiConstants;
import com.mobile.seemesave.api.RetrofitClient;
import com.mobile.seemesave.model.common.ApiErrorModel;
import com.mobile.seemesave.model.res.CommonRes;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonApi {
    public static void getAddress() {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Common.class)
                .getAddress()
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        CommonRes res = new CommonRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                res = result.body();
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                try {
                                    JSONObject obj = new JSONObject(dataStr);
                                    double lat = obj.getDouble("latitude");
                                    double lng = obj.getDouble("longitude");
                                    try {
                                        G.user.setLatitude(lat);
                                        G.user.setLongitude(lng);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
                    }

                    @Override
                    public void onFailure(Call<CommonRes> call, Throwable t) {

                    }
                });

    }

}
