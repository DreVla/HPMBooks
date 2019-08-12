package com.hpmtutorial.hpmbooksapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.hpmtutorial.hpmbooksapp.R;
import com.hpmtutorial.hpmbooksapp.databinding.ActivityDetailsBinding;
import com.hpmtutorial.hpmbooksapp.databinding.ActivityLoginBinding;
import com.hpmtutorial.hpmbooksapp.viewmodel.BooksActivityViewModel;
import com.hpmtutorial.hpmbooksapp.viewmodel.DetailsViewModel;
import com.hpmtutorial.hpmbooksapp.viewmodel.LoginActivityViewModel;

public class DetailsActivity extends AppCompatActivity {

    private DetailsViewModel detailsViewModel;
    private ActivityDetailsBinding binding;
    private String id;
    private TextInputEditText titleEditText, authorEditText, publisherEditText;
    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;
    private FrameLayout loadingOverlay;

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
        loadingOverlay = findViewById(R.id.book_overlay_view_loading);

        setEditTexts(false);
        observeUIChange();
        loadBook();
    }


    private void observeUIChange() {
        detailsViewModel.uiStatusMutableLiveData.observe(this, new Observer<DetailsViewModel.uiStatus>() {
            @Override
            public void onChanged(DetailsViewModel.uiStatus uiStatus) {
                switch (uiStatus){
                    case LOADING:
                        startAnimation();
                        break;
                    case DONE:
                        endAnimation();
                        break;
                }
            }
        });
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
                setEditTexts(true);
                break;
            case R.id.details_save_button:
                updateBook();
                setEditTexts(false);
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


    private void setEditTexts(boolean status) {
        titleEditText.setEnabled(status);
        authorEditText.setEnabled(status);
        publisherEditText.setEnabled(status);
    }
}
