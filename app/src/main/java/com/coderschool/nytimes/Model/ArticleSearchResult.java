package com.coderschool.nytimes.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 6/21/2017.
 */

public class ArticleSearchResult {
    @SerializedName("docs")
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }
}

