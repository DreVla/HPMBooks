package com.hpmtutorial.hpmbooksapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hpmtutorial.hpmbooksapp.model.User;
import com.hpmtutorial.hpmbooksapp.model.network.RetrofitClient;
import com.hpmtutorial.hpmbooksapp.model.network.UserAPI;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends ViewModel {

    private UserAPI userAPI;
    private MutableLiveData<Integer> uiLiveData;
    private int i = 1;

    public MutableLiveData<Integer> getCurrentUi(){
        if(uiLiveData == null){
            uiLiveData = new MutableLiveData<>();
        }
        return uiLiveData;
    }

    public void sendPost(String email, String password) {
        userAPI = RetrofitClient.getRetrofitInstance().create(UserAPI.class);
        Log.d("Login", "sendPost: " + email + " " + password);
        userAPI.loginUser(email, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());
                    getCurrentUi().setValue(++i);
                    Log.i("Login User","Login success " + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Login User", "Login failed");
            }
        });
    }

    public String showResponse(String response) {
        Log.d("Login User", "showResponse: " + response);
        return response;
    }


}
