package com.hpmtutorial.hpmbooksapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputLayout;
import com.hpmtutorial.hpmbooksapp.R;
import com.hpmtutorial.hpmbooksapp.databinding.ActivityRegisterBinding;
import com.hpmtutorial.hpmbooksapp.viewmodel.RegisterActivityViewModel;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private RegisterActivityViewModel registerActivityViewModel;
    private TextInputLayout usernameLayout, emailLayout, passwordLayout, passwordCheckLayout;
    private TextView errorMessage;
    private String username, email, password, passwordCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        registerActivityViewModel = ViewModelProviders.of(this).get(RegisterActivityViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setRegisterViewModel(registerActivityViewModel);

        usernameLayout = findViewById(R.id.register_username_layout);
        emailLayout = findViewById(R.id.register_email_layout);
        passwordLayout = findViewById(R.id.register_pass_layout);
        passwordCheckLayout = findViewById(R.id.register_passcheck_layout);
        errorMessage = findViewById(R.id.error_message_textview);

        // Creaza observer pentru a putea sa schimbe ui-ul
        final Observer<Integer> uiChangeListener = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer newName) {
                // Update UI
                Toast.makeText(RegisterActivity.this, "Success! Please login!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        };
        // leaga observerul
        registerActivityViewModel.getCurrentUi().observe(this, uiChangeListener);
    }

    public void registerNewUser(View view) {
        usernameLayout.setError(null);
        emailLayout.setError(null);
        passwordLayout.setError(null);
        passwordCheckLayout.setError(null);
        username = registerActivityViewModel.username;
        email = registerActivityViewModel.email;
        password = registerActivityViewModel.password;
        passwordCheck = registerActivityViewModel.passwordCheck;
        if (username == null || email == null || password == null) {
            errorMessage.setText(R.string.error_message);
        } else {
            errorMessage.setText(null);
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailLayout.setError("Please input valid email!");
            } else if (password.equals(passwordCheck)) {
                registerActivityViewModel.sendPost(username, email, password);
            } else {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                passwordLayout.setError("Passwords do not match!");
                passwordCheckLayout.setError("Passwords do not match!");
            }
        }
    }
}
