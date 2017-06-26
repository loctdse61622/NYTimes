package com.coderschool.nytimes.Api;

import com.coderschool.nytimes.Model.ArticleSearchResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Admin on 6/21/2017.
 */

public interface ArticleApi {
    @GET("articlesearch.json")
    Call<ArticleSearchResult> search(@QueryMap(encoded = true) Map<String, String> options);
}