package com.codepath.tiago.nytimessearch;

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

    String webUrl;
    String headline;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return String.format("http://www.nytimes.com/%s", thumbnail);
    }

    String thumbnail;

    public Article(JSONObject jsonObject) {
        try {

            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if (multimedia.length() > 0) {
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbnail = multimediaJson.getString("url");
            } else {
                this.thumbnail = "";
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
