package com.rahmitufanoglu.retrofitdemo;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view_fragment) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title) TextView tvToolbarTitle;
    private ListAdapter mListAdapter;
    private List<GitHubRepo> mGitHubRepoList;
    private ProgressDialog mProgressDialog;
    AnimationDrawable mAnimationDrawable;
    private long mBackPressedTime = 0;
    public static final String TAG = "CallInstances";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
        swipeRefreshLayout.setOnRefreshListener(this);

        setAnimation();
        setToolbar();
        setProgressDialog();
        setRecyclerView();
    }

    private void setToolbar() {
        tvToolbarTitle.setText(R.string.placeholder);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void setAnimation() {
        mAnimationDrawable = (AnimationDrawable) swipeRefreshLayout.getBackground();
        mAnimationDrawable.setEnterFadeDuration(0);
        mAnimationDrawable.setExitFadeDuration(5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAnimationDrawable != null && !mAnimationDrawable.isRunning())
            mAnimationDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning())
            mAnimationDrawable.stop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_custom_toast) {
            openCustomToast();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openCustomToast() {
        final Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(getLayoutInflater().inflate(R.layout.toast_custom, (ViewGroup) findViewById(R.id.toast_custom)));
        toast.show();
    }

    public void setProgressDialog() {
        mProgressDialog = ProgressDialog.show(this, null, null, true, false);
        if (mProgressDialog.getWindow() != null) {
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mProgressDialog.setContentView(R.layout.progress_dialog);

        /*mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Fetching repos...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String searchText = newText.toLowerCase();
        List<GitHubRepo> list = new ArrayList<>();
        for (GitHubRepo gitHubRepoList : mGitHubRepoList) {
            String name = String.valueOf(gitHubRepoList.getName()).toLowerCase();
            if (name.contains(searchText)) {
                list.add(gitHubRepoList);
            }
        }
        mListAdapter.setFilter(list);
        return true;
    }

    @Override
    public void onRefresh() {
        if (checkInternetConnection()) {
            loadJson();
            Toast.makeText(this, "Repos...", Toast.LENGTH_SHORT).show();
        } else {
            checkInternetConnection();
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            mProgressDialog.hide();
        }
    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void setRecyclerView() {
        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //recyclerView.setLayoutManager(staggeredGridLayoutManager);

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        //recyclerView.setLayoutManager(gridLayoutManager);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setItemViewCacheSize(20);
        loadJson();
    }

    private void loadJson() {
        String user = "RahmiTufanoglu";
        GitHubClient gitHubClient = ServiceGenerator.createService(GitHubClient.class);
        Call<List<GitHubRepo>> call = gitHubClient.reposForUser(user);
        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                // response.isSuccesful() is true if the HTTP response status code is in the range of 200-299
                Log.d(TAG, "request success");
                if (response.isSuccessful()) {
                    mGitHubRepoList = response.body();
                    mListAdapter = new ListAdapter(mGitHubRepoList);
                    recyclerView.setAdapter(mListAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                    mProgressDialog.hide();
                } else {
                    int statusCode = response.code();
                    Toast toastStatus = Toast.makeText(getApplicationContext(), "Status code: " + statusCode, Toast.LENGTH_SHORT);
                    toastStatus.show();
                }
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                // handle execution failures like no internet connectivity
                Log.e(TAG, "request failed");
                if (call.isCanceled()) {
                    Log.e(TAG, "request was cancelled");
                } else {
                    Log.e(TAG, "request was cancelled");
                    Toast toastError = Toast.makeText(getApplicationContext(), "Error fetching data!", Toast.LENGTH_SHORT);
                    toastError.show();
                }
                swipeRefreshLayout.setRefreshing(false);
                mProgressDialog.hide();
            }
        });
    }

    @Override
    public void onBackPressed() {
        final long currentTimeMillis = System.currentTimeMillis();

        if (currentTimeMillis - mBackPressedTime > 2000) {
            mBackPressedTime = currentTimeMillis;
            Toast toast = Toast.makeText(this, "Press back again to reach the homescreen", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            super.onBackPressed();
        }
    }
}
