package com.hpmtutorial.hpmbooksapp.model.network;

import com.hpmtutorial.hpmbooksapp.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserAPI {

    @Headers({"Accept: application/json"})
    @POST("/api/users/register")
    Call<ResponseBody> registerUser(@Body User user);

    @FormUrlEncoded
    @POST("api/users/login")
    Call<ResponseBody> loginUser(@Field("email") String email,
                                 @Field("password") String password);

}
