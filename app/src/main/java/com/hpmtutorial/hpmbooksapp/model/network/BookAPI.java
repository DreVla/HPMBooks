package com.hpmtutorial.hpmbooksapp.model.network;

import com.hpmtutorial.hpmbooksapp.model.Book;
import com.hpmtutorial.hpmbooksapp.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BookAPI {

//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/books")
    Call<List<Book>> getAllBooks(@Header("Authorization") String authHeader);

    @Headers({"Accept: application/json"})
    @POST("api/books/")
    Call<ResponseBody> addNewBook(@Header("Authorization") String authHeader, @Body Book book);

    @GET("api/books/{book_id}")
    Call<Book> findById(@Header("Authorization") String authHeader, @Path("book_id")String bookId);

    @Headers({"Accept: application/json"})
    @PUT("api/books/{book_id}")
    Call<ResponseBody> updateBook(@Header("Authorization") String authHeader, @Path("book_id")String bookId, @Body Book book);

    @DELETE("api/books/{book_id}")
    Call<ResponseBody> deleteBook(@Header("Authorization") String authHeader, @Path("book_id")String bookId);
}
