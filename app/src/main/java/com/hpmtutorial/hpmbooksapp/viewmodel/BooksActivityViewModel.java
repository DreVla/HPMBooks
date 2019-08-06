package com.hpmtutorial.hpmbooksapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.hpmtutorial.hpmbooksapp.model.Book;
import com.hpmtutorial.hpmbooksapp.model.network.BookAPI;
import com.hpmtutorial.hpmbooksapp.model.network.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BooksActivityViewModel extends ViewModel {

    private BookAPI bookAPI;
    private List<Book> receivedBooks;
    private MutableLiveData<List<Book>> mutableLiveDataBooks;

    public MutableLiveData<List<Book>> getBookList(){
        if(mutableLiveDataBooks == null){
            mutableLiveDataBooks = new MutableLiveData<>();
        }
        return mutableLiveDataBooks;
    }

    public void sendGet(String token){
        bookAPI = RetrofitClient.getRetrofitBooks(token).create(BookAPI.class);
        bookAPI.getAllBooks(token).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                receivedBooks = new ArrayList<>();
//                Log.d("FirstBook", "onResponse: " + response.body().get(0).toString());
                mutableLiveDataBooks.setValue(response.body());
//                Log.d("FirstBook", "onResponse: " + mutableLiveDataBooks.getValue().get(0).toString());
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("Fail", "onFailure: Something wrong");
            }
        });
    }
}
