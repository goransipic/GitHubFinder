package com.android.example.github.ui.repo;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.example.github.R;
import com.android.example.github.binding.FragmentDataBindingComponent;
import com.android.example.github.databinding.RepoFragmentBinding;
import com.android.example.github.di.Injectable;
import com.android.example.github.ui.common.DateUtil;
import com.android.example.github.ui.common.NavigationController;
import com.android.example.github.vo.Repo;
import com.android.example.github.vo.Resource;

import java.util.Collections;

import javax.inject.Inject;

/**
 * Created by gsipic on 14.10.17..
 */

public class RepoFragment extends Fragment implements LifecycleRegistryOwner, Injectable {

    public static final String REPO_OWNER_KEY = "repo_owner";

    public static final String REPO_NAME_KEY = "repo_name";

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private RepoViewModel repoViewModel;


    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    RepoFragmentBinding binding;
    ContributorAdapter adapter;

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigationController = new NavigationController((AppCompatActivity) this.getActivity());
        repoViewModel = ViewModelProviders.of(this, viewModelFactory).get(RepoViewModel.class);
        Bundle args = getArguments();
        if (args != null && args.containsKey(REPO_OWNER_KEY) &&
                args.containsKey(REPO_NAME_KEY)) {
            repoViewModel.setId(args.getString(REPO_OWNER_KEY),
                    args.getString(REPO_NAME_KEY));
        } else {
            repoViewModel.setId(null, null);
        }
        LiveData<Resource<Repo>> repo = repoViewModel.getRepo();
        repo.observe(this, resource -> {
            binding.setRepo(resource == null ? null : resource.data);
            binding.setRepoResource(resource);
            binding.executePendingBindings();
        });

        ContributorAdapter adapter = new ContributorAdapter(dataBindingComponent,
                contributor -> navigationController.navigateToUser(contributor.getLogin()) );
        this.adapter = adapter;
        binding.contributorList.setAdapter(adapter);
        initContributorList(repoViewModel);
    }

    private void initContributorList(RepoViewModel viewModel) {
        viewModel.getContributors().observe(this, listResource -> {
            if (listResource != null && listResource.data != null) {
                adapter.replace(listResource.data);
            } else {
                adapter.replace(Collections.emptyList());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RepoFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.repo_fragment, container, false);
        dataBinding.setRetryCallback(() -> repoViewModel.retry());
        binding = dataBinding;
        return dataBinding.getRoot();
    }

    public static RepoFragment create(String owner, String name) {
        RepoFragment repoFragment = new RepoFragment();
        Bundle args = new Bundle();
        args.putString(REPO_OWNER_KEY, owner);
        args.putString(REPO_NAME_KEY, name);
        repoFragment.setArguments(args);
        return repoFragment;
    }

}
