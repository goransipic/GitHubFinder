package com.android.example.github.ui.repo;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.example.github.R;
import com.android.example.github.databinding.ActivityRepoBinding;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static com.android.example.github.ui.repo.RepoFragment.REPO_NAME_KEY;
import static com.android.example.github.ui.repo.RepoFragment.REPO_OWNER_KEY;

public class RepoActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    ActivityRepoBinding mActivityRepoBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityRepoBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_repo);

        String owner = getIntent().getStringExtra(REPO_OWNER_KEY);
        String name = getIntent().getStringExtra(REPO_NAME_KEY);

        if (savedInstanceState == null) {
            RepoFragment fragment = RepoFragment.create(owner, name);
            String tag = "repo" + "/" + owner + "/" + name;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, tag)
                    .commitAllowingStateLoss();
        }

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    public static void start(Context context, String owner, String name) {
        Intent starter = new Intent(context, RepoActivity.class);
        starter.putExtra(REPO_OWNER_KEY, owner);
        starter.putExtra(REPO_NAME_KEY, name);
        context.startActivity(starter);
    }

}
