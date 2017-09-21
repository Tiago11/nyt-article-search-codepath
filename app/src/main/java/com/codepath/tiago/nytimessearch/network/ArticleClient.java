package com.codepath.tiago.nytimessearch.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by tiago on 9/20/17.
 */

public class ArticleClient {

    private final String NYT_API_KEY = "8337eda794df48cbb6ae2fd466c77561";
    private final String NYT_API_BASE_URL = "http://api.nytimes.com/svc/search/v2/";

    private final String relative_url = "articlesearch.json";
    AsyncHttpClient client;

    public ArticleClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relative_url) {
        return NYT_API_BASE_URL + relative_url;
    }

    public void getArticles(String query, JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl(this.relative_url);

            RequestParams params = new RequestParams();
            params.put("api-key", NYT_API_KEY);
            params.put("page", 0);
            params.put("q", URLEncoder.encode(query, "utf-8"));

            client.get(url, params, handler);

        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
