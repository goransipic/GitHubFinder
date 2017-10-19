package com.android.example.github.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.example.github.AppExecutors;
import com.android.example.github.serviceapi.ApiResponse;
import com.android.example.github.serviceapi.GithubService;
import com.android.example.github.database.UserDao;
import com.android.example.github.valueobjects.Resource;
import com.android.example.github.valueobjects.User;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by gsipic on 14.10.17..
 */

@Singleton
public class UserRepository {
    private final UserDao userDao;
    private final GithubService githubService;
    private final AppExecutors appExecutors;

    @Inject
    UserRepository(AppExecutors appExecutors, UserDao userDao, GithubService githubService) {
        this.userDao = userDao;
        this.githubService = githubService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<User>> loadUser(String login) {
        return new NetworkBoundResource<User,User>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull User item) {
                userDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return userDao.findByLogin(login);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return githubService.getUser(login);
            }
        }.asLiveData();
    }
}
