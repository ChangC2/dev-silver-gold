package com.mobile.seemesave.view.checkout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.mobile.seemesave.R;
import com.mobile.seemesave.api.ApiConstants;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.cart.DeliverCartActivity;
import com.mobile.seemesave.view.cart.ShoppingCartActivity;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentActivity extends BaseActivity {

    @BindView(R.id.imgBack)
    ImageView imgBack;

    WebView myWebView;
    Context mContext;

    boolean redirected1 = false;
    boolean redirected2 = false;
    boolean redirected3 = false;
    boolean deliver = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        deliver = getIntent().getBooleanExtra("deliver", false);
        initUI();
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        imgBack.setOnClickListener(view->onBack());
        myWebView = findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        showLoadingDialog();

        myWebView.setWebViewClient(new MyWebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri url = request.getUrl();
                if(url.toString().contains("seemesave.com") && url.toString().contains("&success")) {
                    Log.e("urlurl:", "bbbbbbbbbb");
                    onOrders();
                }else if (url.toString().contains("seemesave.com") && url.toString().contains("&error")){
                    finish();
                }
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

//        myWebView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
//                String url = myWebView.getUrl();
//                if(url.contains("seemesave.com") && url.contains("&success")) {
//                    if (!redirected1){
//                        redirected1 = true;
//                        Log.e("urlurl:", "aaaaaaaaaaaaa");
//                        onOrders();
//                    }
//                }else if (url.contains("seemesave.com") && url.contains("&error")){
//                    if (!redirected2){
//                        redirected2 = true;
//                        finish();
//                    }
//                }
//            }
//        });

        int id = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            return;
        }
        String token = G.pref.getString("token" , "");
        String loadUrl = String.format(Locale.US, deliver ? ApiConstants.SiteURL + "payment/click-deliver/?token=%s&id=%s" : ApiConstants.SiteURL + "payment/click-collect/?token=%s&id=%s", token, String.valueOf(id));
        myWebView.loadUrl(loadUrl);
        mContext = this;
    }

    private void onOrders() {
        Intent intent;
        if (deliver) {
            intent = new Intent(this, DeliverCartActivity.class);
        } else {
            intent = new Intent(this, ShoppingCartActivity.class);
        }
        intent.putExtra("after_pay", true);
        startActivity(intent);
        finish();
    }

    private void onBack(){
        finish();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideLoadingDialog();
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            //You can add some custom functionality here
            myWebView.setVisibility(View.GONE);
            hideLoadingDialog();
        }

    }
}