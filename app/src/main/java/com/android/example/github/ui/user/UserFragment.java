package com.android.example.github.ui.user;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.example.github.R;
import com.android.example.github.binding.FragmentDataBindingComponent;
import com.android.example.github.databinding.UserFragmentBinding;
import com.android.example.github.dependencyinjection.Injectable;
import com.android.example.github.ui.common.NavigationController;
import com.android.example.github.ui.common.RepoListAdapter;
import com.android.example.github.valueobjects.User;

import javax.inject.Inject;

/**
 * Created by gsipic on 14.10.17..
 */

public class UserFragment extends LifecycleFragment implements Injectable {
    public static final String LOGIN_KEY = "login";
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    NavigationController navigationController;

    android.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private UserViewModel userViewModel;
    private UserFragmentBinding binding;
    private RepoListAdapter adapter;

    public static UserFragment create(String login) {
        UserFragment userFragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LOGIN_KEY, login);
        userFragment.setArguments(bundle);
        return userFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navigationController = new NavigationController((AppCompatActivity) getActivity());
        UserFragmentBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.user_fragment,
                container, false, dataBindingComponent);
        dataBinding.setRetryCallback(() -> userViewModel.retry());
        binding = dataBinding;
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.avatar.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.github.com/" + binding.getUser().login));
            startActivity(i);
        });

        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.setLogin(getArguments().getString(LOGIN_KEY));
        userViewModel.getUser().observe(this, userResource -> {
            binding.setUser(userResource == null ? null : userResource.data);
            binding.setUserResource(userResource);
            binding.executePendingBindings();
        });
    }

    public interface ImageClickCallback {
        void onClick(User user);
    }
}
