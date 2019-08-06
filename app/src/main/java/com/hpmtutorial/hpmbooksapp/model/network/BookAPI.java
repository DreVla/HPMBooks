package com.hpmtutorial.hpmbooksapp.model.network;

import com.hpmtutorial.hpmbooksapp.model.Book;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface BookAPI {

//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/books")
    Call<List<Book>> getAllBooks(@Header("Authorization") String authHeader);
}
