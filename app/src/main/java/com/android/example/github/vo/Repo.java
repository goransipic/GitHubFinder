package com.android.example.github.vo;

import com.android.example.github.db.GithubTypeConverters;
import com.google.gson.annotations.SerializedName;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

/**
 * Created by gsipic on 14.10.17..
 */
@Entity(primaryKeys = {"name", "owner_login"})
@TypeConverters(GithubTypeConverters.class)
public class Repo {
    public static final int UNKNOWN_ID = -1;
    public final int id;
    @SerializedName("name")
    public final String name;
    @SerializedName("watchers_count")
    public final int watchers;
    @SerializedName("forks_count")
    public final int forks;
    @SerializedName("open_issues_count")
    public final int issues;
    @SerializedName("stargazers_count")
    public final int stars;
    @SerializedName("html_url")
    public final String htmlUrl;
    @SerializedName("created_at")
    public final Date createdate;
    @SerializedName("updated_at")
    public final Date updatedate;
    @SerializedName("owner")
    @Embedded(prefix = "owner_")
    public final Owner owner;

    public Repo(int id, String name, Owner owner, int watchers, int forks ,int issues, int stars, String htmlUrl,Date createdate,Date updatedate) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.watchers = watchers;
        this.forks = forks;
        this.issues = issues;
        this.stars = stars;
        this.htmlUrl = htmlUrl;
        this.createdate = createdate;
        this.updatedate = updatedate;
    }

    public static class Owner {
        @SerializedName("login")
        public final String login;
        @SerializedName("url")
        public final String url;
        @SerializedName("avatar_url")
        public final String avatarUrl;

        public Owner(String login, String url, String avatarUrl) {
            this.login = login;
            this.url = url;
            this.avatarUrl = avatarUrl;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Owner owner = (Owner) o;

            if (login != null ? !login.equals(owner.login) : owner.login != null) {
                return false;
            }
            return url != null ? url.equals(owner.url) : owner.url == null;
        }

        @Override
        public int hashCode() {
            int result = login != null ? login.hashCode() : 0;
            result = 31 * result + (url != null ? url.hashCode() : 0);
            return result;
        }
    }
}
