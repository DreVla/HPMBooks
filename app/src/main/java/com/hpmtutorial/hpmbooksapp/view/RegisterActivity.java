package com.hpmtutorial.hpmbooksapp.view;

import android.content.Intent;
import android.os.Bundle;
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
import com.hpmtutorial.hpmbooksapp.view.adapter.DataBindingAdapters;
import com.hpmtutorial.hpmbooksapp.viewmodel.RegisterActivityViewModel;

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

        registerUIObserver();
        registerErrorObserver();
//
//
//
//        final Observer<String> usernameErrorListener = new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable final String errorMessage) {
//                DataBindingAdapters.setErrorMessage(usernameLayout, errorMessage);
//            }
//        };
//        registerActivityViewModel.usernameError.observe(this, usernameErrorListener);
//
//        final Observer<String> emailErrorListener = new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable final String errorMessage) {
//                DataBindingAdapters.setErrorMessage(emailLayout, errorMessage);
//            }
//        };
//        registerActivityViewModel.emailError.observe(this, emailErrorListener);
//
//        final Observer<String> passwordErrorListener = new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable final String errorMessage) {
//                DataBindingAdapters.setErrorMessage(passwordLayout, errorMessage);
//            }
//        };
//        registerActivityViewModel.passwordError.observe(this, passwordErrorListener);
//
//        final Observer<String> passwordCheckErrorListener = new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable final String errorMessage) {
//
//                DataBindingAdapters.setErrorMessage(passwordCheckLayout, errorMessage);
//            }
//        };
//        registerActivityViewModel.passwordError.observe(this, passwordCheckErrorListener);


    }

    public void registerErrorObserver() {
        final Observer<RegisterActivityViewModel.ErrorStatus> errorListener = new Observer<RegisterActivityViewModel.ErrorStatus>() {
            @Override
            public void onChanged(@Nullable final RegisterActivityViewModel.ErrorStatus error) {
                DataBindingAdapters.setErrorMessage(usernameLayout, null);
                DataBindingAdapters.setErrorMessage(emailLayout, null);
                DataBindingAdapters.setErrorMessage(passwordLayout,null);
                DataBindingAdapters.setErrorMessage(passwordCheckLayout, null);
                switch(error)
                {
                    case FILL_ALL:
                        DataBindingAdapters.setErrorMessage(usernameLayout, getResources().getString(R.string.fill_fields));
                        DataBindingAdapters.setErrorMessage(emailLayout, getResources().getString(R.string.fill_fields));
                        DataBindingAdapters.setErrorMessage(passwordLayout, getResources().getString(R.string.fill_fields));
                        DataBindingAdapters.setErrorMessage(passwordCheckLayout, getResources().getString(R.string.fill_fields));
                        break;
                    case FILL_USER:
                        DataBindingAdapters.setErrorMessage(usernameLayout, getResources().getString(R.string.fill_fields));
                        break;
                    case FILL_EMAIL:
                        DataBindingAdapters.setErrorMessage(emailLayout, getResources().getString(R.string.fill_fields));
                        break;
                    case FILL_PASSWORD:
                        DataBindingAdapters.setErrorMessage(passwordLayout, getResources().getString(R.string.fill_fields));
                        break;
                    case FILL_PASSWORDCHECK:
                        DataBindingAdapters.setErrorMessage(passwordCheckLayout, getResources().getString(R.string.fill_fields));
                        break;
                    case INVALID_MAIL:
                        DataBindingAdapters.setErrorMessage(emailLayout, getResources().getString(R.string.invalid_email));
                        break;
                    case PASSWORDS_NO_MATCH:
                        DataBindingAdapters.setErrorMessage(passwordLayout, getResources().getString(R.string.passwords_no_match));
                        DataBindingAdapters.setErrorMessage(passwordCheckLayout, getResources().getString(R.string.passwords_no_match));
                        break;
                    default:

                }
            }
        };
        registerActivityViewModel.errorMessage.observe(this, errorListener);
    }

    public void registerUIObserver(){
        final Observer<Integer> uiChangeListener = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer newName) {
                // Update UI
                Toast.makeText(RegisterActivity.this, "Success! Please login!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        };
        registerActivityViewModel.uiLiveData.observe(this, uiChangeListener);
    }
}
