package com.hpmtutorial.hpmbooksapp.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.hpmtutorial.hpmbooksapp.R;
import com.hpmtutorial.hpmbooksapp.databinding.ActivityBooksBinding;
import com.hpmtutorial.hpmbooksapp.databinding.ActivityRegisterBinding;
import com.hpmtutorial.hpmbooksapp.model.Book;
import com.hpmtutorial.hpmbooksapp.viewmodel.BooksActivityViewModel;
import com.hpmtutorial.hpmbooksapp.view.adapter.BooksRecyclerViewAdapter;
import com.hpmtutorial.hpmbooksapp.viewmodel.RegisterActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBooks;
    private List<Book> bookList;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String token;

//    Add viewmodel
    private BooksActivityViewModel booksActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityBooksBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_books);
        booksActivityViewModel = ViewModelProviders.of(this).get(BooksActivityViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setBooksViewModel(booksActivityViewModel);

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
        recyclerViewBooks.setAdapter(rvAdapter);

        observeBooks();
        observeUIChange(this);

    }

    private void observeUIChange(final Context context) {
        booksActivityViewModel.uiChangeMutableLiveData.observe(this, new Observer<BooksActivityViewModel.uiChange>() {
            @Override
            public void onChanged(BooksActivityViewModel.uiChange uiChange) {
                switch (uiChange){
                    case ADD_BOOK:
                        Log.d("AddBook", "onChanged: creates Intent");
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
        Log.d("AddBook", "onActivityResult: enters activity result");
        loadBooks();
    }

    private void observeBooks() {
        booksActivityViewModel.mutableLiveDataBooks.observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                if(bookList.isEmpty()){
                    bookList.addAll(books);
                    rvAdapter.notifyDataSetChanged();
                } else if(bookList.size() == books.size()-1){
                    bookList.add(books.get(books.size()-1));
                    rvAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void loadBooks(){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        token = sharedPref.getString(getString(R.string.auth_token), null);
        if(token != null){
            booksActivityViewModel.sendGet(token);
        }
    }
}
