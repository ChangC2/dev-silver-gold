package com.mobile.seemesave.view.bookmark;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.BookmarkAdapter;
import com.mobile.seemesave.listener.RecyclerClickListener;
import com.mobile.seemesave.listener.pagination.PaginationScrollListener;
import com.mobile.seemesave.model.common.BookmarkModel;
import com.mobile.seemesave.model.res.HomeRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookMarkAddBottomSheet  extends BottomSheetDialogFragment {

    private View mFragView;
    @BindView(R.id.recylerStores)
    RecyclerView recyclerPosts;


    ArrayList<BookmarkModel> data_list = new ArrayList<>();
    private BookmarkAdapter bookmarkCategoryAdapter;

    public int post_id = -1;
    public String query = "";
    private int offset = 0;
    private int limit = 100;
    private boolean isLoading = false;
    private boolean isLast = false;
    private SetBookMarkListener markListener;

    public String tagString = "";
    private String local_book_data = null;
    Activity activity;

    public interface SetBookMarkListener {
        void setBookMark(String cID, int pID);
    }

    public BookMarkAddBottomSheet(SetBookMarkListener listener) {
        this.markListener = listener;
    }

    public static BookMarkAddBottomSheet newInstance(SetBookMarkListener listener) {

        Bundle args = new Bundle();
        BookMarkAddBottomSheet f = new BookMarkAddBottomSheet(listener);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new BottomSheetDialog(getContext(), R.style.BottomSheetStyle);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragView = inflater.inflate(R.layout.dlg_bookmark_sheet, container, false);
        ButterKnife.bind(this, mFragView);
        activity = getActivity();
        try {
            local_book_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "BookmarkCategory", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initView();
        return mFragView;
    }

    private void initView() {
        setRecycler();
        refreshPage();
    }




    private void refreshPage() {
        initPageNationParams();

        if (local_book_data == null) {
            apiCallForGetBookCategory(true);
        } else {
            loadDataFromLocal();
            apiCallForGetBookCategory(false);
        }
    }

    private void loadDataFromLocal() {
        try {
            if (local_book_data != null) {
                JSONArray local_book_array = new JSONArray(local_book_data);
                parseData(local_book_array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initPageNationParams() {
        offset = 0;
        limit = 100;
        isLoading = false;
        isLast = false;
        data_list.clear();
    }


    private void setRecycler() {
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 3, LinearLayoutManager.VERTICAL, false);
        recyclerPosts.setLayoutManager(layoutManager);
        RecyclerClickListener listener = new RecyclerClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (v.getId() == R.id.li_add) {
                    Dialog dialog = new Dialog(activity, R.style.DialogTheme);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    dialog.setContentView(R.layout.dlg_add_bookmark_category);
                    EditText eName = (EditText) dialog.findViewById(R.id.editName);

                    dialog.findViewById(R.id.btnCreate).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = eName.getText().toString();
                            if (TextUtils.isEmpty(name)) {
                                Toast.makeText(activity, R.string.missing_param, Toast.LENGTH_LONG).show();
                                return;
                            }

                            apiCallCreateCategory(name);
                            dialog.dismiss();
                        }
                    });

                    dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                } else if (v.getId() == R.id.rm_image) {
                    apiCallSetBookmark(data_list.get(position).getId());
                }

            }

            @Override
            public void onClick(View v, int position, int type) {
            }
        };
        recyclerPosts.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                offset = offset + limit;
//                apiCallForGetBookCategory();
            }

            @Override
            public boolean isLastPage() {
                return isLast;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        bookmarkCategoryAdapter = new BookmarkAdapter(activity, data_list, listener, "bottom");
        recyclerPosts.setAdapter(bookmarkCategoryAdapter);
    }

    //***************************************//
    //             API Call Method           //
    //***************************************//

    void apiCallSetBookmark(String category_id) {
        markListener.setBookMark(category_id, post_id);
        dismiss();
    }

    void apiCallCreateCategory(String name) {
        if (G.isNetworkAvailable(getContext())) {
            JsonObject json = new JsonObject();
            json.addProperty("name", name);
            String token = G.pref.getString("token", "");
            G.showLoading(activity);
            Ion.with(this)
                    .load("POST", G.CreateBookmarkCategory)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .setJsonObjectBody(json)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            G.hideLoading();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")) {
                                    JSONObject jsonArray = jsonObject.getJSONObject("data");
                                    String category_id = jsonArray.getString("id");
                                    markListener.setBookMark(category_id, post_id);
                                    G.hideSoftKeyboard(activity);
                                    dismiss();
                                } else {
                                    Toast.makeText(activity, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException jsonException) {
                                Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
        }

    }

    private void parseData(JSONArray jsonArray) {
        try {
            if (jsonArray.length() == 0) {
                ArrayList<BookmarkModel> newPosts = new ArrayList<>();
                BookmarkModel item = new BookmarkModel();
                item.setId("");
                item.setName("Add new");
                item.setItem_type("add");
                item.setCover_image("");
                newPosts.add(item);
                bookmarkCategoryAdapter.setDatas(newPosts);
                data_list.clear();
                isLoading = false;
                data_list.addAll(newPosts);
            } else {
                ArrayList<BookmarkModel> newPosts = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    BookmarkModel item = new BookmarkModel();
                    JSONArray thumbnailList = jsonArray.getJSONObject(i).getJSONArray("thumbnailList");
                    item.setId(jsonArray.getJSONObject(i).getString("id"));
                    item.setName(jsonArray.getJSONObject(i).getString("name"));
                    item.setItem_type("main");
                    String cover_image = "";

                    if (thumbnailList.length() > 0) {
                        JSONArray subImageArry = thumbnailList.getJSONObject(0).getJSONArray("SubMedia");
                        if (subImageArry.length() > 0) {
                            if (subImageArry.getJSONObject(0).getString("Media_Type").equalsIgnoreCase("Image")) {
                                cover_image = subImageArry.getJSONObject(0).getString("Media");
                            } else {
                                cover_image = subImageArry.getJSONObject(0).getString("VideoThumb");
                            }
                        }
                    }
                    item.setCover_image(cover_image);
                    newPosts.add(item);
                }
                data_list.clear();
                data_list.addAll(newPosts);

                BookmarkModel new_item = new BookmarkModel();
                new_item.setId("");
                new_item.setName("Add new");
                new_item.setItem_type("add");
                new_item.setCover_image("");

                data_list.add(new_item);

                bookmarkCategoryAdapter.setDatas(data_list);
                isLoading = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void apiCallForGetBookCategory(boolean show_load) {
        if (G.isNetworkAvailable(activity)) {
            String token = G.pref.getString("token", "");
            String url = G.GetBookmarkCategory;
            if (show_load)
                G.showLoading(activity);
            Ion.with(activity)
                    .load(url)
                    .addHeader("Authorization", "Bearer " + token)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if (show_load)
                                G.hideLoading();
                            if (e == null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.getBoolean("status")) {
                                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("data");
                                        DatabaseQueryClass.getInstance().insertData(
                                                G.getUserID(),
                                                "BookmarkCategory",
                                                jsonArray.toString(),
                                                "",
                                                ""
                                        );
                                        parseData(jsonArray);
                                    } else {
                                        data_list = new ArrayList<>();
                                        bookmarkCategoryAdapter.setDatas(data_list);
                                    }
                                } catch (JSONException jsonException) {
                                    data_list = new ArrayList<>();
                                    bookmarkCategoryAdapter.setDatas(data_list);
                                }
                            }
                        }
                    });
        } else {
            Toast.makeText(activity, R.string.msg_offline, Toast.LENGTH_LONG).show();
        }
    }

}
