package com.android.example.github.dependencyinjection;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.android.example.github.serviceapi.GithubService;
import com.android.example.github.database.GithubDb;
import com.android.example.github.database.RepoDao;
import com.android.example.github.database.UserDao;
import com.android.example.github.util.LiveDataCallAdapterFactory;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gsipic on 14.10.17..
 */

@Module(includes = ViewModelModule.class)
class AppModule {
    @Singleton
    @Provides
    GithubService provideGithubService(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create()))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(okHttpClient)
                .build()
                .create(GithubService.class);
    }

    @Singleton @Provides
    OkHttpClient provideClient(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        return httpClient.build();
    }

    @Singleton @Provides
    GithubDb provideDb(Application app) {
        return Room.databaseBuilder(app, GithubDb.class,"github.db").build();
    }

    @Singleton @Provides
    UserDao provideUserDao(GithubDb db) {
        return db.userDao();
    }

    @Singleton @Provides
    RepoDao provideRepoDao(GithubDb db) {
        return db.repoDao();
    }
}