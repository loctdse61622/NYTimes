package com.coderschool.nytimes.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.coderschool.nytimes.Adapter.ArticleAdapter;
import com.coderschool.nytimes.Api.ArticleApi;
import com.coderschool.nytimes.Dialog.OptionDialog;
import com.coderschool.nytimes.Model.Article;
import com.coderschool.nytimes.Model.ArticleSearchRequest;
import com.coderschool.nytimes.Model.ArticleSearchResult;
import com.coderschool.nytimes.R;
import com.coderschool.nytimes.Utils.RetrofitUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;


public class MainActivity extends AppCompatActivity implements OptionDialog.Listener {
    @BindView(R.id.rvArticle)
    RecyclerView rvArticle;
    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;
    @BindView(R.id.pbLoadMore)
    ProgressBar pbLoadMore;

    private ArticleSearchRequest searchRequest;
    private ArticleApi articleApi;
    private ArticleAdapter articleAdapter;

    @Override
    public void onSaveOption(ArticleSearchRequest mSearchRequest) {
        searchRequest = mSearchRequest;
        search();
    }

    private interface Listener {
        void onResult(ArticleSearchResult searchResult);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (!isNetworkAvailable() || !isOnline()){
            Toast.makeText(getBaseContext(), "No network available", Toast.LENGTH_SHORT).show();
        }

        setUpView();
        setUpApi();
        search();
    }

    private void setUpApi(){
        searchRequest = new ArticleSearchRequest();
        articleApi = RetrofitUtils.get().create(ArticleApi.class);
    }

    private void setUpView(){
        articleAdapter = new ArticleAdapter(this);
        rvArticle.setAdapter(articleAdapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, VERTICAL);
        rvArticle.setLayoutManager(layoutManager);
        articleAdapter.setListener(new ArticleAdapter.Listener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }

            @Override
            public void onItemClicked(Article article) {
                openWebView(article);
            }

        });
    }

    private void openWebView(Article article){
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        intent.putExtra("article", article);
        startActivity(intent);
    }

    private void search(){
        searchRequest.resetPage();
        pbLoading.setVisibility(View.VISIBLE);
        fetchArticle(searchResult -> {
            articleAdapter.setData(searchResult.getArticles());
            rvArticle.scrollToPosition(0);
        });

    }

    private void loadMore(){
        searchRequest.nextPage();
        pbLoadMore.setVisibility(View.VISIBLE);
        fetchArticle(searchResult -> {
            articleAdapter.appendData(searchResult.getArticles());
        });
    }

    private void fetchArticle(Listener listener){
        articleApi.search(searchRequest.toQueryMap()).enqueue(new Callback<ArticleSearchResult>() {
            @Override
            public void onResponse(Call<ArticleSearchResult> call, Response<ArticleSearchResult> response) {
                if (response.body() != null){
                    listener.onResult(response.body());
                }
                handleComplete();
            }

            @Override
            public void onFailure(Call<ArticleSearchResult> call, Throwable t) {
                Log.d("MainActivity.class ", t.getMessage());
            }
        });
    }

    private void showOption(){
        FragmentManager fm = getSupportFragmentManager();
        OptionDialog optionDialog = OptionDialog.newInstance(searchRequest, MainActivity.this);
        optionDialog.show(fm, "option_fragment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        setUpSearchView(menu.findItem(R.id.action_search));
        return super.onCreateOptionsMenu(menu);
    }

    private void setUpSearchView(MenuItem item){
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchRequest.setQuery(query);
                search();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_sort:
                showOption();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleComplete(){
        pbLoading.setVisibility(View.GONE);
        pbLoadMore.setVisibility(View.GONE);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}
