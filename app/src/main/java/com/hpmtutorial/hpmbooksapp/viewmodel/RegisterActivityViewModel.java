package com.hpmtutorial.hpmbooksapp.viewmodel;

import android.app.Application;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hpmtutorial.hpmbooksapp.R;
import com.hpmtutorial.hpmbooksapp.model.User;
import com.hpmtutorial.hpmbooksapp.model.network.RetrofitClient;
import com.hpmtutorial.hpmbooksapp.model.network.ServerError;
import com.hpmtutorial.hpmbooksapp.model.network.UserAPI;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivityViewModel extends AndroidViewModel {

    public enum UIChange{
        SUCCES,
        LOADING,
        FAILED,
        NOACTION
    }
    private UserAPI userAPI;
    public MutableLiveData<UIChange> uiLiveData = new MutableLiveData<>();
    public MutableLiveData<ErrorStatus> errorMessage = new MutableLiveData<>();
    public MutableLiveData<String>
            username = new MutableLiveData<>(),
            email = new MutableLiveData<>(),
            password = new MutableLiveData<>(),
            passwordCheck = new MutableLiveData<>();
    public MutableLiveData<String> serverError = new MutableLiveData<>();
    private int i = 1;

    public RegisterActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void sendPost(String username, final String email, String password) {
        uiLiveData.setValue(UIChange.LOADING);
        userAPI = RetrofitClient.getRetrofitInstance().create(UserAPI.class);
        User newUser = new User(username, email, password);
        Log.d("Register User", "sendPost: " + newUser.toString());
        userAPI.registerUser(newUser).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showResponse(response.body().toString());
                    uiLiveData.setValue(UIChange.SUCCES);
                    Log.i("Register User", "User submitted to API. " + response.body().toString());
                } else {
                    uiLiveData.setValue(UIChange.FAILED);
                    try {
                        Gson gson = new Gson();
                        ServerError error=gson.fromJson(response.errorBody().charStream(),ServerError.class);
                        serverError.setValue(error.getError());
                    } catch (Exception e) {
                        Log.d("RegisterException", "onResponse: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                uiLiveData.setValue(UIChange.FAILED);
                Log.e("Register User", "Unable to submit user to API. ");
            }
        });
    }

    public String showResponse(String response) {
        Log.d("Register User", "showResponse: " + response);
        return response;
    }

    public enum ErrorStatus{
        FILL_ALL,
        FILL_USER,
        FILL_EMAIL,
        FILL_PASSWORD,
        FILL_PASSWORDCHECK,
        INVALID_MAIL,
        PASSWORDS_NO_MATCH,
        SUCCES
    }

    public void onRegisterClick() {
        if (username.getValue() == null && email.getValue() == null && password.getValue() == null && passwordCheck.getValue() == null) {
            errorMessage.setValue(ErrorStatus.FILL_ALL);
        } else if (username.getValue() == null) {
            errorMessage.setValue(ErrorStatus.FILL_USER);
        } else if (email.getValue() == null) {
            errorMessage.setValue(ErrorStatus.FILL_EMAIL);
        } else if (password.getValue() == null) {
            errorMessage.setValue(ErrorStatus.FILL_PASSWORD);
        }else if(passwordCheck.getValue() == null){
            errorMessage.setValue(ErrorStatus.FILL_PASSWORDCHECK);
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(email.getValue()).matches()) {
                errorMessage.setValue(ErrorStatus.INVALID_MAIL);
            } else if (password.getValue().equals(passwordCheck.getValue())) {
                sendPost(username.getValue(), email.getValue(), password.getValue());
                errorMessage.setValue(ErrorStatus.SUCCES);
            } else {
                errorMessage.setValue(ErrorStatus.PASSWORDS_NO_MATCH);
            }
        }
    }
}
