package com.android.example.github.valueobjects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gsipic on 14.10.17..
 */
@Entity(primaryKeys = {"repoName", "repoOwner", "login"},
        foreignKeys = @ForeignKey(entity = Repo.class,
                parentColumns = {"name", "owner_login"},
                childColumns = {"repoName", "repoOwner"},
                onUpdate = ForeignKey.CASCADE,
                deferred = true))
public class Contributor {
    @SerializedName("login")
    private final String login;

    @SerializedName("contributions")
    private final int contributions;

    @SerializedName("avatar_url")
    private final String avatarUrl;

    private String repoName;

    private String repoOwner;

    public Contributor(String login, int contributions, String avatarUrl) {
        this.login = login;
        this.contributions = contributions;
        this.avatarUrl = avatarUrl;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public void setRepoOwner(String repoOwner) {
        this.repoOwner = repoOwner;
    }

    public String getLogin() {
        return login;
    }

    public int getContributions() {
        return contributions;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getRepoOwner() {
        return repoOwner;
    }
}
