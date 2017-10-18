package com.android.example.github.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.android.example.github.MainActivity;
import com.android.example.github.R;
import com.android.example.github.ui.repo.RepoActivity;
import com.android.example.github.ui.repo.RepoFragment;
import com.android.example.github.ui.search.SearchFragment;
import com.android.example.github.ui.user.UserActivity;
import com.android.example.github.ui.user.UserFragment;

import javax.inject.Inject;

import static com.android.example.github.ui.repo.RepoFragment.REPO_NAME_KEY;
import static com.android.example.github.ui.repo.RepoFragment.REPO_OWNER_KEY;

/**
 * Created by gsipic on 14.10.17..
 */

public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;
    private final Activity activity;

    public NavigationController(AppCompatActivity mainActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
        this.activity = mainActivity;
    }

    public void navigateToSearch() {
        SearchFragment searchFragment = new SearchFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, searchFragment)
                .commitAllowingStateLoss();
    }

    public void navigateToRepo(String owner, String name) {
        RepoActivity.start(activity, owner, name);
    }

    public void navigateToUser(String login) {
        UserActivity.start(activity, login);
    }
}
