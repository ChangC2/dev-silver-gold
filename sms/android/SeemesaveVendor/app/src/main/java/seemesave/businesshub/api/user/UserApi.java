package seemesave.businesshub.api.user;

import android.text.TextUtils;

import seemesave.businesshub.api.ApiConstants;
import seemesave.businesshub.api.RetrofitClient;
import seemesave.businesshub.model.common.ApiErrorModel;
import seemesave.businesshub.model.req.LoginReq;
import seemesave.businesshub.model.req.SignupReq;
import seemesave.businesshub.model.res.CommonRes;
import seemesave.businesshub.model.res.LoginRes;
import seemesave.businesshub.model.res.SignupRes;
import seemesave.businesshub.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserApi {
    public static void doLogin(LoginReq req) {

        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(User.class)
                .login(req)
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        LoginRes res = new LoginRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, LoginRes.class);
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
                        EventBus.getDefault().post(new LoginRes());
                    }
                });

    }

    public static void createPortal(SignupReq req) {

        RetrofitClient.getClient(ApiConstants.BASE_URL)
                .create(User.class)
                .createPortal(req.getUser_id(), req.getDest_portal(), req.getNew_logo(), req.getName(), req.getEmail(), req.getPhone(), req.getAddress(), req.getWebsite())
                .enqueue(new Callback<CommonRes>() {
                    @Override
                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
                        SignupRes res = new SignupRes();
                        if (result.isSuccessful()) {
                            if (result.body().isStatus()) {
                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
                                res = GsonUtils.getInstance().fromJson(dataStr, SignupRes.class);
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
                        EventBus.getDefault().post(new SignupRes());
                    }
                });

    }

//    public static void sendOtp(SignupReq req) {
//
//        RetrofitClient.getClient(ApiConstants.BASE_URL)
//                .create(com.mobile.seemesave.api.user.User.class)
//                .sendOTP(req)
//                .enqueue(new Callback<CommonRes>() {
//                    @Override
//                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
//                        LoginRes res = new LoginRes();
//                        if (result.isSuccessful()) {
//                            if (result.body().isStatus()) {
//                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
//                                res = GsonUtils.getInstance().fromJson(dataStr, LoginRes.class);
//                            } else {
//                                res.setMessage(result.body().getMessage());
//                            }
//                            res.setStatus(result.body().isStatus());
//                        } else if (result.errorBody() != null) {
//                            ResponseBody errorBody = result.errorBody();
//                            if (result.code() == 200) {
//                                ApiErrorModel errorModel = null;
//                                try {
//                                    if (TextUtils.isEmpty(errorBody.string()) || errorBody.string().equalsIgnoreCase("Internal Server Error")) {
//                                        res.setMessage("Something went wrong");
//                                    } else {
//                                        try {
//                                            errorModel = GsonUtils.getInstance().fromJson(errorBody.string(), ApiErrorModel.class);
//                                            res.setMessage(errorModel.getMessage());
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                res.setMessage("Something went wrong");
//                            }
//                        }
//                        EventBus.getDefault().post(res);
//                    }
//
//                    @Override
//                    public void onFailure(Call<CommonRes> call, Throwable t) {
//                        EventBus.getDefault().post(new LoginRes());
//                    }
//                });
//
//    }
//    public static void getUserInfo() {
//        RetrofitClient.getClient(ApiConstants.BASE_URL)
//                .create(com.mobile.seemesave.api.user.User.class)
//                .getUserInfo(G.getToken())
//                .enqueue(new Callback<CommonRes>() {
//                    @Override
//                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
//                        UserRes res = new UserRes();
//                        if (result.isSuccessful()) {
//                            if (result.body().isStatus()) {
//                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
//                                res = GsonUtils.getInstance().fromJson(dataStr, UserRes.class);
//                            } else {
//                                res.setMessage(result.body().getMessage());
//                            }
//                            res.setStatus(result.body().isStatus());
//
//                        } else if (result.errorBody() != null) {
//                            ResponseBody errorBody = result.errorBody();
//                            if (result.code() == 200) {
//                                ApiErrorModel errorModel = null;
//                                try {
//                                    if (TextUtils.isEmpty(errorBody.string()) || errorBody.string().equalsIgnoreCase("Internal Server Error")) {
//                                        res.setMessage("Something went wrong");
//                                    } else {
//                                        try {
//                                            errorModel = GsonUtils.getInstance().fromJson(errorBody.string(), ApiErrorModel.class);
//                                            res.setMessage(errorModel.getMessage());
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                res.setMessage("Something went wrong");
//                            }
//                        }
//                        EventBus.getDefault().post(res);
//                    }
//
//                    @Override
//                    public void onFailure(Call<CommonRes> call, Throwable t) {
//                        EventBus.getDefault().post(new UserRes());
//                    }
//                });
//
//    }
//    public static void getUserBaseInfo() {
//        RetrofitClient.getClient(ApiConstants.BASE_URL)
//                .create(com.mobile.seemesave.api.user.User.class)
//                .getUserBaseInfo(G.getToken())
//                .enqueue(new Callback<CommonRes>() {
//                    @Override
//                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
//                        UserBaseInfoRes res = new UserBaseInfoRes();
//                        if (result.isSuccessful()) {
//                            if (result.body().isStatus()) {
//                                String dataStr = GsonUtils.getInstance().toJson(result.body().getData());
//                                res = GsonUtils.getInstance().fromJson(dataStr, UserBaseInfoRes.class);
//                            } else {
//                                res.setMessage(result.body().getMessage());
//                            }
//                            res.setStatus(result.body().isStatus());
//
//                        } else if (result.errorBody() != null) {
//                            ResponseBody errorBody = result.errorBody();
//                            if (result.code() == 200) {
//                                ApiErrorModel errorModel = null;
//                                try {
//                                    if (TextUtils.isEmpty(errorBody.string()) || errorBody.string().equalsIgnoreCase("Internal Server Error")) {
//                                        res.setMessage("Something went wrong");
//                                    } else {
//                                        try {
//                                            errorModel = GsonUtils.getInstance().fromJson(errorBody.string(), ApiErrorModel.class);
//                                            res.setMessage(errorModel.getMessage());
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                res.setMessage("Something went wrong");
//                            }
//                        }
//                        EventBus.getDefault().post(res);
//                    }
//
//                    @Override
//                    public void onFailure(Call<CommonRes> call, Throwable t) {
//                        EventBus.getDefault().post(new UserBaseInfoRes());
//                    }
//                });
//
//    }
//    public static void getAlertCount() {
//        RetrofitClient.getClient(ApiConstants.BASE_URL)
//                .create(com.mobile.seemesave.api.user.User.class)
//                .getAlertCount(G.getToken())
//                .enqueue(new Callback<AlertCountRes>() {
//                    @Override
//                    public void onResponse(Call<AlertCountRes> call, Response<AlertCountRes> result) {
//                        AlertCountRes res = new AlertCountRes();
//                        if (result.isSuccessful()) {
//                            if (result.body().isStatus()) {
//                                res = result.body();
//                            } else {
//                                res.setMessage(result.body().getMessage());
//                            }
//                            res.setStatus(result.body().isStatus());
//                        } else if (result.errorBody() != null) {
//                            ResponseBody errorBody = result.errorBody();
//                            if (result.code() == 200) {
//                                ApiErrorModel errorModel = null;
//                                try {
//                                    if (TextUtils.isEmpty(errorBody.string()) || errorBody.string().equalsIgnoreCase("Internal Server Error")) {
//                                        res.setMessage("Something went wrong");
//                                    } else {
//                                        try {
//                                            errorModel = GsonUtils.getInstance().fromJson(errorBody.string(), ApiErrorModel.class);
//                                            res.setMessage(errorModel.getMessage());
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                res.setMessage("Something went wrong");
//                            }
//                        }
//                        EventBus.getDefault().post(res);
//                    }
//
//                    @Override
//                    public void onFailure(Call<AlertCountRes> call, Throwable t) {
//                        EventBus.getDefault().post(new AlertCountRes());
//                    }
//                });
//
//    }
//    public static void updateLocation(JsonObject json) {
//        RetrofitClient.getClient(ApiConstants.BASE_URL)
//                .create(com.mobile.seemesave.api.user.User.class)
//                .updateUserInfo(G.getToken(), json)
//                .enqueue(new Callback<CommonRes>() {
//                    @Override
//                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
//                        CommonRes res = new CommonRes();
//                        if (result.isSuccessful()) {
//                            if (result.body().isStatus()) {
//                                res = result.body();
//                                res.setPage("update_location");
//                            } else {
//                                res.setMessage(result.body().getMessage());
//                            }
//                            res.setStatus(result.body().isStatus());
//                        } else if (result.errorBody() != null) {
//                            ResponseBody errorBody = result.errorBody();
//                            if (result.code() == 200) {
//                                ApiErrorModel errorModel = null;
//                                try {
//                                    if (TextUtils.isEmpty(errorBody.string()) || errorBody.string().equalsIgnoreCase("Internal Server Error")) {
//                                        res.setMessage("Something went wrong");
//                                    } else {
//                                        try {
//                                            errorModel = GsonUtils.getInstance().fromJson(errorBody.string(), ApiErrorModel.class);
//                                            res.setMessage(errorModel.getMessage());
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                res.setMessage("Something went wrong");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<CommonRes> call, Throwable t) {
//                    }
//                });
//
//    }
//    public static void updateProfile(JsonObject json) {
//        RetrofitClient.getClient(ApiConstants.BASE_URL)
//                .create(com.mobile.seemesave.api.user.User.class)
//                .updateUserProfile(G.getToken(), json)
//                .enqueue(new Callback<CommonRes>() {
//                    @Override
//                    public void onResponse(Call<CommonRes> call, Response<CommonRes> result) {
//                        CommonRes res = new CommonRes();
//                        if (result.isSuccessful()) {
//                            if (result.body().isStatus()) {
//                                res = result.body();
//                            } else {
//                                res.setMessage(result.body().getMessage());
//                            }
//                            res.setStatus(result.body().isStatus());
//                        } else if (result.errorBody() != null) {
//                            ResponseBody errorBody = result.errorBody();
//                            if (result.code() == 200) {
//                                ApiErrorModel errorModel = null;
//                                try {
//                                    if (TextUtils.isEmpty(errorBody.string()) || errorBody.string().equalsIgnoreCase("Internal Server Error")) {
//                                        res.setMessage("Something went wrong");
//                                    } else {
//                                        try {
//                                            errorModel = GsonUtils.getInstance().fromJson(errorBody.string(), ApiErrorModel.class);
//                                            res.setMessage(errorModel.getMessage());
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                res.setMessage("Something went wrong");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<CommonRes> call, Throwable t) {
//                    }
//                });
//
//    }
}
