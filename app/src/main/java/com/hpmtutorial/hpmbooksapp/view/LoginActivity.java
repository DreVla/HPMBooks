package com.hpmtutorial.hpmbooksapp.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewOverlay;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputLayout;
import com.hpmtutorial.hpmbooksapp.R;
import com.hpmtutorial.hpmbooksapp.databinding.ActivityLoginBinding;
import com.hpmtutorial.hpmbooksapp.view.adapter.DataBindingAdapters;
import com.hpmtutorial.hpmbooksapp.viewmodel.LoginActivityViewModel;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityViewModel loginActivityViewModel;
    private TextInputLayout emailLayout, passwordLayout;
    private ActivityLoginBinding binding;
    private FrameLayout loadingOverlay;
    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginActivityViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setLoginViewModel(loginActivityViewModel);

        emailLayout = findViewById(R.id.login_email_layout);
        passwordLayout = findViewById(R.id.login_password_layout);
        loadingOverlay = findViewById(R.id.login_overlay_view_loading);

        setTokenListener();
        setUIListener();
        setUIErrorListener();
        setServerErrorListener(this);

    }

    private void setUIErrorListener() {
        final Observer<String> emailErrorListener = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String errorMessage) {
                DataBindingAdapters.setErrorMessage(emailLayout, errorMessage);
            }
        };
        // leaga observerul
        loginActivityViewModel.emailError.observe(this, emailErrorListener);

        final Observer<String> passErrorListener = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String errorMessage) {
                DataBindingAdapters.setErrorMessage(passwordLayout, errorMessage);
            }
        };
        // leaga observerul
        loginActivityViewModel.passwordError.observe(this, passErrorListener);
    }

    private void setUIListener() {
        final Observer<LoginActivityViewModel.UIChange> uiChangeListener = new Observer<LoginActivityViewModel.UIChange>() {
            @Override
            public void onChanged(@Nullable final LoginActivityViewModel.UIChange msg) {
                switch (msg){
                    case LOAD:
                        startAnimation();
                        break;
                    case CHANGE:
                        Intent booksIntent = new Intent(getApplicationContext(), BooksActivity.class);
                        endAnimation();
                        startActivity(booksIntent);
                        loginActivityViewModel.uiLiveData.setValue(LoginActivityViewModel.UIChange.NOACTION);
                        break;
                    case REGISTER:
                        Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(registerIntent);
                    default:

                }
            }
        };
        loginActivityViewModel.uiLiveData.observe(this, uiChangeListener);
    }

    private void setTokenListener(){
        final Observer<String> tokenObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String token) {
                saveToSharedPref(token);
            }
        };
        loginActivityViewModel.tokenReceived.observe(this, tokenObserver);
    }

    public void setServerErrorListener(final Context context){
        final Observer<String> serverErrorListener = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String errorMessage) {
                // Update UI
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(errorMessage)
                        .setTitle(R.string.error_message)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                endAnimation();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                Log.d("Server error", "onChanged: " + errorMessage);
            }
        };
        loginActivityViewModel.serverError.observe(this, serverErrorListener);
    }

    public void saveToSharedPref(String token){
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.auth_token), token);
        editor.apply();
    }

    public void startAnimation(){
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        loadingOverlay.setAnimation(inAnimation);
        loadingOverlay.setVisibility(View.VISIBLE);
    }

    public void endAnimation(){
        outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        loadingOverlay.setAnimation(outAnimation);
        loadingOverlay.setVisibility(View.GONE);
    }
}
