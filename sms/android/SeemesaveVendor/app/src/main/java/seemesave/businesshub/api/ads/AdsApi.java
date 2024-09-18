package seemesave.businesshub.api.ads;

import android.text.TextUtils;

import seemesave.businesshub.api.ApiConstants;
import seemesave.businesshub.api.RetrofitClient;
import seemesave.businesshub.application.App;
import seemesave.businesshub.model.common.ApiErrorModel;
import seemesave.businesshub.model.req.PromotionReq;
import seemesave.businesshub.model.res.AdsAdvertListRes;
import seemesave.businesshub.model.res.AdsBestDealListRes;
import seemesave.businesshub.model.res.AdsExclusiveDealListRes;
import seemesave.businesshub.model.res.AdsFeaturedBrandListRes;
import seemesave.businesshub.model.res.AdsFeaturedStoreListRes;
import seemesave.businesshub.model.res.AdsListRes;
import seemesave.businesshub.model.res.AdsMissDealListRes;
import seemesave.businesshub.model.res.AdsPostListRes;
import seemesave.businesshub.model.res.AdsStoryListRes;
import seemesave.businesshub.model.res.CommonRes;
import seemesave.businesshub.model.res.PostRes;
import seemesave.businesshub.model.res.PromotionRes;
import seemesave.businesshub.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdsApi {

    public static void createPromotion(PromotionReq req) {

        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Ads.class)
                .createAds(App.getToken(), App.getPortalToken(),
                        req.getPromotion_type(),
                        req.getTitle(),
                        req.getDescription(),
                        req.getStart_date(),
                        req.getEnd_date(),
                        req.getStore_id(),
                        req.getTotal_money(),
                        req.getCurrency(),
                        req.getIs_private(),
                        req.getIs_deliver(),
                        req.getNeed_data(),
                        req.getTag_list(),
                        req.getPlace_id_list(),
                        req.getSingleString(),
                        req.getComboString(),
                        req.getBuy1get1String(),
                        req.getPay_singleString(),
                        req.getPay_comboString(),
                        req.getPay_buy1get1String(),
                        req.getSub_media())
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        CommonRes res = new CommonRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, CommonRes.class);
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
                        EventBus.getDefault().post(new CommonRes());
                    }
                });

    }
    public static void getAdsList(String promotion_type, String keyword, String from_date, String end_date, int offset, int page_size) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Ads.class)
                .getAdsList(App.getToken(), App.getPortalToken(), promotion_type, from_date, end_date, keyword, offset, page_size)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        AdsListRes res = new AdsListRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, AdsListRes.class);
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
                        EventBus.getDefault().post(new AdsListRes());
                    }
                });

    }
    public static void getAdsDetail(String promotion_type, int id) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Ads.class)
                .getAdsDetail(App.getToken(), App.getPortalToken(), promotion_type, id)
                .enqueue(new Callback<PromotionRes>() {
                    @Override
                    public void onResponse(Call<PromotionRes> call, Response<PromotionRes> result) {
                        PromotionRes res = new PromotionRes();
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
                    public void onFailure(Call<PromotionRes> call, Throwable t) {
                        EventBus.getDefault().post(new PromotionRes());
                    }
                });

    }
    public static void getPostList(String keyword, String from_date, String end_date, int offset, int page_size) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Ads.class)
                .getPostList(App.getToken(), App.getPortalToken(), from_date, end_date, keyword, offset, page_size)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        AdsPostListRes res = new AdsPostListRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, AdsPostListRes.class);
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
                        EventBus.getDefault().post(new AdsListRes());
                    }
                });

    }
    public static void getPostDetail(int id) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Ads.class)
                .getPostDetail(App.getToken(), App.getPortalToken(), id)
                .enqueue(new Callback<PostRes>() {
                    @Override
                    public void onResponse(Call<PostRes> call, Response<PostRes> result) {
                        PostRes res = new PostRes();
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
                    public void onFailure(Call<PostRes> call, Throwable t) {
                        EventBus.getDefault().post(new PostRes());
                    }
                });

    }
    public static void getFeaturedBrandDetail(int id) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Ads.class)
                .getFeaturedBrandDetail(App.getToken(), App.getPortalToken(), id)
                .enqueue(new Callback<PostRes>() {
                    @Override
                    public void onResponse(Call<PostRes> call, Response<PostRes> result) {
                        PostRes res = new PostRes();
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
                    public void onFailure(Call<PostRes> call, Throwable t) {
                        EventBus.getDefault().post(new PostRes());
                    }
                });

    }
    public static void getAdvertDetail(int id) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Ads.class)
                .getAdvertDetail(App.getToken(), App.getPortalToken(), id)
                .enqueue(new Callback<PostRes>() {
                    @Override
                    public void onResponse(Call<PostRes> call, Response<PostRes> result) {
                        PostRes res = new PostRes();
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
                    public void onFailure(Call<PostRes> call, Throwable t) {
                        EventBus.getDefault().post(new PostRes());
                    }
                });

    }
    public static void getStoryDetail(int id) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Ads.class)
                .getStoryDetail(App.getToken(), App.getPortalToken(), id)
                .enqueue(new Callback<PostRes>() {
                    @Override
                    public void onResponse(Call<PostRes> call, Response<PostRes> result) {
                        PostRes res = new PostRes();
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
                    public void onFailure(Call<PostRes> call, Throwable t) {
                        EventBus.getDefault().post(new PostRes());
                    }
                });

    }
    public static void getAdsBestDealList(String promotion_type, String keyword, String from_date, String end_date, int offset, int page_size) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Ads.class)
                .getAdsList(App.getToken(), App.getPortalToken(), promotion_type, from_date, end_date, keyword, offset, page_size)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        AdsBestDealListRes res = new AdsBestDealListRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, AdsBestDealListRes.class);
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
                        EventBus.getDefault().post(new AdsBestDealListRes());
                    }
                });

    }
    public static void getAdsMissDealList(String promotion_type, String keyword, String from_date, String end_date, int offset, int page_size) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Ads.class)
                .getAdsList(App.getToken(), App.getPortalToken(), promotion_type, from_date, end_date, keyword, offset, page_size)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        AdsMissDealListRes res = new AdsMissDealListRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, AdsMissDealListRes.class);
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
                        EventBus.getDefault().post(new AdsMissDealListRes());
                    }
                });

    }
    public static void getAdsExclusiveDealList(String promotion_type, String keyword, String from_date, String end_date, int offset, int page_size) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Ads.class)
                .getAdsList(App.getToken(), App.getPortalToken(), promotion_type, from_date, end_date, keyword, offset, page_size)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        AdsExclusiveDealListRes res = new AdsExclusiveDealListRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, AdsExclusiveDealListRes.class);
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
                        EventBus.getDefault().post(new AdsExclusiveDealListRes());
                    }
                });

    }
    public static void getFeaturedStores(String keyword, String from_date, String end_date, int offset, int page_size) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Ads.class)
                .getFeaturedStoreList(App.getToken(), App.getPortalToken(), from_date, end_date, keyword, offset, page_size)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        AdsFeaturedStoreListRes res = new AdsFeaturedStoreListRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, AdsFeaturedStoreListRes.class);
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
                        EventBus.getDefault().post(new AdsFeaturedStoreListRes());
                    }
                });

    }
    public static void getStoryList(String keyword, String from_date, String end_date, int offset, int page_size) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Ads.class)
                .getStoryList(App.getToken(), App.getPortalToken(), from_date, end_date, keyword, offset, page_size)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        AdsStoryListRes res = new AdsStoryListRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, AdsStoryListRes.class);
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
                        EventBus.getDefault().post(new AdsStoryListRes());
                    }
                });

    }
    public static void getAdsFeaturedBrandList(String promotion_type, String keyword, String from_date, String end_date, int offset, int page_size) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Ads.class)
                .getFeaturedBrandList(App.getToken(), App.getPortalToken(), promotion_type, from_date, end_date, keyword, offset, page_size)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        AdsFeaturedBrandListRes res = new AdsFeaturedBrandListRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, AdsFeaturedBrandListRes.class);
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
                        EventBus.getDefault().post(new AdsFeaturedBrandListRes());
                    }
                });

    }
    public static void getAdsAdvertList(String promotion_type, String keyword, String from_date, String end_date, int offset, int page_size) {
        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(Ads.class)
                .getAdvertList(App.getToken(), App.getPortalToken(), promotion_type, from_date, end_date, keyword, offset, page_size)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        AdsAdvertListRes res = new AdsAdvertListRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, AdsAdvertListRes.class);
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
                        EventBus.getDefault().post(new AdsAdvertListRes());
                    }
                });

    }
//    File promotionImg = new File(mediaPath);
//    RequestBody imgFile = RequestBody.create(MediaType.parse("multipart/form-data"), promotionImg);
//    MultipartBody.Part uploadFilePart = MultipartBody.Part.createFormData("sub_media", promotionImg.getName(), imgFile);

//        PromotionReq req = new PromotionReq();
//        req.setSub_media(uploadFilePart);
//        req.setPromotion_type(RequestBody.create(MediaType.parse("multipart/form-data"), "VendorPromotion"));
//        req.setTitle(RequestBody.create(MediaType.parse("multipart/form-data"), title));
//        req.setDescription(RequestBody.create(MediaType.parse("multipart/form-data"), description));
//        req.setStart_date(RequestBody.create(MediaType.parse("multipart/form-data"), start_date));
//        req.setEnd_date(RequestBody.create(MediaType.parse("multipart/form-data"), end_date));
//        req.setStore_id(RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(selStore)));
//        req.setTotal_money(RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(total_money)));
//        req.setCurrency(RequestBody.create(MediaType.parse("multipart/form-data"), selCurrency));
//        req.setIs_private(RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(is_private)));
//        req.setIs_deliver(RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(isDeliver)));
//        req.setNeed_data(RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(need_data)));
//        req.setTag_list(RequestBody.create(MediaType.parse("multipart/form-data"), tag_list));
//        req.setPlace_id_list(RequestBody.create(MediaType.parse("multipart/form-data"), place_id_list));
//        req.setSingleString(RequestBody.create(MediaType.parse("multipart/form-data"), singleString));
//        req.setComboString(RequestBody.create(MediaType.parse("multipart/form-data"), comboString));
//        req.setBuy1get1String(RequestBody.create(MediaType.parse("multipart/form-data"), buy1get1String));
//        req.setPay_singleString(RequestBody.create(MediaType.parse("multipart/form-data"), pay_singleString));
//        req.setPay_comboString(RequestBody.create(MediaType.parse("multipart/form-data"), pay_comboString));
//        req.setPay_buy1get1String(RequestBody.create(MediaType.parse("multipart/form-data"), pay_buy1get1String));
//        PromotionApi.createPromotion(req);
}
