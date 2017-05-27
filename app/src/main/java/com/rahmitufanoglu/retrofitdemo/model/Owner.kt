package com.rahmitufanoglu.retrofitdemo.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Owner(@SerializedName("login") @Expose var login: String?,
            @SerializedName("id") @Expose var id: Int?,
            @SerializedName("avatar_url") @Expose var avatarUrl: String?)
