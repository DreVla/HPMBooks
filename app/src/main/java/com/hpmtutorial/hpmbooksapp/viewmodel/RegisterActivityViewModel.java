package com.hpmtutorial.hpmbooksapp.viewmodel;

import android.app.Application;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.hpmtutorial.hpmbooksapp.R;
import com.hpmtutorial.hpmbooksapp.model.User;
import com.hpmtutorial.hpmbooksapp.model.network.RetrofitClient;
import com.hpmtutorial.hpmbooksapp.model.network.UserAPI;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivityViewModel extends AndroidViewModel {

    private UserAPI userAPI;
    public MutableLiveData<Integer> uiLiveData = new MutableLiveData<>();
    public MutableLiveData<ErrorStatus> errorMessage = new MutableLiveData<ErrorStatus>();
    public MutableLiveData<String>
            username = new MutableLiveData<>(),
            email = new MutableLiveData<>(),
            password = new MutableLiveData<>(),
            passwordCheck = new MutableLiveData<>(),
            usernameError = new MutableLiveData<>(),
            emailError = new MutableLiveData<>(),
            passwordCheckError = new MutableLiveData<>(),
            passwordError = new MutableLiveData<>();
    private int i = 1;

    public RegisterActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void sendPost(String username, String email, String password) {
        userAPI = RetrofitClient.getRetrofitInstance().create(UserAPI.class);
        User newUser = new User(username, email, password);
        Log.d("Register User", "sendPost: " + newUser.toString());
        userAPI.registerUser(newUser).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showResponse(response.body().toString());
                    uiLiveData.setValue(++i);
                    Log.i("Register User", "User submitted to API. " + response.body().toString());
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Log.d("RegisterErrorMessage", "onResponse: " + jObjError.getJSONObject("error").getString("message"));
                    } catch (Exception e) {
                        Log.d("RegisterException", "onResponse: " + e.getMessage());
                    }
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
        emailError.setValue(null);
        passwordCheckError.setValue(null);
        passwordError.setValue(null);
        usernameError.setValue(null);
        if (username.getValue() == null && email.getValue() == null && password.getValue() == null && passwordCheckError.getValue() == null) {

            errorMessage.setValue(ErrorStatus.FILL_ALL);

            usernameError.setValue(getApplication().getResources().getString(R.string.fill_fields));
            emailError.setValue(getApplication().getResources().getString(R.string.fill_fields));
            passwordError.setValue(getApplication().getResources().getString(R.string.fill_fields));
            passwordCheckError.setValue(getApplication().getResources().getString(R.string.fill_fields));
        } else if (username.getValue() == null) {
            errorMessage.setValue(ErrorStatus.FILL_USER);
            usernameError.setValue(getApplication().getResources().getString(R.string.register_username));
        } else if (email.getValue() == null) {
            errorMessage.setValue(ErrorStatus.FILL_EMAIL);
            emailError.setValue(getApplication().getResources().getString(R.string.register_email));
        } else if (password.getValue() == null) {
            errorMessage.setValue(ErrorStatus.FILL_PASSWORD);
            passwordError.setValue(getApplication().getResources().getString(R.string.register_password));
        }else if(passwordCheck.getValue() == null){
            errorMessage.setValue(ErrorStatus.FILL_PASSWORDCHECK);
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(email.getValue()).matches()) {
                errorMessage.setValue(ErrorStatus.INVALID_MAIL);
                emailError.setValue(getApplication().getResources().getString(R.string.register_email));
            } else if (password.getValue().equals(passwordCheck.getValue())) {
                sendPost(username.getValue(), email.getValue(), password.getValue());
                errorMessage.setValue(ErrorStatus.SUCCES);
            } else {
                errorMessage.setValue(ErrorStatus.PASSWORDS_NO_MATCH);
                passwordError.setValue(getApplication().getResources().getString(R.string.passwords_no_match));
                passwordCheckError.setValue(getApplication().getResources().getString(R.string.passwords_no_match));
            }
        }
    }
}
