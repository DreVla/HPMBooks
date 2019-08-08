package com.hpmtutorial.hpmbooksapp.model.network;

import com.hpmtutorial.hpmbooksapp.model.Book;
import com.hpmtutorial.hpmbooksapp.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BookAPI {

//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/books")
    Call<List<Book>> getAllBooks(@Header("Authorization") String authHeader);

    @Headers({"Accept: application/json"})
    @POST("api/books/")
    Call<ResponseBody> addNewBook(@Header("Authorization") String authHeader, @Body Book book);
}
