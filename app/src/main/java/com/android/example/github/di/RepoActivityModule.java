package com.android.example.github.di;

import com.android.example.github.ui.repo.RepoActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by gsipic on 16.10.17..
 */
@Module
public abstract class RepoActivityModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract RepoActivity contributeRepoActivity();
}
