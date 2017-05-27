package com.rahmitufanoglu.retrofitdemo.api


import com.rahmitufanoglu.retrofitdemo.model.GitHubRepo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubService {

    @GET("users/{user}/repos")
    fun getRepos(
            @Path("user") user: String
    ):
            Call<List<GitHubRepo>>

}
