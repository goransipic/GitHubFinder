package com.android.example.github.valueobjects;

/**
 * Created by gsipic on 15.10.17..
 */

public enum FilterBy {
    STARS("stars"),FORKS("forks"),UPDATED("updatedate");

    String type;

    FilterBy(String type) {
        this.type = type;
    }

}
