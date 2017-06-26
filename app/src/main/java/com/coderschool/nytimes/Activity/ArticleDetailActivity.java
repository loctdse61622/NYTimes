package com.coderschool.nytimes.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.coderschool.nytimes.Model.Article;
import com.coderschool.nytimes.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 6/25/2017.
 */

public class ArticleDetailActivity extends AppCompatActivity {
    @BindView(R.id.webView)
    WebView webView;
    private Article currentArticle;
    private String articleUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_web_view);
        ButterKnife.bind(this);

        currentArticle = getIntent().getParcelableExtra("article");
        articleUrl = currentArticle.getWebUrl();
        WebSettings webSettings = webView.getSettings();
        setUpWebSettings(webSettings);
        webView.loadUrl(articleUrl);
    }

    private void setUpWebSettings(WebSettings mWebSettings) {
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setJavaScriptEnabled(true);
    }
}
