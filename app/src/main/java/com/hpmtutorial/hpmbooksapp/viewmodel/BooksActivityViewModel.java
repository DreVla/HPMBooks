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

    public enum uiChange{
        LOADING,
        DONE,
        ADD_BOOK,
        DELETED_BOOK,
        FAILED
    }

    private BookAPI bookAPI;
    public MutableLiveData<List<Book>> mutableLiveDataBooks = new MutableLiveData<>();
    public MutableLiveData<uiChange> uiChangeMutableLiveData = new MutableLiveData<>();

    public void sendGet(String token){
        uiChangeMutableLiveData.setValue(uiChange.LOADING);
        bookAPI = RetrofitClient.getRetrofitBooks(token).create(BookAPI.class);
        bookAPI.getAllBooks(token).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                mutableLiveDataBooks.setValue(response.body());
                uiChangeMutableLiveData.setValue(uiChange.DONE);
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("Fail", "onFailure: Something wrong");
            }
        });
    }

    public void sendDelete(String token, String id){
        bookAPI = RetrofitClient.getRetrofitBooks(token).create(BookAPI.class);
        bookAPI.deleteBook(token, id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("DeleteSucces", "onResponse: Delete success");
                    uiChangeMutableLiveData.setValue(uiChange.DELETED_BOOK);
                }else{
                    uiChangeMutableLiveData.setValue(uiChange.FAILED);
                    Log.d("Delete failed", "onResponse: Delete failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("DeleteBook", "Unable to delete book!");
            }

        });
    }

    public void onAddClick(){
        uiChangeMutableLiveData.setValue(uiChange.ADD_BOOK);
        Log.d("VM button", "onAddClick: pressed button");
    }
}
