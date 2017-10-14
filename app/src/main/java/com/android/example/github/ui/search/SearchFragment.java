package com.android.example.github.ui.search;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.example.github.R;
import com.android.example.github.binding.FragmentDataBindingComponent;
import com.android.example.github.databinding.SearchFragmentBinding;
import com.android.example.github.di.Injectable;
import com.android.example.github.ui.common.NavigationController;
import com.android.example.github.ui.common.RepoListAdapter;
import com.android.example.github.ui.common.RetryCallback;
import com.android.example.github.vo.Repo;
import com.android.example.github.vo.Resource;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by gsipic on 14.10.17..
 */

public class SearchFragment extends LifecycleFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    SearchFragmentBinding binding;

    RepoListAdapter adapter;

    private SearchViewModel searchViewModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SearchFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.search_fragment, container, false,
                        dataBindingComponent);
        binding = dataBinding;
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel.class);
        initRecyclerView();
        RepoListAdapter rvAdapter = new RepoListAdapter(dataBindingComponent, true,
                new RepoListAdapter.RepoClickCallback() {
                    @Override
                    public void onClick(Repo repo) {
                        navigationController.navigateToRepo(repo.owner.login, repo.name);
                    }
                });
        binding.repoList.setAdapter(rvAdapter);
        adapter = rvAdapter;

        initSearchInputListener();

        binding.setCallback(new RetryCallback() {
            @Override
            public void retry() {
                searchViewModel.refresh();
            }
        });
    }

    private void initSearchInputListener() {
        binding.input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch(v);
                    return true;
                }
                return false;
            }
        });
        binding.input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    doSearch(v);
                    return true;
                }
                return false;
            }
        });
    }

    private void doSearch(View v) {
        String query = binding.input.getText().toString();
        // Dismiss keyboard
        dismissKeyboard(v.getWindowToken());
        binding.setQuery(query);
        searchViewModel.setQuery(query);
    }

    private void initRecyclerView() {

        binding.repoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                int lastPosition = layoutManager
                        .findLastVisibleItemPosition();
                if (lastPosition == adapter.getItemCount() - 1) {
                    searchViewModel.loadNextPage();
                }
            }
        });
        searchViewModel.getResults().observe(this, new Observer<Resource<List<Repo>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Repo>> result) {
                binding.setSearchResource(result);
                binding.setResultCount((result == null || result.data == null)
                        ? 0 : result.data.size());
                adapter.replace(result == null ? null : result.data);
                binding.executePendingBindings();
            }
        });

        searchViewModel.getLoadMoreStatus().observe(this, new Observer<SearchViewModel.LoadMoreState>() {
            @Override
            public void onChanged(@Nullable SearchViewModel.LoadMoreState loadingMore) {
                if (loadingMore == null) {
                    binding.setLoadingMore(false);
                } else {
                    binding.setLoadingMore(loadingMore.isRunning());
                    String error = loadingMore.getErrorMessageIfNotHandled();
                    if (error != null) {
                        Snackbar.make(binding.loadMoreBar, error, Snackbar.LENGTH_LONG).show();
                    }
                }
                binding.executePendingBindings();
            }
        });
    }

    private void dismissKeyboard(IBinder windowToken) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(windowToken, 0);
        }
    }
}
