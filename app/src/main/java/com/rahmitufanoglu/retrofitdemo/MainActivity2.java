package com.rahmitufanoglu.retrofitdemo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity2 extends AppCompatActivity implements FragmentCommunicator {

    @BindView(R.id.toolbar_main) Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title) TextView tvToolbarTitle;
    private long mBackPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setToolbar();
        setFragment();
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

    private void setFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainFragment mainFragment = new MainFragment();
        fragmentTransaction.add(R.id.fragment_container, mainFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        Fragment fragment = new Fragment();

        if (fragment instanceof MainFragment) {
            MainFragment mainFragment = (MainFragment) fragment;
            mainFragment.handleIntent(intent);
        }
    }

    @Override
    public void onBackPressed() {
        final long currentTimeMillis = System.currentTimeMillis();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else if (currentTimeMillis - mBackPressedTime > 2000) {
            mBackPressedTime = currentTimeMillis;
            Toast toast = Toast.makeText(this, "Press back again to reach the homescreen", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void respond(String data) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SearchResultFragment searchResultFragment = (SearchResultFragment) fragmentManager.findFragmentById(R.id.fragment_search_result);
        searchResultFragment.test(data);
    }
}
