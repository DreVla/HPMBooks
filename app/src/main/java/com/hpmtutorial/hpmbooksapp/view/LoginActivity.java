package com.hpmtutorial.hpmbooksapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hpmtutorial.hpmbooksapp.R;
import com.hpmtutorial.hpmbooksapp.viewmodel.LoginActivityViewModel;
import com.hpmtutorial.hpmbooksapp.viewmodel.RegisterActivityViewModel;

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
        final Observer<Integer> uiChangeListener = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer newName) {
                // Update the UI
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
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
}
