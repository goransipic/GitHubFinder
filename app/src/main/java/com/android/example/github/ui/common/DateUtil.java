package com.android.example.github.ui.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gsipic on 16.10.17..
 */

public class DateUtil {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String formatDate(Date date) {
       return simpleDateFormat.format(date);
    }

}
