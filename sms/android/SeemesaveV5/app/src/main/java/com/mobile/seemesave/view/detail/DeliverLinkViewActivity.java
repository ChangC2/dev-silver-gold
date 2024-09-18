package com.mobile.seemesave.view.detail;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeliverLinkViewActivity  extends BaseActivity {


    @BindView(R.id.webview)
    WebView webView;

    String link = "";

    DeliverLinkViewActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_deliver_link_view);
        activity = this;
        link = getIntent().getStringExtra("link");
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                showLoadingDialog();
                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                hideLoadingDialog();
            }
        });

        webView.loadUrl(link);


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}