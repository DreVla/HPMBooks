package com.hpmtutorial.hpmbooksapp.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
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
        final Observer<String> uiChangeListener = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newToken) {
                // Update the UI
//                Toast.makeText(LoginActivity.this, "Success!" + " New token is: " + newToken, Toast.LENGTH_SHORT).show();
                saveToSharedPref(newToken);
                Intent intent = new Intent(getApplicationContext(), BooksActivity.class);
                startActivity(intent);
            }
        };
        loginActivityViewModel.uiLiveData.observe(this, uiChangeListener);
    }


    public void setServerErrorListener(final Context context){
        final Observer<String> serverErrorListener = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String errorMessage) {
                // Update UI
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(errorMessage)
                        .setTitle(R.string.error_message)
                        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
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
}
