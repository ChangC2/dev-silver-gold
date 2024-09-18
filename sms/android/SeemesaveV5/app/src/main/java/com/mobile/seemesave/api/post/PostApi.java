package com.mobile.seemesave.api.post;

import static com.mobile.seemesave.utils.ParseUtil.parseFeed;
import static com.mobile.seemesave.utils.ParseUtil.parsePost;

import android.text.TextUtils;

import com.mobile.seemesave.api.ApiConstants;
import com.mobile.seemesave.api.RetrofitClient;
import com.mobile.seemesave.model.common.ApiErrorModel;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.common.PostModel;
import com.mobile.seemesave.model.res.CommonRes;
import com.mobile.seemesave.model.res.FeedRes;
import com.mobile.seemesave.model.res.PostRes;
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

public class PostApi {
    public static void getRelatedMediaList(int post_id, int offset, int page_size) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Post.class)
                .getRelatedMediaList(G.getToken(), post_id, offset, page_size)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        FeedRes res = new FeedRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, FeedRes.class);
                                try {
                                    JSONObject jsonObject = new JSONObject(dataStr);
                                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                                    ArrayList<FeedModel> feedList = new ArrayList<>();
                                    feedList.clear();
                                    if (jsonArray.length() != 0) {
                                        feedList = parseFeed(jsonArray);
                                    }
                                    res.setPostList(feedList);
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
                        EventBus.getDefault().post(res);
                    }

                    @Override
                    public void onFailure(Call<CommonRes> call, Throwable t) {
                        EventBus.getDefault().post(new FeedRes());
                    }
                });
    }
    public static void getMyPostList(int offset, int page_size) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Post.class)
                .getMyPostList(G.getToken(), offset, page_size)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        PostRes res = new PostRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, PostRes.class);
                                try {
                                    JSONObject jsonObject = new JSONObject(dataStr);
                                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                                    ArrayList<FeedModel> postList = new ArrayList<>();
                                    postList.clear();
                                    if (jsonArray.length() != 0) {
                                        postList = parseFeed(jsonArray);
                                    }
                                    res.setPostList(postList);
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
                        EventBus.getDefault().post(res);
                    }

                    @Override
                    public void onFailure(Call<CommonRes> call, Throwable t) {
                        EventBus.getDefault().post(new PostRes());
                    }
                });
    }
}
