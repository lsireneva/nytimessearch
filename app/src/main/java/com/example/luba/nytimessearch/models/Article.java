package com.example.luba.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by luba on 9/19/17.
 */

public class Article implements Parcelable{
    String webURL;
    String headLine;
    String thumbNail;

    protected Article(Parcel in) {
        webURL = in.readString();
        headLine = in.readString();
        thumbNail = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getWeURL() {
        return webURL;
    }

    public String getHeadLine() {
        return headLine;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setTitle(String headLine) {
        this.headLine = headLine;
    }


    public Article (JSONObject jsonObject) throws JSONException{
        if (jsonObject.has("headLine")) setTitle(jsonObject.getJSONObject("headline").getString("main"));
        try {
            this.webURL = jsonObject.getString("web_url");
            this.headLine = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if (multimedia.length()>0) {
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbNail = "http://www.nytimes.com/"+multimediaJson.getString("url");
                //Log.d ("DEBUG", "multimediaJson.getString(\"url\")="+multimediaJson.getString("url"));
            } else {
                this.thumbNail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

   /* public static ArrayList<Article> fromJsonArray(JSONArray array) {
        ArrayList<Article> results = new ArrayList<>();

        for (int i=0; i<array.length(); i++) {
            try {
                results.add(new Article(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(webURL);
        parcel.writeString(headLine);
        parcel.writeString(thumbNail);
    }
}
