package com.codepath.tiago.nytimessearch.network;

import android.content.Context;
import android.util.Log;

import com.codepath.tiago.nytimessearch.adapters.ArticlesAdapter;
import com.codepath.tiago.nytimessearch.models.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by tiago on 9/23/17.
 */

public class ApiCallHandlers {

    private final String TAG = ApiCallHandlers.class.toString();

    int mStatusCode;
    Header[] mHeaders;
    JSONObject mResponse;
    String mResponseString;
    Throwable mThrowable;

    Context mContext;
    ArticlesAdapter mAdapter;
    List<Article> mArticles;

    ApiCallHandlers(ApiCallHandlersBuilder builder) {
        this.mStatusCode = builder.mStatusCode;
        this.mHeaders = builder.mHeaders;
        this.mResponse = builder.mResponse;
        this.mResponseString = builder.mResponseString;
        this.mThrowable = builder.mThrowable;
        this.mContext = builder.mContext;
        this.mAdapter = builder.mAdapter;
        this.mArticles = builder.mArticles;
    }

    public void onApiCallSuccess() {
        JSONArray articleJsonResults = null;

        try {
            articleJsonResults = mResponse.getJSONObject("response").getJSONArray("docs");

            // record this value before making any changes to the existing list.
            int curSize = mAdapter.getItemCount();

            // replace this line with wherever you get new records.
            // update the existing list.
            List<Article> newArticles = Article.fromJsonArray(articleJsonResults);
            mArticles.addAll(newArticles);

            // curSize should represent the first element that got added.
            // newItems.size() represents the itemCount.
            mAdapter.notifyItemRangeInserted(curSize, newArticles.size());

        } catch(JSONException e) {
            Log.e(TAG, "Error parsing JSON response in context: " + mContext.toString());
            e.printStackTrace();
        }
    }

    public void onApiCallFailure() {
        if (mResponseString != null) {
            Log.e(TAG, "status: " + mStatusCode + " response: " + mResponseString);
        } else {
            Log.e(TAG, "status: " + mStatusCode + " JSONresponse: " + mResponse.toString());
        }

        // Check the connectivity.
        // Displays an alert dialog if there is no connection to inform the user.
        ConnectivityChecker connectivityChecker = new ConnectivityChecker(mContext);
        connectivityChecker.checkConnectivity();

        // Print stack trace of throwable.
        mThrowable.printStackTrace();
    }
}
