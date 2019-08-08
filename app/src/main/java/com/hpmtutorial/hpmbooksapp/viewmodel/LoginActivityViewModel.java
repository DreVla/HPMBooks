package com.hpmtutorial.hpmbooksapp.viewmodel;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.hpmtutorial.hpmbooksapp.model.Login;
import com.hpmtutorial.hpmbooksapp.model.User;
import com.hpmtutorial.hpmbooksapp.model.network.RetrofitClient;
import com.hpmtutorial.hpmbooksapp.model.network.ServerError;
import com.hpmtutorial.hpmbooksapp.model.network.UserAPI;

import org.json.JSONObject;

import java.io.IOException;
import java.util.prefs.Preferences;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends ViewModel {

    public MutableLiveData<String> uiLiveData = new MutableLiveData<>();
    public MutableLiveData<String>
            email = new MutableLiveData<>(),
            password = new MutableLiveData<>(),
            emailError = new MutableLiveData<>(),
            passwordError = new MutableLiveData<>();
    public MutableLiveData<String> serverError = new MutableLiveData<>();
    private String token;


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
                    uiLiveData.setValue(token);
                } else {
                    try {
                        Gson gson = new Gson();
                        ServerError error=gson.fromJson(response.errorBody().charStream(),ServerError.class);
                        serverError.setValue(error.getError());
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


    public void onLoginClick(){
        emailError.setValue(null);
        passwordError.setValue(null);
        if (email.getValue() == null || password.getValue() == null) {
            passwordError.setValue("Fill both fields!");
            emailError.setValue("Fill both fields!");
        } else {
            if(!Patterns.EMAIL_ADDRESS.matcher(email.getValue()).matches()) {
                emailError.setValue("Please input valid email!");
            } else {
                sendPost(email.getValue(), password.getValue());
            }
        }
    }

}
