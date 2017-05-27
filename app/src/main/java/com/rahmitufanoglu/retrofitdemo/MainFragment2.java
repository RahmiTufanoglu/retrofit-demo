package com.rahmitufanoglu.retrofitdemo;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rahmitufanoglu.retrofitdemo.api.GitHubService;
import com.rahmitufanoglu.retrofitdemo.api.ServiceGenerator;
import com.rahmitufanoglu.retrofitdemo.model.GitHubRepo;
import com.rahmitufanoglu.retrofitdemo.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment2 extends Fragment implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

    //@Inject GitHubService2 gitHubService;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view_fragment) RecyclerView recyclerView;
    private ListAdapter mListAdapter;
    private List<GitHubRepo> mGitHubRepoList;
    private ProgressDialog mProgressDialog;
    private AnimationDrawable mAnimationDrawable;
    private SearchView mSearchView;
    private Disposable mDisposable;
    public static final String TAG = "CallInstances";
    private FragmentCommunicator mFragmentCommunicator;

    public MainFragment2() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        //((DemoApplication) getApplication()).getAppComponent().inject(this);

        handleIntent(getActivity().getIntent());
        setHasOptionsMenu(true);

        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
        swipeRefreshLayout.setOnRefreshListener(this);

        setAnimation();
        setProgressDialog();
        setRecyclerView();

        return view;
    }

    public void setAnimation() {
        mAnimationDrawable = (AnimationDrawable) swipeRefreshLayout.getBackground();
        mAnimationDrawable.setEnterFadeDuration(0);
        mAnimationDrawable.setExitFadeDuration(5000);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAnimationDrawable != null && !mAnimationDrawable.isRunning())
            mAnimationDrawable.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning())
            mAnimationDrawable.stop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_replace_fragment) {
            replaceFragment();
            //mFragmentCommunicator.respond("Test");
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SearchResultFragment searchResultFragment = new SearchResultFragment();
        fragmentTransaction.replace(R.id.fragment_container, searchResultFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setProgressDialog() {
        mProgressDialog = ProgressDialog.show(getActivity(), null, null, true, false);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_search, menu);

        ComponentName componentName = new ComponentName(getActivity(), MainActivity2.class);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        //mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        // mSearchView.setIconifiedByDefault(false); -> to show the search icoon
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getString(R.string.search));

        //ImageView closeButton = (ImageView) mSearchView.findViewById(R.id.search_close_btn);
        //ImageView searchButton = (ImageView) mSearchView.findViewById(R.id.search_mag_icon);

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                swipeRefreshLayout.setEnabled(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                swipeRefreshLayout.setEnabled(true);
                return true;
            }
        });
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

    public void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mSearchView.setQuery(query, true);
        }
    }

    @Override
    public void onRefresh() {
        if (NetworkUtil.isNetworkConnected(getActivity())) {
            Toast.makeText(getActivity(), "Repos...", Toast.LENGTH_SHORT).show();
            loadJson();
        } else {
            NetworkUtil.isNetworkConnected(getActivity());
            Toast connectionToast = Toast.makeText(getActivity(), "No internet", Toast.LENGTH_SHORT);
            connectionToast.show();
            swipeRefreshLayout.setRefreshing(false);
            mProgressDialog.hide();
        }
    }

    public void setRecyclerView() {
        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //recyclerView.setLayoutManager(staggeredGridLayoutManager);

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        //recyclerView.setLayoutManager(gridLayoutManager);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setItemViewCacheSize(20);
        loadJson();
    }

    private void loadJson() {
        /*
        mDisposable = gitHubService2.getRepos(Constants2.USER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<GitHubRepo>>() {
                    @Override
                    public void onNext(List<GitHubRepo> value) {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        */
        GitHubService gitHubService = ServiceGenerator.createService(GitHubService.class);
        Call<List<GitHubRepo>> call = gitHubService.getRepos(Constants2.USER);
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
                    Toast toastStatus = Toast.makeText(getActivity(), "Status code: " + statusCode, Toast.LENGTH_SHORT);
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
                    Toast toastError = Toast.makeText(getActivity(), "Error fetching data!", Toast.LENGTH_SHORT);
                    toastError.show();
                }
                swipeRefreshLayout.setRefreshing(false);
                mProgressDialog.hide();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mFragmentCommunicator = (FragmentCommunicator) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(context.toString() + " must implement OnListSelectedListener");
        }
    }
}
