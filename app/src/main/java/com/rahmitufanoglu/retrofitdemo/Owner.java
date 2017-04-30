package com.rahmitufanoglu.retrofitdemo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Owner {

    @SerializedName("login")
    @Expose
    private String mLogin;
    @SerializedName("id")
    @Expose
    private Integer mId;
    @SerializedName("avatar_url")
    @Expose
    private String mAvatarUrl;

    public Owner(String login, Integer id, String avatarUrl) {
        mLogin = login;
        mId = id;
        mAvatarUrl = avatarUrl;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        mLogin = login;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        mAvatarUrl = avatarUrl;
    }
}
