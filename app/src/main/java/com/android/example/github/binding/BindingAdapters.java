package com.android.example.github.binding;

import android.databinding.BindingAdapter;
import android.view.View;

/**
 * Created by gsipic on 14.10.17..
 */

public class BindingAdapters {
    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
