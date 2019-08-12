package com.hpmtutorial.hpmbooksapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.hpmtutorial.hpmbooksapp.model.Book;
import com.hpmtutorial.hpmbooksapp.model.network.BookAPI;
import com.hpmtutorial.hpmbooksapp.model.network.RetrofitClient;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsViewModel extends ViewModel {

    public enum uiStatus{
        LOADING,
        DONE
    }
    private BookAPI bookAPI;
    public MutableLiveData<uiStatus> uiStatusMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String>
            title = new MutableLiveData<>(),
            author = new MutableLiveData<>(),
            publisher = new MutableLiveData<>();

    public void sendGet(String token, String bookId){
        uiStatusMutableLiveData.setValue(uiStatus.LOADING);
        bookAPI = RetrofitClient.getRetrofitBooks(token).create(BookAPI.class);
        bookAPI.findById(token, bookId).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if(response.isSuccessful()){
                    Log.d("GetBookSucces", "onResponse: " + response.body().toString());
                    Book book = response.body();
                    title.setValue(book.getTitle());
                    author.setValue(book.getAuthor());
                    publisher.setValue(book.getPublisher());
                    uiStatusMutableLiveData.setValue(uiStatus.DONE);
                } else {
                    uiStatusMutableLiveData.setValue(uiStatus.DONE);
                    try {
                        Log.d("GetBookError", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.d("Fail", "onFailure: Something wrong");
            }
        });
    }

    public void sendPut(String token, String bookId){
        uiStatusMutableLiveData.setValue(uiStatus.LOADING);
        bookAPI = RetrofitClient.getRetrofitBooks(token).create(BookAPI.class);
        Book updatedBook = new Book(title.getValue(), author.getValue(), publisher.getValue());
        bookAPI.updateBook(token, bookId, updatedBook).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    uiStatusMutableLiveData.setValue(uiStatus.DONE);
                    try {
                        Log.d("UpdateBookSuccess", "onResponse: " + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    uiStatusMutableLiveData.setValue(uiStatus.DONE);
                    try {
                        Log.d("UpdateBookError", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Fail", "onFailure: Something wrong");
            }
        });
    }
}
