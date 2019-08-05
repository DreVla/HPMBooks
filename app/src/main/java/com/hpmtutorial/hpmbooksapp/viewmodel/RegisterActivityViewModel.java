package com.hpmtutorial.hpmbooksapp.viewmodel;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hpmtutorial.hpmbooksapp.model.User;
import com.hpmtutorial.hpmbooksapp.model.network.UserAPI;
import com.hpmtutorial.hpmbooksapp.model.network.RetrofitClient;
import com.hpmtutorial.hpmbooksapp.view.LoginActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivityViewModel extends ViewModel {

    private UserAPI userAPI;
    private MutableLiveData<Integer> uiLiveData;
    private int i = 1;

    public MutableLiveData<Integer> getCurrentUi(){
        if(uiLiveData == null){
            uiLiveData = new MutableLiveData<>();
        }
        return uiLiveData;
    }

    public void sendPost(String username, String email, String password) {
        userAPI = RetrofitClient.getRetrofitInstance().create(UserAPI.class);
        User newUser = new User(username,email,password);
        Log.d("Register User", "sendPost: " + newUser.toString());
        userAPI.registerUser(newUser).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());
                    getCurrentUi().setValue(++i);
                    Log.i("Register User", "User submitted to API. " + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Register User", "Unable to submit user to API. ");
            }
        });
    }

    public String showResponse(String response) {
        Log.d("Register User", "showResponse: " + response);
        return response;
    }
}
