package com.hpmtutorial.hpmbooksapp.view.adapter;

import androidx.databinding.BindingAdapter;

import com.google.android.material.textfield.TextInputLayout;

public class DataBindingAdapters {

    @BindingAdapter("app:errorText")
    public static void setErrorMessage(TextInputLayout view, String errorMessage) {
        view.setError(errorMessage);
    }
}
