package com.codepath.tiago.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiago on 9/19/17.
 */

public class Article implements Serializable {

    String mWebUrl;
    String mHeadline;
    String mSnippet;
    String mThumbnail;

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public String getmSnippet() { return mSnippet; }

    public String getThumbnail() {
        return String.format("http://www.nytimes.com/%s", mThumbnail);
    }

    public Article(JSONObject jsonObject) {
        try {

            this.mWebUrl = jsonObject.getString("web_url");
            this.mHeadline = jsonObject.getJSONObject("headline").getString("main");
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
}
