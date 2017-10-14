package com.android.example.github.vo;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by gsipic on 14.10.17..
 */

public class RepoSearchResult {
    public final String query;
    public final List<Integer> repoIds;
    public final int totalCount;
    @Nullable
    public final Integer next;

    public RepoSearchResult(String query, List<Integer> repoIds, int totalCount,
                            Integer next) {
        this.query = query;
        this.repoIds = repoIds;
        this.totalCount = totalCount;
        this.next = next;
    }
}
