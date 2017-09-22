package com.codepath.tiago.nytimessearch.network;

import com.codepath.tiago.nytimessearch.models.Filter;
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

    public void getArticles(String query, Filter filter, JsonHttpResponseHandler handler) {

        String url = getApiUrl(this.relative_url);

        // Get all the params that will be included in the request according to
        // the query and the filter settings. We get the first page (number 0).
        RequestParams params = getRequestParams(query, 0, filter);

        // Make the API call.
        client.get(url, params, handler);

    }

    public void getArticlesNextPage(String query, int page, Filter filter, JsonHttpResponseHandler handler) {
        String url = getApiUrl(this.relative_url);

        RequestParams params = getRequestParams(query, page, filter);

        // Make the API call.
        client.get(url, params, handler);
    }

    private RequestParams getRequestParams(String query, int page, Filter filter) {

        RequestParams params = new RequestParams();
        try {
            params.put("q", URLEncoder.encode(query, "utf-8"));

            if (filter != null) {
                // Translate filter settings into request params.
                params.put("begin_date", filter.getBeginDateString());
                if (filter.getSortOrder() == Filter.SortValues.OLDEST) {
                    params.put("sort", "oldest");
                } else if (filter.getSortOrder() == Filter.SortValues.NEWEST) {
                    params.put("sort", "newest");
                }

                if (filter.getNewsDeskValueSize() > 0) {
                    String values = "";
                    if (filter.hasNewsDeskValue(Filter.NewsDeskValues.ARTS)) {
                        values += "\"Arts\", ";
                    }
                    if (filter.hasNewsDeskValue(Filter.NewsDeskValues.FASHION_STYLE)) {
                        values += "\"Fashion & Style\"";
                    }
                    if (filter.hasNewsDeskValue(Filter.NewsDeskValues.SPORTS)) {
                        values += "\"Sports\"";
                    }
                    params.put("fq", "news_desk:(" + values + ")");
                }
            }

            params.put("fl", "web_url, multimedia, news_desk, headline, snippet");
            params.put("page", page);
            params.put("api-key", NYT_API_KEY);
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return params;
    }
}
