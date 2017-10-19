package com.android.example.github.dependencyinjection;

import com.android.example.github.ui.repo.RepoFragment;
import com.android.example.github.ui.search.SearchFragment;
import com.android.example.github.ui.user.UserFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by gsipic on 14.10.17..
 */

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract RepoFragment contributeRepoFragment();

    @ContributesAndroidInjector
    abstract UserFragment contributeUserFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();
}
