package com.rahmitufanoglu.retrofitdemo.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GitHubRepo(@SerializedName("id") @Expose var id: Int?,
                 @SerializedName("name") @Expose var name: String?,
                 @SerializedName("full_name") @Expose var fullName: String?,
                 @SerializedName("owner") @Expose var owner: Owner?)
