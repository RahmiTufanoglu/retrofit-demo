package com.rahmitufanoglu.retrofitdemo.api;


import com.rahmitufanoglu.retrofitdemo.model.GitHubRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface GitHubService2 {

    @GET("users/{user}/repos")
        //Observable<List<GitHubRepo>> getRepos(
    Call<List<GitHubRepo>> getRepos(
            @Path("user") String user
    );
}
