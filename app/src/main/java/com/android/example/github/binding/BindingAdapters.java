package com.android.example.github.binding;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.example.github.R;
import com.android.example.github.ui.common.DateUtil;
import com.android.example.github.ui.common.LinkifyHelper;
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

    @BindingAdapter(value={"text","url"})
    public static void setLinkifyText(TextView textView, String text, String url) {
        //textView.setTextColor(ContextCompat.getColor(textView.getContext(),R.color.colorPrimary));
        //textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setText(text);
        //textView.setLinkTextColor(Color.parseColor("#2f6699"));
        LinkifyHelper.addLink(textView,url);

    }
}
