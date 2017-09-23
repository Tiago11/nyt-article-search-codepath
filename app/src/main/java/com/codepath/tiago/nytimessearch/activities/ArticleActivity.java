package com.codepath.tiago.nytimessearch.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codepath.tiago.nytimessearch.R;
import com.codepath.tiago.nytimessearch.models.Article;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        // Enable up button on the ActionBar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get article from the intent.
        Article article = (Article) getIntent().getSerializableExtra("article");

        // Set the WebView to display the article and then load it.
        setupWebView(article);
    }

    /*
     * Sets the necessary properties to show the article in an embedded WebView.
     * Loads the article into the WebView.
     */
    private void setupWebView(Article article) {
        // Get a reference to the WebView.
        WebView webView = (WebView) findViewById(R.id.wvArticle);
        // Configure related browser settings.
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        // Configure the client to use when opening URLs.
        webView.setWebViewClient(new MyArticleBrowser());

        // Load the URL.
        webView.loadUrl(article.getWebUrl());
    }

    // Manages the behavior when URLs are loaded.
    private class MyArticleBrowser extends WebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }
}
