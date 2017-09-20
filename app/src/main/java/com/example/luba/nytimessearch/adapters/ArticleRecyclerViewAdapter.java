package com.example.luba.nytimessearch.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luba.nytimessearch.R;
import com.example.luba.nytimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by luba on 9/20/17.
 */

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Article> mArticlesNYTimes;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_result,parent,false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ArticleViewHolder) holder).setupArticleView(mArticlesNYTimes.get(position));
    }

    @Override
    public int getItemCount() {
        return this.mArticlesNYTimes != null ? this.mArticlesNYTimes.size() : 0;
    }

    public void notifyDataSetChanged(ArrayList<Article> articles) {
        this.mArticlesNYTimes = articles;
        notifyDataSetChanged();
    }

    private OnArticleRecyclerViewAdapterListener recyclerViewAdapterListener;

    public ArticleRecyclerViewAdapter(ArrayList<Article> articles, OnArticleRecyclerViewAdapterListener listener) {
        this.mArticlesNYTimes = articles;
        this.recyclerViewAdapterListener = listener;
    }

}

 class ArticleViewHolder extends RecyclerView.ViewHolder {

    private Article article;

    ImageView ivImage;
    TextView tvTitle;

    private OnArticleRecyclerViewAdapterListener recyclerViewAdapterListener;


    public ArticleViewHolder(View itemView) {
        super(itemView);

        ivImage = itemView.findViewById(R.id.ivImage);
        tvTitle = itemView.findViewById(R.id.tvTitle);


        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recyclerViewAdapterListener != null)
                    recyclerViewAdapterListener.selectArticle(article);
            }
        });
    }

    public void setupArticleView(Article article) {
        this.article = article;
        if (this.ivImage != null) {
            ivImage.setImageDrawable(null);
            String thumbnail = article.getThumbNail();
            if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(itemView.getContext()).load(thumbnail).into(ivImage);}
        }
        this.tvTitle.setText(article.getHeadLine());
    }
}