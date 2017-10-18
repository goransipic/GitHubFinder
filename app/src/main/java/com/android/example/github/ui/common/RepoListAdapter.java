package com.android.example.github.ui.common;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.example.github.R;
import com.android.example.github.databinding.RepoItemBinding;
import com.android.example.github.util.Objects;
import com.android.example.github.vo.Repo;



/**
 * Created by gsipic on 14.10.17..
 */

public class RepoListAdapter extends DataBoundListAdapter<Repo, RepoItemBinding> {
    private final android.databinding.DataBindingComponent dataBindingComponent;
    private final RepoClickCallback repoClickCallback;
    private final RepoClickCallback repoClickCallbackAvatar;

    public RepoListAdapter(DataBindingComponent dataBindingComponent,
            RepoClickCallback repoClickCallback, RepoClickCallback repoClickCallbackAvatar) {
        this.dataBindingComponent = dataBindingComponent;
        this.repoClickCallback = repoClickCallback;
        this.repoClickCallbackAvatar = repoClickCallbackAvatar;
    }

    @Override
    protected RepoItemBinding createBinding(ViewGroup parent) {
       final RepoItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.repo_item,
                        parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Repo repo = binding.getRepo();
            if (repo != null && repoClickCallback != null) {
                repoClickCallback.onClick(repo);
            }
        });
        binding.avatarImage.setOnClickListener(v -> { Repo repo = binding.getRepo();
            if (repo != null && repoClickCallback != null) {
                repoClickCallbackAvatar.onClick(repo);
            } });
        return binding;
    }

    @Override
    protected void bind(RepoItemBinding binding, Repo item) {
        binding.setRepo(item);
    }

    @Override
    protected boolean areItemsTheSame(Repo oldItem, Repo newItem) {
        return Objects.equals(oldItem.owner, newItem.owner) &&
                Objects.equals(oldItem.name, newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(Repo oldItem, Repo newItem) {
        return oldItem.stars == newItem.stars;
    }

    public interface RepoClickCallback {
        void onClick(Repo repo);
    }
}
