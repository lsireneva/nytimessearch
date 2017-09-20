package com.example.luba.nytimessearch.network;

import com.example.luba.nytimessearch.models.Article;

import java.util.ArrayList;

/**
 * Created by luba on 9/20/17.
 */

public interface ArticlesCallback {
    void onSuccess(ArrayList<Article> articles);

    void onError(Error error);
}
