package com.mobile.seemesave.view.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.TagAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.utils.G;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddMediaActivity extends BaseActivity {
    @BindView(R.id.imgBack)
    ImageView imgBack;

    @BindView(R.id.imgPlay)
    ImageView imgPlay;

    @BindView(R.id.imgMedia)
    ImageView imgMedia;


    @BindView(R.id.editCaption)
    EditText editCaption;

    @BindView(R.id.editDescription)
    EditText editDescription;

    @BindView(R.id.txtLeft)
    TextView txtLeft;

    @BindView(R.id.txtAddLocation)
    TextView txtAddLocation;

    @BindView(R.id.imgDelLocation)
    ImageView imgDelLocation;

    @BindView(R.id.recyclerTag)
    RecyclerView recyclerTag;

    @BindView(R.id.btnTagFriend)
    TextView btnTagFriend;
    @BindView(R.id.btnTagStore)
    TextView btnTagStore;
    @BindView(R.id.btnTagBrand)
    TextView btnTagBrand;
    @BindView(R.id.btnTagProduct)
    TextView btnTagProduct;

    @BindView(R.id.btnSave)
    Button btnSave;

    @BindView(R.id.li_add_location)
    RelativeLayout li_add_location;

    @BindView(R.id.li_tag)
    RelativeLayout li_tag;

    private static int REQUEST_CODE = 100;
    private String mediaPath = "";
    private String contentType = "image/jpeg";
    private String mType = "";
    private int type = 0;

    double lat = 0.0, lon = 0.0;
    String fullAddress = "";
    String addressId = "";
    ArrayList<String> tags = new ArrayList<>();

    private TagAdapter tagAdapter;

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1000;
    private static final int SELECT_TAG_REQUEST_CODE = 1001;

    public static void start(Context context) {
        Intent intent = new Intent(context, AddMediaActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_media);
        G.setLightFullScreen(this);
        ButterKnife.bind(this);

        imgBack.setOnClickListener(view->onBack());
        imgMedia.setOnClickListener(view->onClickMedia());

        editDescription.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                txtLeft.setText(String.format("[%d words left]", 200 - editDescription.getText().length()));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key), Locale.US);
        }

        txtAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountry("ZA")
                        .build(AddMediaActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        imgDelLocation.setVisibility(View.GONE);
        imgDelLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtAddLocation.setText(R.string.txt_add_location);
                fullAddress = "";
                addressId = "";
                lat = 0.0;
                lon = 0.0;
                imgDelLocation.setVisibility(View.GONE);
            }
        });

        btnTagFriend.setOnClickListener(view->addTag(1));
        btnTagStore.setOnClickListener(view->addTag(2));
        btnTagBrand.setOnClickListener(view->addTag(3));
        btnTagProduct.setOnClickListener(view->addTag(4));

        btnSave.setOnClickListener(view->onSaveMedia());
        mediaPath = getIntent().getStringExtra("mediaPath");
        mType = getIntent().getStringExtra("mType");
        contentType = getIntent().getStringExtra("contentType");
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void addTag(int type){
        Intent intent = new Intent(AddMediaActivity.this, SearchTagStringActivity.class);
        intent.putExtra("tag_string", "");
        intent.putExtra("type", type);
        startActivityForResult(intent, SELECT_TAG_REQUEST_CODE);
    }

    private void initUI() {
        if (G.isNetworkAvailable(AddMediaActivity.this)) {
            li_add_location.setVisibility(View.VISIBLE);
            li_tag.setVisibility(View.VISIBLE);
        } else {
            li_add_location.setVisibility(View.GONE);
            li_tag.setVisibility(View.GONE);
        }
        imgPlay.setVisibility(View.GONE);

        if (TextUtils.isEmpty(mediaPath)) {
            return;
        }

        imgPlay.setVisibility(mType.equalsIgnoreCase("video") ? View.VISIBLE : View.GONE);

        Glide.with(this)
                .load(new File(mediaPath)) // Uri of the picture
                .into(imgMedia);
    }

    private void setRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerTag.setLayoutManager(linearLayoutManager);
        recyclerTag.setItemAnimator(new DefaultItemAnimator());
        tagAdapter = new TagAdapter(this, tags, null);
        recyclerTag.setAdapter(tagAdapter);
    }

    private void onBack(){
        finish();
    }

    private void onClickMedia(){
//        if (mType.equalsIgnoreCase("video")){
//            Intent intent = new Intent(this, VideoPreviewActivity.class);
//            intent.putExtra("url", mediaPath);
//            startActivity(intent);
//        }else{
//            Intent intent = new Intent(this, PhotoDetailsActivity.class);
//            ArrayList<String> imgList = new ArrayList<>();
//            imgList.add(mediaPath);
//            intent.putStringArrayListExtra(G.PHOTO_DETAILS_IMAGES, imgList);
//            intent.putExtra(G.PHOTO_DETAILS_SELPOS, 0);
//            startActivity(intent);
//        }
    }

    private void onSaveMedia(){
        if (TextUtils.isEmpty(mediaPath)){
            Toast.makeText(this, R.string.missing_param, Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(editCaption.getText().toString()) ||
                TextUtils.isEmpty(editDescription.getText().toString())){
            Toast.makeText(this, R.string.missing_param, Toast.LENGTH_LONG).show();
            return;
        }
        apiCallForUploadMedia();
    }

    void apiCallForUploadMedia() {
        String token = G.pref.getString("token" , "");
        String user_id = G.pref.getString("user_id" , "");
        String tagStr = "";
        if (tags.size() > 0){
            tagStr = tags.get(0);
            for (int i=1;i<tags.size();i++){
                tagStr += tags.get(i);
            }
        }
        if (G.isNetworkAvailable(AddMediaActivity.this)) {
            File file = new File(mediaPath);

            showLoadingDialog();
            Ion.with(this)
                    .load(G.UploadPostUrl)
                    .addHeader("Authorization", "Bearer " + token)
                    .setMultipartFile("sub_media", contentType, file)
                    .setMultipartParameter("title", editCaption.getText().toString())
                    .setMultipartParameter("description", editDescription.getText().toString())
                    .setMultipartParameter("tag_list", tagStr)
                    .setMultipartParameter("place_id_list", addressId)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            hideLoadingDialog();
                            if (e != null){
                                Toast.makeText(AddMediaActivity.this, R.string.connection_fail, Toast.LENGTH_LONG).show();
                            }else{
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.getBoolean("status")){
                                        Toast.makeText(AddMediaActivity.this, R.string.post_success, Toast.LENGTH_LONG).show();
                                        finish();
                                    }else{
                                        Toast.makeText(AddMediaActivity.this, R.string.connection_fail, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException jsonException) {
                                    Toast.makeText(AddMediaActivity.this, R.string.connection_fail, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        } else {
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                fullAddress = place.getAddress();
                addressId = place.getId();
                lat = place.getLatLng().latitude;
                lon = place.getLatLng().longitude;
                txtAddLocation.setText(fullAddress);
                imgDelLocation.setVisibility(View.VISIBLE);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            } else if (resultCode == RESULT_CANCELED) {
            }
            return;
        }else if (requestCode == SELECT_TAG_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String tagStr = data.getStringExtra("tag_string");
                tags.add(tagStr);
                setRecycler();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}