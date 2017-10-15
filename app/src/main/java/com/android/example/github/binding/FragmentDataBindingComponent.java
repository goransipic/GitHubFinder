package com.android.example.github.binding;

import android.support.v4.app.Fragment;

/**
 * Created by gsipic on 14.10.17..
 */

public class FragmentDataBindingComponent implements android.databinding.DataBindingComponent {
    private final FragmentBindingAdapters adapter;

    public FragmentDataBindingComponent(Fragment fragment) {
        this.adapter = new FragmentBindingAdapters(fragment);
    }

    @Override
    public FragmentBindingAdapters getFragmentBindingAdapters() {
        return adapter;
    }
}
