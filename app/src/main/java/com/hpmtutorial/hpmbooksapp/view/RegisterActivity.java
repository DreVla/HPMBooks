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
import com.hpmtutorial.hpmbooksapp.viewmodel.RegisterActivityViewModel;

public class RegisterActivity extends AppCompatActivity {

    private RegisterActivityViewModel registerActivityViewModel;
    private EditText usernameEdittext, emailEdittext, passwordEdittext, passwordCheckEdittext;
    private String username, email, password, passwordCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEdittext = findViewById(R.id.register_username_edittext);
        emailEdittext = findViewById(R.id.register_email_edittext);
        passwordEdittext = findViewById(R.id.register_pass_edittext);
        passwordCheckEdittext = findViewById(R.id.register_passcheck_edittext);

        // Leaga de viewmodel
        registerActivityViewModel = ViewModelProviders.of(this).get(RegisterActivityViewModel.class);

        // Creaza observer pentru a putea sa schimbe ui-ul
        final Observer<Integer> uiChangeListener = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer newName) {
                // Update the UI, in this case, a TextView.
                Toast.makeText(RegisterActivity.this, "Success! Please login!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        };
        // leaga observerul
        registerActivityViewModel.getCurrentUi().observe(this, uiChangeListener);
    }

    public void registerNewUser(View view) {
        username = usernameEdittext.getText().toString();
        email = emailEdittext.getText().toString();
        password = passwordEdittext.getText().toString();
        passwordCheck = passwordCheckEdittext.getText().toString();
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
        } else {
            if (password.equals(passwordCheck)) {
                registerActivityViewModel.sendPost(username, email, password);
            } else {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
