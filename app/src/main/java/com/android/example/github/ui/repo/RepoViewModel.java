package com.android.example.github.ui.repo;

import android.arch.lifecycle.ViewModel;

import com.android.example.github.repository.RepoRepository;

import javax.inject.Inject;

/**
 * Created by gsipic on 14.10.17..
 */

public class RepoViewModel extends ViewModel {

    @Inject
    public RepoViewModel(RepoRepository repository) {

    }
}
