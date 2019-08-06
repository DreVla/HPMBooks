package com.hpmtutorial.hpmbooksapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.hpmtutorial.hpmbooksapp.R;
import com.hpmtutorial.hpmbooksapp.model.Book;
import com.hpmtutorial.hpmbooksapp.viewmodel.BooksActivityViewModel;
import com.hpmtutorial.hpmbooksapp.view.adapter.BooksRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBooks;
    private List<Book> bookList;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;

//    Add viewmodel
    private BooksActivityViewModel booksActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        recyclerViewBooks = findViewById(R.id.books_recyclerview);
        recyclerViewBooks.setHasFixedSize(true);

        booksActivityViewModel = ViewModelProviders.of(this).get(BooksActivityViewModel.class);

        bookList = new ArrayList<>();

        loadBooks();
        layoutManager = new LinearLayoutManager(this);
        recyclerViewBooks.setLayoutManager(layoutManager);
        rvAdapter = new BooksRecyclerViewAdapter(bookList);
        recyclerViewBooks.setAdapter(rvAdapter);

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        booksActivityViewModel.getBookList().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                bookList.addAll(books);
                rvAdapter.notifyDataSetChanged();
            }
        });
    }

    public void loadBooks(){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.auth_token), null);
        Log.d("ReadToken", "token is: " + token);
        if(token != null){
            booksActivityViewModel.sendGet(token);
        }
    }
}
