package com.hpmtutorial.hpmbooksapp.model.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BooksAPI {

    @POST("/api/users/register")
    Call<ResponseBody> registerUser(@Field("username") String username,
                                    @Field("email") String email,
                                    @Field("password") String password);
}
