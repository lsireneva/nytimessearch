package com.example.luba.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.luba.nytimessearch.R;
import com.example.luba.nytimessearch.adapters.ArticleRecyclerViewAdapter;
import com.example.luba.nytimessearch.adapters.OnArticleRecyclerViewAdapterListener;
import com.example.luba.nytimessearch.models.Article;
import com.example.luba.nytimessearch.network.ArticleRestClient;
import com.example.luba.nytimessearch.network.ArticlesCallback;
import com.example.luba.nytimessearch.network.Error;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements OnArticleRecyclerViewAdapterListener{

    EditText etQuery;
    //GridView gvResults;
    Button btnSearch;
    private RecyclerView mRecyclerViewArticles;
    ArrayList<Article> articles;
    int page = 0;
    private ArticleRecyclerViewAdapter mRecyclerArticleArrayAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupViews();
    }

    public void setupViews() {
        mRecyclerViewArticles = (RecyclerView) findViewById(R.id.rvArticles);
        mRecyclerViewArticles.setHasFixedSize(true);
        // Define a layout for RecyclerView
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerViewArticles.setLayoutManager(mLayoutManager);
        etQuery = (EditText) findViewById(R.id.etQuery);
        //gvResults = (GridView) findViewById(R.id.gvResults);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        mRecyclerArticleArrayAdapter = new ArticleRecyclerViewAdapter(articles, this);
        mRecyclerViewArticles.setAdapter(mRecyclerArticleArrayAdapter);


        //hood up listener for grid click
        /*mRecyclerViewArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //create intent to display an article
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                //get the article to display
                Article article = articles.get(position);
                //pass in  that the article into intent
                intent.putExtra("article", article);
                //launch the activity
                startActivity(intent);
            }
        });*/

    }
    private void loadArticles(int page, String query) {
        ArticleRestClient.getArticles(page, query, new ArticlesCallback(){
            @Override
            public void onSuccess(ArrayList<Article> articles) {
                //SearchActivity.this.articles = new ArrayList<>();
                SearchActivity.this.articles.addAll(articles);
                //SearchActivity.this.page = page;
                mRecyclerArticleArrayAdapter.notifyDataSetChanged(SearchActivity.this.articles);
            }

            @Override
            public void onError(Error error) {

            }


        });
    }

    public void onArticleSearch(View view) {

        String query = etQuery.getText().toString();
        loadArticles(0, query);
        //Toast.makeText(this, "Searching for ="+query, Toast.LENGTH_LONG).show();
        //AsyncHttpClient client = new AsyncHttpClient();
        //String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        /*RequestParams params = new RequestParams();
        params.put("api-key", "fe88e156392e4d298664ccffeb5485e1");
        params.put("page", 0);
        params.put("q", query);


        ArticleRestClient.getArticles(new ArticlesCallback(){
            @Override
            public void onSuccess(ArrayList<Article> articles) {
                SearchActivity.this.articles = new ArrayList<>();
                SearchActivity.this.articles.addAll(articles);
                mRecyclerArticleArrayAdapter.notifyDataSetChanged(SearchActivity.this.articles);
            }

            @Override
            public void onError(Error error) {
                error.printStackTrace();
            }
        });

        /*client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                //Log.d ("DEBUG", response.toString());

                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response"). getJSONArray("docs");
                    mRecyclerViewArticles.addAll(Article.fromJsonArray(articleJsonResults));
                    //articleArrayAdapter.notifyDataSetChanged();
                    //Log.d ("DEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });*/
}



    @Override
    public void selectArticle(Article article) {
        //create intent to display an article
        Intent intent = new Intent(this, ArticleActivity.class);
        //get the article to display
        //Article article = articles.get(position);
        //pass in  that the article into intent
        intent.putExtra("article", article);
        //launch the activity
        startActivity(intent);

    }
}

