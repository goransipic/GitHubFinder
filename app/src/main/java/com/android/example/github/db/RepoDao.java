/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.example.github.db;

import com.android.example.github.vo.Contributor;
import com.android.example.github.vo.FilterBy;
import com.android.example.github.vo.Repo;
import com.android.example.github.vo.RepoSearchResult;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import java.util.Collections;
import java.util.List;

@Dao
public abstract class RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Repo... repos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertContributors(List<Contributor> contributors);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertRepos(List<Repo> repositories);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long createRepoIfNotExists(Repo repo);

    @Query("SELECT * FROM repo WHERE owner_login = :login AND name = :name")
    public abstract LiveData<Repo> load(String login, String name);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT login, avatarUrl, contributions FROM contributor "
            + "WHERE repoName = :name AND repoOwner = :owner "
            + "ORDER BY contributions DESC")
    public abstract LiveData<List<Contributor>> loadContributors(String owner, String name);

    @Query("SELECT * FROM Repo "
            + "WHERE owner_login = :owner "
            + "ORDER BY stars DESC")
    public abstract LiveData<List<Repo>> loadRepositories(String owner);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(RepoSearchResult result);

    @Query("SELECT * FROM RepoSearchResult WHERE query = :query")
    public abstract LiveData<RepoSearchResult> search(String query);

    public LiveData<List<Repo>> loadOrdered(List<Integer> repoIds, FilterBy filterBy) {
        return Transformations.map(loadById(repoIds), repositories -> {
            switch (filterBy) {
                case FORKS:
                    Collections.sort(repositories, (o1, o2) -> (o2.forks < o1.forks) ? -1 : ((o2.forks == o1.forks) ? 0 : 1));
                    break;
                case STARS:
                    Collections.sort(repositories, (o1, o2) -> (o2.stars < o1.stars) ? -1 : ((o2.stars == o1.stars) ? 0 : 1));
                    break;
                case UPDATED:
                    Collections.sort(repositories, (o1, o2) -> (o2.updatedate.getTime() < o1.updatedate.getTime()) ? -1 : ((o2.updatedate.getTime() == o1.updatedate.getTime()) ? 0 : 1));
                    break;
                default:
            }

            return repositories;
        });
    }

    @Query("SELECT * FROM Repo WHERE id in (:repoIds)")
    protected abstract LiveData<List<Repo>> loadById(List<Integer> repoIds);

    @Query("SELECT * FROM Repo ORDER BY forks DESC")
    public abstract LiveData<List<Repo>> loadByForks();

    @Query("SELECT * FROM Repo ORDER BY stars DESC")
    public abstract LiveData<List<Repo>> loadByStars();

    @Query("SELECT * FROM Repo ORDER BY updatedate DESC")
    public abstract LiveData<List<Repo>> loadByDate();

    @Query("SELECT * FROM RepoSearchResult WHERE query = :query")
    public abstract RepoSearchResult findSearchResult(String query);
}
