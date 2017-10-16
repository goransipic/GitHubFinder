package com.android.example.github.binding;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.example.github.ui.common.DateUtil;
import com.bumptech.glide.Glide;

import java.util.Date;

/**
 * Created by gsipic on 14.10.17..
 */

public class BindingAdapters {
    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url).into(imageView);
    }

    @BindingAdapter("text")
    public static void setDateText(TextView imageView, Date date) {
        imageView.setText(DateUtil.formatDate(date));
    }
}
