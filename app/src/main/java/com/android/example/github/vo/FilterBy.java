package com.android.example.github.vo;

/**
 * Created by gsipic on 15.10.17..
 */

public enum FilterBy {
    STARS("stars"),FORKS("forks"),UPDATED("date");

    String type;

    FilterBy(String type) {
        this.type = type;
    }

}
