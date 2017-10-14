package com.android.example.github.util;

import android.arch.lifecycle.LiveData;

/**
 * Created by gsipic on 14.10.17..
 */

public class AbsentLiveData extends LiveData {
    private AbsentLiveData() {
        postValue(null);
    }
    public static <T> LiveData<T> create() {
        //noinspection unchecked
        return new AbsentLiveData();
    }
}
