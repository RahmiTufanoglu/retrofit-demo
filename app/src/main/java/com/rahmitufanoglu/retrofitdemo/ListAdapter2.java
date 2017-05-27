package com.rahmitufanoglu.retrofitdemo;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rahmitufanoglu.retrofitdemo.model.GitHubRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListAdapter2 extends RecyclerView.Adapter<ListAdapter2.ListViewHolder> {

    private List<GitHubRepo> mGitHubRepoList;

    public ListAdapter2(List<GitHubRepo> gitHubRepoList) {
        mGitHubRepoList = gitHubRepoList;
    }

    @Override
    public ListAdapter2.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        GitHubRepo gitHubRepoList = mGitHubRepoList.get(position);

        Glide.with(holder.civAvatar.getContext())
                .load(gitHubRepoList.getOwner().getAvatarUrl())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .dontAnimate()
                .into(holder.civAvatar);

        holder.tvPlaceholderOne.setText(String.format(Locale.getDefault(), "%d", gitHubRepoList.getId()));
        holder.tvPlaceholderTwo.setText(gitHubRepoList.getName());

        // start the Activity
        holder.view.setOnClickListener(view -> {
            Context context = view.getContext();
            Intent intent = new Intent(context, SampleDetailActivity.class);
            intent.putExtra("image", gitHubRepoList.getOwner().getAvatarUrl());
            intent.putExtra("id", gitHubRepoList.getId());
            intent.putExtra("name", gitHubRepoList.getName());
            intent.putExtra("fullname", gitHubRepoList.getFullName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mGitHubRepoList.size();
    }

    public void setFilter(List<GitHubRepo> list) {
        mGitHubRepoList = new ArrayList<>();
        mGitHubRepoList.addAll(list);
        notifyDataSetChanged();
    }

    // set the RecyclerView content
    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.civ_avatar) ImageView civAvatar;
        @BindView(R.id.tv_placeholder_one) TextView tvPlaceholderOne;
        @BindView(R.id.tv_placeholder_two) TextView tvPlaceholderTwo;
        final View view;

        ListViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }
}
