package com.example.luba.nytimessearch.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luba.nytimessearch.R;
import com.example.luba.nytimessearch.adapters.ArticleRecyclerViewAdapter;
import com.example.luba.nytimessearch.fragments.FilterFragment;
import com.example.luba.nytimessearch.models.Article;
import com.example.luba.nytimessearch.network.ArticleRequestParams;
import com.example.luba.nytimessearch.network.ArticleResponse;
import com.example.luba.nytimessearch.network.ArticleRestClient;
import com.example.luba.nytimessearch.network.ArticlesCallback;
import com.example.luba.nytimessearch.network.Error;
import com.example.luba.nytimessearch.utils.EndlessRecyclerViewScrollListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashSet;

import static com.example.luba.nytimessearch.utils.ArticlesConstants.BEGIN_DATE;
import static com.example.luba.nytimessearch.utils.ArticlesConstants.FILTER;
import static com.example.luba.nytimessearch.utils.ArticlesConstants.NEWS_DESK;
import static com.example.luba.nytimessearch.utils.ArticlesConstants.SORT;


public class SearchActivity extends AppCompatActivity implements ArticleRecyclerViewAdapter.OnArticleRecyclerViewAdapterListener, FilterFragment.SaveDialogListener{

    private static final String TAG_LOG = SearchActivity.class.getCanonicalName();

    private RecyclerView mRecyclerViewArticles;
    SharedPreferences filterPrefs;
    String mBeginDate;
    String mSortOrder;
    HashSet<String> mNewsDeskValues;
    ArrayList<Article> mArticles;
    int mPage=0;
    private ArticleRecyclerViewAdapter mRecyclerArticleArrayAdapter;
    StaggeredGridLayoutManager mLayoutManager;
    EndlessRecyclerViewScrollListener mEndlessListener;

    private Toolbar toolbar;
    MenuItem searchItem, filterItem;
    DialogFragment dlgFilter;
    String mFilter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupViews();
    }

    public void getFilterFromSharedPreferences() {
        filterPrefs = getSharedPreferences(FILTER, 0);
        mSortOrder = filterPrefs.getString(SORT, null);
        mBeginDate = filterPrefs.getString(BEGIN_DATE, null);
        mNewsDeskValues = (HashSet<String>) filterPrefs.getStringSet(NEWS_DESK, null);
    }

    public void setupViews() {
        mArticles = new ArrayList<Article>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dlgFilter = new FilterFragment();

        // setup RecyclerView
        mRecyclerViewArticles = (RecyclerView) findViewById(R.id.rvArticles);
        mRecyclerViewArticles.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerViewArticles.setLayoutManager(mLayoutManager);

        mRecyclerArticleArrayAdapter = new ArticleRecyclerViewAdapter(mArticles, this);
        mRecyclerViewArticles.setAdapter(mRecyclerArticleArrayAdapter);

        mEndlessListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d(TAG_LOG, "loadNextPage: " + String.valueOf(page));
                if (page > 100) {
                   Toast.makeText(SearchActivity.this,getString(R.string.error_loading),Toast.LENGTH_LONG).show();

                } else {
                    loadArticles(page);
                    mRecyclerArticleArrayAdapter.notifyDataSetChanged();

                }
            }
        };
        mRecyclerViewArticles.addOnScrollListener(mEndlessListener);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_activity, menu);
        searchItem = menu.findItem(R.id.action_search);
        filterItem = menu.findItem(R.id.action_filter);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.hint_search));
        //int searchEditId =R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(R.id.search_src_text);
        et.setTextColor(Color.WHITE);
        et.setHintTextColor(Color.WHITE);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mFilter = query;
                searchView.clearFocus();
                setTitle(query);
                //mRecyclerArticleArrayAdapter.clearAdapter();
                loadArticles(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        // Return true to allow the action view to expand
                        filterItem.setVisible(false);
                        searchView.setQuery(mFilter, false);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        // When the action view is collapsed, reset the query
                        filterItem.setVisible(true);
                        // Return true to allow the action view to collapse
                        return true;
                    }
                });


        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_search:
                filterItem.setVisible(false);

                return true;

            case R.id.action_filter:
                getFilterFromSharedPreferences();
                startFilterFragmentDialog();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void startFilterFragmentDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterFragment filterSettingsDialogFragment = FilterFragment.newInstance(mBeginDate,mSortOrder, mNewsDeskValues);
        filterSettingsDialogFragment.show(fm,"filter_fragment");
    }

    public String getNewsDeskFilterQuery(){
        String values = "";
        for(String value:mNewsDeskValues){
            values = values.concat("\""+value+"\"").concat(" ");
        }
        return new String(NEWS_DESK +":("+values+")");
    }

    private void loadArticles(final int page) {
        Log.d (TAG_LOG, "loadArticles="+page);

        ArticleRequestParams requestParams = new ArticleRequestParams();

        if (mFilter != null && !"".equals(mFilter)) {

            requestParams.setPage(page);
            requestParams.setQuery(mFilter);
        }

            if (mBeginDate != null && !"".equals(mBeginDate)) {
                requestParams.setBeginDate(mBeginDate);
            }

            if (mSortOrder != null && !"".equals(mSortOrder)) {
                requestParams.setSortOrder(mSortOrder);
            }

            if (mNewsDeskValues != null && mNewsDeskValues.size() >= 1) {
                requestParams.setNewsDesk(getNewsDeskFilterQuery());
            }

            ArticleRestClient.getArticles(requestParams, new ArticlesCallback() {


                @Override
                public void onSuccess(ArticleResponse response) {

                    if (response.getArticles() != null) {
                        if (response.getArticles().size() > 0) {
                            mPage = page;
                        }
                        if (mArticles == null) {
                            mArticles = new ArrayList<>();
                        }
                        mArticles.addAll(response.getArticles());
                        Log.d(TAG_LOG, "addall");
                        mRecyclerArticleArrayAdapter.notifyDataSetChanged(mArticles);

                        if (page == 0) {
                            mRecyclerViewArticles.scrollToPosition(0);
                        }
                    }

                }

                @Override
                public void onError(Error error) {

                }


            });
        }


        @Override
        public void selectArticle (Article article){

            Intent intent = new Intent(SearchActivity.this, ArticleActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("article", Parcels.wrap(article));
            intent = intent.putExtras(bundle);

            startActivity(intent);
        }


    @Override
    public void onFinishEditDialog(String beginDate, String sortOrder, HashSet<String> newsDeskValueSet) {
        mBeginDate = beginDate;
        mSortOrder = sortOrder;
        mNewsDeskValues =  newsDeskValueSet;

        SharedPreferences.Editor editor = filterPrefs.edit();
        editor.putString(SORT, sortOrder);
        editor.putString(BEGIN_DATE,beginDate);
        editor.putStringSet(NEWS_DESK, newsDeskValueSet);
        editor.commit();

        //mRecyclerArticleArrayAdapter.clearAdapter();

        loadArticles(mPage);


    }
}
