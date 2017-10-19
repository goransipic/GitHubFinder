package com.android.example.github.ui.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.Pair;

import com.android.example.github.repository.RepoRepository;
import com.android.example.github.util.AbsentLiveData;
import com.android.example.github.util.Objects;
import com.android.example.github.valueobjects.FilterBy;
import com.android.example.github.valueobjects.Repo;
import com.android.example.github.valueobjects.Resource;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by gsipic on 14.10.17..
 */

public class SearchViewModel extends ViewModel {
    public final MutableLiveData<Pair<String,FilterBy>> query = new MutableLiveData<>();

    private final LiveData<Resource<List<Repo>>> results;

    private final NextPageHandler nextPageHandler;

    private RepoRepository repoRepository;

    @Inject
    SearchViewModel(final RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
        nextPageHandler = new NextPageHandler(repoRepository);
        results = Transformations.switchMap(query, search -> {
            if (search == null || search.first.trim().length() == 0) {
                return AbsentLiveData.create();
            } else {
                return repoRepository.search(search.first, search.second);
            }
        });
    }

    LiveData<Resource<List<Repo>>> getResults() {
        return results;
    }

    public void setQuery(@NonNull Pair<String,FilterBy> sortByPair) {
        String input = sortByPair.first.toLowerCase(Locale.getDefault()).trim();
        if (Objects.equals(input, query.getValue())) {
            return;
        }
        nextPageHandler.reset();
        query.setValue(sortByPair);
    }

    LiveData<LoadMoreState> getLoadMoreStatus() {
        return nextPageHandler.getLoadMoreState();
    }

    void loadNextPage() {
        String value = query.getValue().first;
        if (value == null || value.trim().length() == 0) {
            return;
        }
        nextPageHandler.queryNextPage(value);
    }

    void refresh() {
        if (query.getValue() != null) {
            query.setValue(query.getValue());
        }
    }

    static class LoadMoreState {
        private final boolean running;
        private final String errorMessage;
        private boolean handledError = false;

        LoadMoreState(boolean running, String errorMessage) {
            this.running = running;
            this.errorMessage = errorMessage;
        }

        boolean isRunning() {
            return running;
        }

        String getErrorMessage() {
            return errorMessage;
        }

        String getErrorMessageIfNotHandled() {
            if (handledError) {
                return null;
            }
            handledError = true;
            return errorMessage;
        }
    }

    @VisibleForTesting
    static class NextPageHandler implements Observer<Resource<Boolean>> {
        @Nullable
        private LiveData<Resource<Boolean>> nextPageLiveData;
        private final MutableLiveData<LoadMoreState> loadMoreState = new MutableLiveData<>();
        private String query;
        private final RepoRepository repository;
        @VisibleForTesting
        boolean hasMore;

        @VisibleForTesting
        NextPageHandler(RepoRepository repository) {
            this.repository = repository;
            reset();
        }

        void queryNextPage(String query) {
            if (Objects.equals(this.query, query)) {
                return;
            }
            unregister();
            this.query = query;
            nextPageLiveData = repository.searchNextPage(query);
            loadMoreState.setValue(new LoadMoreState(true, null));
            //noinspection ConstantConditions
            nextPageLiveData.observeForever(this);
        }

        @Override
        public void onChanged(@Nullable Resource<Boolean> result) {
            if (result == null) {
                reset();
            } else {
                switch (result.status) {
                    case SUCCESS:
                        hasMore = Boolean.TRUE.equals(result.data);
                        unregister();
                        loadMoreState.setValue(new LoadMoreState(false, null));
                        break;
                    case ERROR:
                        hasMore = true;
                        unregister();
                        loadMoreState.setValue(new LoadMoreState(false,
                                result.message));
                        break;
                }
            }
        }

        private void unregister() {
            if (nextPageLiveData != null) {
                nextPageLiveData.removeObserver(this);
                nextPageLiveData = null;
                if (hasMore) {
                    query = null;
                }
            }
        }

        private void reset() {
            unregister();
            hasMore = true;
            loadMoreState.setValue(new LoadMoreState(false, null));
        }

        MutableLiveData<LoadMoreState> getLoadMoreState() {
            return loadMoreState;
        }
    }
}
