package com.codepath.tiago.nytimessearch.network;

import android.content.Context;

import com.codepath.tiago.nytimessearch.adapters.ArticlesAdapter;
import com.codepath.tiago.nytimessearch.models.Article;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by tiago on 9/23/17.
 */

public class ApiCallHandlersBuilder {
    int mStatusCode;
    Header[] mHeaders;
    JSONObject mResponse;
    String mResponseString;
    Throwable mThrowable;

    Context mContext;
    ArticlesAdapter mAdapter;
    List<Article> mArticles;

    public static ApiCallHandlersBuilder apiCallHandlers() {
        return new ApiCallHandlersBuilder();
    }

    public ApiCallHandlersBuilder withStatusCode(int statusCode) {
        this.mStatusCode = statusCode;
        return this;
    }

    public ApiCallHandlersBuilder withHeaders(Header[] headers) {
        this.mHeaders = headers;
        return this;
    }

    public ApiCallHandlersBuilder withResponse(JSONObject response) {
        this.mResponse = response;
        return this;
    }

    public ApiCallHandlersBuilder withResponseString(String responseString) {
        this.mResponseString = responseString;
        return this;
    }

    public ApiCallHandlersBuilder withThrowable(Throwable throwable) {
        this.mThrowable = throwable;
        return this;
    }

    public ApiCallHandlersBuilder withContext(Context context) {
        this.mContext = context;
        return this;
    }

    public ApiCallHandlersBuilder withAdapter(ArticlesAdapter adapter) {
        this.mAdapter = adapter;
        return this;
    }

    public ApiCallHandlersBuilder withArticles(List<Article> articles) {
        this.mArticles = articles;
        return this;
    }

    public ApiCallHandlers build() {
        return new ApiCallHandlers(this);
    }
}
