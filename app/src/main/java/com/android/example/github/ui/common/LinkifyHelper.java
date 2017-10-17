package com.android.example.github.ui.common;

import android.text.util.Linkify;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gsipic on 17.10.17..
 */

public class LinkifyHelper {

    private static final String regex = ".*?";


    public static void addLink(TextView textView,
                               final String link) {
        Linkify.addLinks(textView, Pattern.compile(regex), link);
    }


}
