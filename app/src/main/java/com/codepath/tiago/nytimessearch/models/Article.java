package com.codepath.tiago.nytimessearch.models;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiago on 9/19/17.
 */

@Parcel
public class Article {

    // Tag for logging.
    final String TAG = Article.class.toString();

    String mWebUrl;
    String mHeadline;
    String mByline;
    String mSnippet;
    String mThumbnail;

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public String getByline() { return mByline; }

    public String getSnippet() { return mSnippet; }

    public String getThumbnail() {
        if (TextUtils.isEmpty(mThumbnail)) {
            return null;
        }

        return String.format("http://www.nytimes.com/%s", mThumbnail);
    }

    // Empty constructor (for Parceler).
    public Article() {

    }

    // Contructor from a JSON Object.
    public Article(JSONObject jsonObject) {
        try {

            this.mWebUrl = jsonObject.getString("web_url");
            this.mHeadline = jsonObject.getJSONObject("headline").getString("main");
            if (jsonObject.has("byline")) {
                this.mByline = jsonObject.getJSONObject("byline").getString("original");
            } else {
                this.mByline = null;
            }
            this.mSnippet = jsonObject.getString("snippet");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if (multimedia.length() > 0) {
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.mThumbnail = multimediaJson.getString("url");
            } else {
                this.mThumbnail = "";
            }

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    // Static factory method to create a collection of articles from a JSON Array.
    public static List<Article> fromJsonArray(JSONArray array) {
        ArrayList<Article> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {

                results.add(new Article(array.getJSONObject(i)));

            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    // Predicates.
    public boolean hasByline() {
        return (this.mByline != null);
    }
}
