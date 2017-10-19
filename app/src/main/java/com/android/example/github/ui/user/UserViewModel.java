package com.android.example.github.ui.user;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.android.example.github.repository.RepoRepository;
import com.android.example.github.repository.UserRepository;
import com.android.example.github.util.AbsentLiveData;
import com.android.example.github.util.Objects;
import com.android.example.github.valueobjects.Repo;
import com.android.example.github.valueobjects.Resource;
import com.android.example.github.valueobjects.User;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by gsipic on 14.10.17..
 */

public class UserViewModel extends ViewModel {

    @VisibleForTesting
    final MutableLiveData<String> login = new MutableLiveData<>();
    private final LiveData<Resource<List<Repo>>> repositories;
    private final LiveData<Resource<User>> user;
    @SuppressWarnings("unchecked")
    @Inject
    public UserViewModel(UserRepository userRepository, RepoRepository repoRepository) {
        user = Transformations.switchMap(login, login -> {
            if (login == null) {
                return AbsentLiveData.create();
            } else {
                return userRepository.loadUser(login);
            }
        });
        repositories = Transformations.switchMap(login, login -> {
            if (login == null) {
                return AbsentLiveData.create();
            } else {
                return repoRepository.loadRepos(login);
            }
        });
    }

    void setLogin(String login) {
        if (Objects.equals(this.login.getValue(), login)) {
            return;
        }
        this.login.setValue(login);
    }

    LiveData<Resource<User>> getUser() {
        return user;
    }

    LiveData<Resource<List<Repo>>> getRepositories() {
        return repositories;
    }

    void retry() {
        if (this.login.getValue() != null) {
            this.login.setValue(this.login.getValue());
        }
    }
}

