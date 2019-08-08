package com.hpmtutorial.hpmbooksapp.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        registerActivityViewModel = ViewModelProviders.of(this).get(RegisterActivityViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setRegisterViewModel(registerActivityViewModel);

        usernameLayout = findViewById(R.id.register_username_layout);
        emailLayout = findViewById(R.id.register_email_layout);
        passwordLayout = findViewById(R.id.register_pass_layout);
        passwordCheckLayout = findViewById(R.id.register_passcheck_layout);

        registerUIObserver();
        registerErrorObserver();
        registerServerErrorObserver(this);
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
        final Observer<String> uiChangeListener = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String status) {
                // Update UI
                Toast.makeText(RegisterActivity.this, status, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        };
        registerActivityViewModel.uiLiveData.observe(this, uiChangeListener);
    }

    public void registerServerErrorObserver(final Context context){
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
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                Log.d("Server error", "onChanged: " + errorMessage);
            }
        };
        registerActivityViewModel.serverError.observe(this, serverErrorListener);
    }
}
