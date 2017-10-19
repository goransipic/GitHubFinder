package com.android.example.github.dependencyinjection;

import com.android.example.github.ui.user.UserActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by gsipic on 18.10.17..
 */
@Module
public abstract class UserActivityModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract UserActivity contributeRepoActivity();
}
