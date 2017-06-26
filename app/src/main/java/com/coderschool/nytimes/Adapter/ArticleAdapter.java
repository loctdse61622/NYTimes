package com.coderschool.nytimes.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coderschool.nytimes.Model.Article;
import com.coderschool.nytimes.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 6/22/2017.
 */

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int NO_IMAGE = 0;
    private static final int WITH_IMAGE = 1;
    private final List<Article> articles;
    private final Context context;
    private Listener listener;


    public ArticleAdapter(Context context) {
        this.articles = new ArrayList<>();
        this.context = context;
    }

    public interface Listener {
        void onLoadMore();
        void onItemClicked(Article article);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if(hasImageAt(position)){
            return WITH_IMAGE;
        }
        return NO_IMAGE;
    }

    private boolean hasImageAt(int position){
        Article article = articles.get(position);
        return  article.getMultimedia() != null && article.getMultimedia().size() > 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NO_IMAGE){
            return new NoImageViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_article_no_image, parent, false));
        } else {
            return new WithImageViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_article, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = articles.get(position);


        if (holder instanceof NoImageViewHolder) {
            bindNoImageViewHolder((NoImageViewHolder) holder, article);
        } else if (holder instanceof WithImageViewHolder) {
            bindWithImageViewHolder((WithImageViewHolder) holder, article);
        }

        if (position == articles.size() - 1 && listener != null) {
            listener.onLoadMore();
        }

        holder.itemView.setOnClickListener(v -> {
            listener.onItemClicked(article);
        });
    }

    private void bindNoImageViewHolder(NoImageViewHolder holder, Article article) {
        holder.tvSnippet.setText(article.getSnippet());
    }

    private void bindWithImageViewHolder(WithImageViewHolder holder, Article article) {
        holder.tvSnippet.setText(article.getSnippet());
        Article.Media media = article.getMultimedia().get(0);
        int itemHeight = media.setUpLayoutParams();
        ViewGroup.LayoutParams layoutParams = holder.ivCover.getLayoutParams();
        layoutParams.height = itemHeight;
        holder.ivCover.setLayoutParams(layoutParams);
        Glide.with(context)
                .load(media.getUrl())
                .placeholder(R.drawable.default_placeholder)
                .into(holder.ivCover);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setData(List<Article> newArticles) {
        articles.clear();
        articles.addAll(newArticles);
        notifyDataSetChanged();
    }

    public void appendData(List<Article> newArticles) {
        int nextPos = articles.size();
        articles.addAll(nextPos, newArticles);
        notifyItemRangeChanged(nextPos, newArticles.size());
    }

    class NoImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvSnippet)
        TextView tvSnippet;

        public NoImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class WithImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvSnippet)
        TextView tvSnippet;
        @BindView(R.id.ivCover)
        ImageView ivCover;

        public WithImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
