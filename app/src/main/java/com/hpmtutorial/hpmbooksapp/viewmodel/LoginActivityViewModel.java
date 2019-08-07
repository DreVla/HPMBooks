package com.hpmtutorial.hpmbooksapp.viewmodel;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hpmtutorial.hpmbooksapp.model.Login;
import com.hpmtutorial.hpmbooksapp.model.User;
import com.hpmtutorial.hpmbooksapp.model.network.RetrofitClient;
import com.hpmtutorial.hpmbooksapp.model.network.UserAPI;

import org.json.JSONObject;

import java.io.IOException;
import java.util.prefs.Preferences;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends ViewModel {

    private MutableLiveData<String> uiLiveData;
    public String email, password;
    private String token;

    public MutableLiveData<String> getCurrentUi(){
        if(uiLiveData == null){
            uiLiveData = new MutableLiveData<>();
        }
        return uiLiveData;
    }

    public void sendPost(String email, String password) {
        UserAPI userAPI = RetrofitClient.getRetrofitInstance().create(UserAPI.class);
        Login login = new Login(email, password);
        userAPI.loginUser(login).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    token = response.body().getToken();
                    if(token != null){
                        //use your header value
                        Log.d("Login Token", "onResponse: token is " + token);
                    }
                    getCurrentUi().setValue(token);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Log.d("LoginErrorMessage", "onResponse: " + jObjError.getJSONObject("error").getString("message"));
                    } catch (Exception e) {
                        Log.d("LoginErrorMessage", "onResponse: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Login User", "Login failed");
            }
        });
    }

    public String showResponse(String response) {
        return response;
    }


}
