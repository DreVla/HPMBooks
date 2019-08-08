package com.hpmtutorial.hpmbooksapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.hpmtutorial.hpmbooksapp.model.Book;
import com.hpmtutorial.hpmbooksapp.model.User;
import com.hpmtutorial.hpmbooksapp.model.network.BookAPI;
import com.hpmtutorial.hpmbooksapp.model.network.RetrofitClient;
import com.hpmtutorial.hpmbooksapp.model.network.ServerError;
import com.hpmtutorial.hpmbooksapp.model.network.UserAPI;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBookActivityViewModel extends ViewModel {

    private BookAPI bookAPI;
    public MutableLiveData<String> mutableLiveDataServerError = new MutableLiveData<>();
    public MutableLiveData<uiError> mutableLiveDataUiError = new MutableLiveData<>();
    public MutableLiveData<String>
            title = new MutableLiveData<>(),
            author = new MutableLiveData<>(),
            publisher = new MutableLiveData<>();
    private String token;

    public void sendPost(String token) {
        bookAPI = RetrofitClient.getRetrofitInstance().create(BookAPI.class);
        Book newBook = new Book(title.getValue(), author.getValue(), publisher.getValue());
        Log.d("Register User", "sendPost: " + newBook.toString());
        bookAPI.addNewBook(token, newBook).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i("AddBook", "Book added. " + response.body().toString());
                    mutableLiveDataUiError.setValue(uiError.SUCCESS);
                } else {
                    try {
                        Gson gson = new Gson();
                        Log.d("AddBookError", "onResponse: " + response.errorBody().string());
                        ServerError error=gson.fromJson(response.errorBody().charStream(),ServerError.class);
                        mutableLiveDataServerError.setValue(error.getError());
                    } catch (Exception e) {
                        Log.d("AddBook Exception", "onResponse: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("AddBook", "Unable to submit book to API. ");
            }
        });
    }

    public String showResponse(String response) {
        Log.d("Addbook", "showResponse: " + response);
        return response;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public enum uiError{
        FILL_FIELDS,
        SUCCESS
    }

    public void onAddBookClick(){
        if(title.getValue() == null || author.getValue() == null || publisher.getValue() == null){
            mutableLiveDataUiError.setValue(uiError.FILL_FIELDS);
        } else {
            sendPost(token);
        }
    }

}
