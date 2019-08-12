package com.hpmtutorial.hpmbooksapp.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.textfield.TextInputLayout;
import com.hpmtutorial.hpmbooksapp.R;
import com.hpmtutorial.hpmbooksapp.databinding.ActivityAddBookBinding;
import com.hpmtutorial.hpmbooksapp.databinding.ActivityBooksBinding;
import com.hpmtutorial.hpmbooksapp.view.adapter.DataBindingAdapters;
import com.hpmtutorial.hpmbooksapp.viewmodel.AddBookActivityViewModel;
import com.hpmtutorial.hpmbooksapp.viewmodel.BooksActivityViewModel;

public class AddBookActivity extends AppCompatActivity {

    private AddBookActivityViewModel addBookActivityViewModel;
    private TextInputLayout titleLayout, authorLayout, publisherLayour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddBookBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_book);
        addBookActivityViewModel = ViewModelProviders.of(this).get(AddBookActivityViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setAddBookViewModel(addBookActivityViewModel);

        titleLayout = findViewById(R.id.add_book_title_layout);
        authorLayout = findViewById(R.id.add_book_author_layout);
        publisherLayour = findViewById(R.id.add_book_publisher_layout);

        setToken();
        addUIErrorListener();
        addServerErrorListener(this);

    }

    private void addServerErrorListener(final Context context) {
        addBookActivityViewModel.mutableLiveDataServerError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
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
            }
        });
    }

    private void addUIErrorListener() {
        addBookActivityViewModel.mutableLiveDataUiError.observe(this, new Observer<AddBookActivityViewModel.uiError>() {
            @Override
            public void onChanged(AddBookActivityViewModel.uiError uiError) {
                switch (uiError){
                    case FILL_FIELDS:
                        DataBindingAdapters.setErrorMessage(titleLayout, getResources().getString(R.string.fill_fields));
                        DataBindingAdapters.setErrorMessage(authorLayout, getResources().getString(R.string.fill_fields));
                        DataBindingAdapters.setErrorMessage(publisherLayour, getResources().getString(R.string.fill_fields));
                        break;
                    case SUCCESS:
                        setResult(RESULT_OK);
                        finish();
                        break;
                    default:
                }

            }
        });
    }

    public String setToken(){
        SharedPreferences sharedPref = getApplication().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.auth_token), null);
        addBookActivityViewModel.setToken(token);
        return token;
    }
}
