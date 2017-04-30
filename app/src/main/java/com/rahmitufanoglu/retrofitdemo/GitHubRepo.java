package com.rahmitufanoglu.retrofitdemo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


class GitHubRepo {

    @SerializedName("id")
    @Expose
    private Integer mId;
    @SerializedName("name")
    @Expose
    private String mName;
    @SerializedName("full_name")
    @Expose
    private String mFullName;
    @SerializedName("owner")
    @Expose
    private Owner mOwner;

    public GitHubRepo(Integer id, String name, String fullName, Owner owner) {
        mId = id;
        mName = name;
        mFullName = fullName;
        mOwner = owner;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }

    public Owner getOwner() {
        return mOwner;
    }

    public void setOwner(Owner owner) {
        mOwner = owner;
    }
}
