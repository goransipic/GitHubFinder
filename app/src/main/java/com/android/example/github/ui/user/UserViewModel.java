package com.android.example.github.ui.user;

import android.arch.lifecycle.ViewModel;

import com.android.example.github.repository.RepoRepository;
import com.android.example.github.repository.UserRepository;

import javax.inject.Inject;

/**
 * Created by gsipic on 14.10.17..
 */

public class UserViewModel extends ViewModel {

    @Inject
    public UserViewModel(UserRepository userRepository, RepoRepository repoRepository) {

    }
}

