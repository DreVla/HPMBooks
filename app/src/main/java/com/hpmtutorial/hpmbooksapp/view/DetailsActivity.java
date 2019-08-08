package com.hpmtutorial.hpmbooksapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.textfield.TextInputEditText;
import com.hpmtutorial.hpmbooksapp.R;
import com.hpmtutorial.hpmbooksapp.databinding.ActivityDetailsBinding;
import com.hpmtutorial.hpmbooksapp.databinding.ActivityLoginBinding;
import com.hpmtutorial.hpmbooksapp.viewmodel.DetailsViewModel;
import com.hpmtutorial.hpmbooksapp.viewmodel.LoginActivityViewModel;

public class DetailsActivity extends AppCompatActivity {

    private DetailsViewModel detailsViewModel;
    private ActivityDetailsBinding binding;
    private String id;
    private TextInputEditText titleEditText, authorEditText, publisherEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        detailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setDetailsViewModel(detailsViewModel);

        id = getIntent().getStringExtra("book_id");

        titleEditText = findViewById(R.id.details_title_edittext);
        authorEditText = findViewById(R.id.details_author_edittext);
        publisherEditText = findViewById(R.id.details_publisher_edittext);

        titleEditText.setEnabled(false);
        authorEditText.setEnabled(false);
        publisherEditText.setEnabled(false);

        loadBook();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.details_edit_button:
                titleEditText.setEnabled(true);
                authorEditText.setEnabled(true);
                publisherEditText.setEnabled(true);
                break;
            case R.id.details_save_button:
                updateBook();
                titleEditText.setEnabled(false);
                authorEditText.setEnabled(false);
                publisherEditText.setEnabled(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadBook(){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.auth_token), null);
        if(token != null){
            detailsViewModel.sendGet(token,id);
        }
    }

    public void updateBook(){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.auth_token), null);
        if(token != null){
            detailsViewModel.sendPut(token,id);
        }
    }
}
