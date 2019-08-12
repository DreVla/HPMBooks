package com.hpmtutorial.hpmbooksapp.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hpmtutorial.hpmbooksapp.R;
import com.hpmtutorial.hpmbooksapp.databinding.ActivityBooksBinding;
import com.hpmtutorial.hpmbooksapp.model.Book;
import com.hpmtutorial.hpmbooksapp.view.adapter.BooksRecyclerViewAdapter;
import com.hpmtutorial.hpmbooksapp.viewmodel.BooksActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBooks;
    private List<Book> bookList;
    private BooksRecyclerViewAdapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String token;

    private FrameLayout loadingOverlay;
    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;
    //    Add viewmodel
    private BooksActivityViewModel booksActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityBooksBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_books);
        booksActivityViewModel = ViewModelProviders.of(this).get(BooksActivityViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setBooksViewModel(booksActivityViewModel);

        loadingOverlay = findViewById(R.id.overlay_loading_view);
        recyclerViewBooks = findViewById(R.id.books_recyclerview);
        recyclerViewBooks.setHasFixedSize(true);

        booksActivityViewModel = ViewModelProviders.of(this).get(BooksActivityViewModel.class);

        bookList = new ArrayList<>();

        loadBooks();
        layoutManager = new LinearLayoutManager(this);
        recyclerViewBooks.setLayoutManager(layoutManager);
        rvAdapter = new BooksRecyclerViewAdapter(bookList, new BooksRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book item) {
                String id = item.getId();
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("book_id", id);
                startActivity(intent);
            }

            @Override
            public void onRemoveClick(View view, int adapterPosition) {
                booksActivityViewModel.sendDelete(token, rvAdapter.getBookId(adapterPosition));
            }
        });
        binding.setAdpt(rvAdapter);

        observeBooks();
        observeUIChange(this);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.books_log_out:
                logOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.auth_token), null);
        editor.apply();
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void observeUIChange(final Context context) {
        booksActivityViewModel.uiChangeMutableLiveData.observe(this, new Observer<BooksActivityViewModel.uiChange>() {
            @Override
            public void onChanged(BooksActivityViewModel.uiChange uiChange) {
                switch (uiChange) {
                    case LOADING:
                        startAnimation();
                        break;
                    case DONE:
                        endAnimation();
                        break;
                    case ADD_BOOK:
                        Intent intent = new Intent(getApplicationContext(), AddBookActivity.class);
                        startActivityForResult(intent, 1);
                        break;
                    case DELETED_BOOK:
                        loadBooks();
                        break;
                    case FAILED:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Failed to delete!")
                                .setTitle(R.string.error_message)
                                .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                    default:
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadBooks();
    }

    private void observeBooks() {
        booksActivityViewModel.mutableLiveDataBooks.observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                if (bookList.isEmpty()) {
                    bookList.addAll(books);
                    rvAdapter.notifyDataSetChanged();
                } else if (bookList.size() == books.size() - 1) {
                    bookList.add(books.get(books.size() - 1));
                    rvAdapter.notifyDataSetChanged();
                } else {
                    rvAdapter.setBooksList(books);
                    rvAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void loadBooks() {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        token = sharedPref.getString(getString(R.string.auth_token), null);
        if (token != null) {
            booksActivityViewModel.sendGet(token);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
    }

    public void startAnimation() {
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        loadingOverlay.setAnimation(inAnimation);
        loadingOverlay.setVisibility(View.VISIBLE);
    }

    public void endAnimation() {
        outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        loadingOverlay.setAnimation(outAnimation);
        loadingOverlay.setVisibility(View.GONE);
    }
}
