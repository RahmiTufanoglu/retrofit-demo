package com.rahmitufanoglu.retrofitdemo;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SampleDetailActivity extends AppCompatActivity {

    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.iv_collapsing) ImageView ivCollapsing;
    @BindView(R.id.toolbar_activity_detail) Toolbar toolbar;
    @BindView(R.id.tv_activity_detail) TextView tvActivityDetail;
    @BindView(R.id.tv_id) TextView tvId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setCollapsingToolbar();
        setToolbar();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void setCollapsingToolbar() {
        Intent intent = getIntent();
        String collapsingImage = intent.getStringExtra("image");
        int id = intent.getIntExtra("id", 0);
        String collapsingHeader = intent.getStringExtra("name");
        String content = intent.getStringExtra("fullname");

        Glide.with(this)
                .load(collapsingImage)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(ivCollapsing);

        tvId.setText(String.format(Locale.getDefault(), "%d", id));
        collapsingToolbarLayout.setTitle(collapsingHeader);
        tvActivityDetail.setText(content);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab_detail)
    public void setOnClick(View view) {
        Toast defaultToast = Toast.makeText(this, "This is a default Toast", Toast.LENGTH_SHORT);
        defaultToast.show();
    }
}
