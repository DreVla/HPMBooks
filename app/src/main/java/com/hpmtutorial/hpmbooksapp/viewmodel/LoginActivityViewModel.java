package com.hpmtutorial.hpmbooksapp.viewmodel;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.hpmtutorial.hpmbooksapp.model.Login;
import com.hpmtutorial.hpmbooksapp.model.User;
import com.hpmtutorial.hpmbooksapp.model.network.RetrofitClient;
import com.hpmtutorial.hpmbooksapp.model.network.ServerError;
import com.hpmtutorial.hpmbooksapp.model.network.UserAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends ViewModel {

    public enum UIChange{
        LOAD,
        CHANGE,
        REGISTER,
        NOACTION
    }

    public MutableLiveData<UIChange> uiLiveData = new MutableLiveData<>();
    public MutableLiveData<String> tokenReceived = new MutableLiveData<>();
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
                    tokenReceived.setValue(token);
                    uiLiveData.setValue(UIChange.CHANGE);
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
                serverError.setValue("Login Failed");
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
                uiLiveData.setValue(UIChange.LOAD);
                sendPost(email.getValue(), password.getValue());
            }
        }
    }

    public void onRegisterClick(){
        uiLiveData.setValue(UIChange.REGISTER);
    }

}
