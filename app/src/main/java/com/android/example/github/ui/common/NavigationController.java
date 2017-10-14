package com.android.example.github.ui.common;

import android.support.v4.app.FragmentManager;

import com.android.example.github.MainActivity;
import com.android.example.github.R;
import com.android.example.github.ui.repo.RepoFragment;
import com.android.example.github.ui.search.SearchFragment;
import com.android.example.github.ui.user.UserFragment;

import javax.inject.Inject;

/**
 * Created by gsipic on 14.10.17..
 */

public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;
    @Inject
    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void navigateToSearch() {
        SearchFragment searchFragment = new SearchFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, searchFragment)
                .commitAllowingStateLoss();
    }

    public void navigateToRepo(String owner, String name) {
        /*RepoFragment fragment = RepoFragment.create(owner, name);
        String tag = "repo" + "/" + owner + "/" + name;
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();*/
    }

    public void navigateToUser(String login) {
        /*String tag = "user" + "/" + login;
        UserFragment userFragment = UserFragment.create(login);
        fragmentManager.beginTransaction()
                .replace(containerId, userFragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();*/
    }
}
