package com.cam8.mmsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.ViewCompat;

import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cam8.mmsapp.network.APIServerUtil;
import com.cam8.mmsapp.utils.DateUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

public class GanttChartActivity extends BaseActivity {

    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Intent intent = getIntent();

        String accountId = appSettings.getCustomerID();
        String machineName = appSettings.getMachineName();
        String date = DateUtil.toStringFormat_21(new Date()); ;

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(String.format("%s(%s)", machineName, date.replace("_", ":")));
        }

        webView = findViewById(R.id.webview);

        ViewCompat.setTransitionName(webView, VIEW_NAME_HEADER_IMAGE);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onReceivedTitle(WebView view, String title) {
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("page", url);

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                if (weakReference !=null && weakReference.get() != null) {
                    hideProgressDialog();
                    showAlert("Couldn't load gantt chart data, please check your login information");
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                hideProgressDialog();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if (weakReference !=null && weakReference.get() != null) {
                    showProgressDialog();
                }
            }
        });

        webView.setBackgroundColor(Color.BLACK);

        //"https://slymms.com/ganttDetail2/sm_ks/Makino/03_20_2020"
        //"https://api.slymms.com/ganttDetail2/sm_ks/Makino/03_20_2020"
        //String ganttChartUrl = String.format("https://api.slymms.com/ganttDetail2/%s/%s/%s", accountId, getEncodedString(machineName), date);
        //https://api.slymms.com/ganttDetail3/?customerId=visser&machineId=NHX6300+%231&date=10_21_2020

        //String ganttChartUrl = String.format("https://api.slymms.com/ganttDetail3?customerId=%s&machineId=%s&date=%s", getEncodedString(accountId), getEncodedString(machineName), getEncodedString(date));
        String ganttChartUrl = String.format("%s?customerId=%s&machineId=%s&date=%s",
                APIServerUtil.getUrl(mContext, APIServerUtil.APIType.gantt, "ganttDetail3"),
                getEncodedString(accountId),
                getEncodedString(machineName),
                getEncodedString(date));

        webView.loadUrl(ganttChartUrl);
        webView.requestFocus();

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        webView.destroy();
    }

    private String getEncodedString(String value) {
        String newVal = value;
        try{
            newVal = URLEncoder.encode(value,"UTF-8");
        } catch(UnsupportedEncodingException e){ // Catch the encoding exception
            e.printStackTrace();
        }
        return newVal;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
