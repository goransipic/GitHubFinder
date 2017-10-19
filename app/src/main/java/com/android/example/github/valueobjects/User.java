package com.android.example.github.valueobjects;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gsipic on 14.10.17..
 */
@Entity(primaryKeys = "login")
public class User {
    @SerializedName("login")
    public final String login;
    @SerializedName("avatar_url")
    public final String avatarUrl;
    @SerializedName("url")
    public final String url;
    @SerializedName("name")
    public final String name;
    @SerializedName("company")
    public final String company;
    @SerializedName("repos_url")
    public final String reposUrl;
    @SerializedName("email")
    public final String email;


    public User(String login, String avatarUrl, String name, String company,
                String reposUrl, String email, String url) {
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.company = company;
        this.reposUrl = reposUrl;
        this.email = email;
        this.url = url;
    }
}
