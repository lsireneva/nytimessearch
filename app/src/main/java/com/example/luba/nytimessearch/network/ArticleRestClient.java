package com.example.luba.nytimessearch.network;

import android.util.Log;

import com.example.luba.nytimessearch.models.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by luba on 9/20/17.
 */

public class ArticleRestClient {

    private static String BASE_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    private static final String API_KEY = "fe88e156392e4d298664ccffeb5485e1";

    private static AsyncHttpClient client = new AsyncHttpClient();


    /*public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if (params == null) {
            params = new RequestParams();
        }
        params.put("api_key", API_KEY);

        client.get(BASE_URL, params, responseHandler);
    }*/

    /*private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }*/


    public static void getArticles(int page, String query, final ArticlesCallback callback) {

        Log.d ("DEBUG", "getArticles()");
        Log.d ("DEBUG", "page="+page);
        Log.d ("DEBUG", "query="+query);

        RequestParams params = new RequestParams();
        params.put("api_key", API_KEY);
        params.put("page", String.valueOf(page));
        params.put("query", String.valueOf(query));


        client.get(BASE_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.d ("DEBUG", response.toString());

                JSONArray articleJsonResults = null;
                ArrayList<Article> articles = new ArrayList<>();

                try {

                    //int page = response.getInt("page");
                    //String query=response.getString("query");
                    //Log.d ("DEBUG", "page="+page);
                    //Log.d ("DEBUG", "query="+query);
                    articleJsonResults = response.getJSONObject("response"). getJSONArray("docs");
                    Log.d ("DEBUG", articleJsonResults.toString());
                    for (int i = 0; i < articleJsonResults.length(); i++) {

                        articles.add(new Article(articleJsonResults.getJSONObject(i)));
                    }
                    callback.onSuccess(articles);

                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError(new Error(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                callback.onError(new Error(throwable.getMessage()));
            }


        });

    }
}
