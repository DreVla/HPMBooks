package com.hpmtutorial.hpmbooksapp.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hpmtutorial.hpmbooksapp.R;
import com.hpmtutorial.hpmbooksapp.viewmodel.LoginActivityViewModel;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityViewModel loginActivityViewModel;
    private EditText emailEdittext, passwordEdittext;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivityViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel.class);
        emailEdittext = findViewById(R.id.login_email_edittext);
        passwordEdittext = findViewById(R.id.login_password_edittext);

        // Creaza observer pentru a putea sa schimbe ui-ul
        final Observer<String> uiChangeListener = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newToken) {
                // Update the UI

                Toast.makeText(LoginActivity.this, "Success!" + " New token is: " + newToken, Toast.LENGTH_SHORT).show();
                saveToSharedPref(newToken);
                Intent intent = new Intent(getApplicationContext(), BooksActivity.class);
                startActivity(intent);
            }
        };
        // leaga observerul
        loginActivityViewModel.getCurrentUi().observe(this, uiChangeListener);
    }

    public void userLogin(View view) {
        email = emailEdittext.getText().toString();
        password = passwordEdittext.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
        } else {
            loginActivityViewModel.sendPost(email, password);
        }
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
