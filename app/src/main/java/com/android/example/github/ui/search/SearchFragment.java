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
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.android.example.github.ui.repo.RepoActivity;
import com.android.example.github.vo.FilterBy;
import com.android.example.github.vo.Repo;
import com.android.example.github.vo.Resource;

import java.util.List;

import javax.inject.Inject;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * Created by gsipic on 14.10.17..
 */

public class SearchFragment extends LifecycleFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    SearchFragmentBinding binding;

    RepoListAdapter adapter;

    private SearchViewModel searchViewModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        SearchFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.search_fragment, container, false,
                        dataBindingComponent);
        binding = dataBinding;
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigationController = new NavigationController((AppCompatActivity) this.getActivity());
        searchViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel.class);
        initRecyclerView();
        RepoListAdapter rvAdapter = new RepoListAdapter(dataBindingComponent,
                repo -> RepoActivity.start(this.getContext(),repo.owner.login,repo.name));
        binding.repoList.setAdapter(rvAdapter);
        adapter = rvAdapter;

        initSearchInputListener();

        binding.setCallback(() -> searchViewModel.refresh());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_by_forks:
                doSearch(binding.input,FilterBy.FORKS);
                return true;
            case R.id.filter_by_stars:
                doSearch(binding.input,FilterBy.STARS);
                return true;
            case R.id.filter_by_date:
                doSearch(binding.input,FilterBy.UPDATED);
                return true;
            default:
                return false;
        }
    }

    private void initSearchInputListener() {
        binding.input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(v,searchViewModel.query.getValue() != null
                        ? searchViewModel.query.getValue().second: FilterBy.FORKS);
                return true;
            }
            return false;
        });
        binding.input.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                doSearch(v,searchViewModel.query.getValue() != null
                        ? searchViewModel.query.getValue().second: FilterBy.FORKS);
                return true;
            }
            return false;
        });
    }

    private void doSearch(View v, FilterBy filterBy) {
        String query = binding.input.getText().toString();
        // Dismiss keyboard
        dismissKeyboard(v.getWindowToken());
        binding.setQuery(query);
        searchViewModel.setQuery(new Pair<>(query,filterBy));
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
        binding.repoList.addItemDecoration(new DividerItemDecoration(
                this.getContext(), VERTICAL));
        searchViewModel.getResults().observe(this, result -> {
            binding.setSearchResource(result);
            binding.setResultCount((result == null || result.data == null)
                    ? 0 : result.data.size());
            adapter.replace(result == null ? null : result.data);
            binding.executePendingBindings();
        });

        searchViewModel.getLoadMoreStatus().observe(this, loadingMore -> {
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
