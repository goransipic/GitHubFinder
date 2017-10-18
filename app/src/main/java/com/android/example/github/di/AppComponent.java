package com.android.example.github.di;

import android.app.Application;

import com.android.example.github.GithubApp;
import com.android.example.github.ui.user.UserActivity;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by gsipic on 14.10.17..
 */
@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        MainActivityModule.class,
        RepoActivityModule.class,
        UserActivityModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
    void inject(GithubApp githubApp);
}
