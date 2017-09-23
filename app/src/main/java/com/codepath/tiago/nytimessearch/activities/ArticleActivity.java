package com.codepath.tiago.nytimessearch.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codepath.tiago.nytimessearch.R;
import com.codepath.tiago.nytimessearch.models.Article;

import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {

    // Tag for logging.
    private final String TAG = ArticleActivity.class.toString();

    WebView wvArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        // Enable up button on the ActionBar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get article from the intent.
        Article article = (Article) Parcels.unwrap(getIntent().getParcelableExtra("article"));

        // Set the WebView to display the article and then load it.
        setupWebView(article);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_article, menu);
        // Locate MenuItem with ShareActionProvider.
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch reference to the share action provider.
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_TEXT, wvArticle.getUrl());

        shareActionProvider.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Sets the necessary properties to show the article in an embedded WebView.
     * Loads the article into the WebView.
     */
    private void setupWebView(Article article) {
        // Get a reference to the WebView.
        wvArticle = (WebView) findViewById(R.id.wvArticle);
        // Configure related browser settings.
        wvArticle.getSettings().setLoadsImagesAutomatically(true);
        wvArticle.getSettings().setJavaScriptEnabled(true);
        wvArticle.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wvArticle.getSettings().setUseWideViewPort(true);
        wvArticle.getSettings().setLoadWithOverviewMode(true);
        // Configure the client to use when opening URLs.
        wvArticle.setWebViewClient(new MyArticleBrowser());

        // Load the URL.
        wvArticle.loadUrl(article.getWebUrl());
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
