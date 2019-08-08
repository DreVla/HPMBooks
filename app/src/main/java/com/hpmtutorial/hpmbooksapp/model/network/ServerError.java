package com.hpmtutorial.hpmbooksapp.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerError {

    @SerializedName("error")
    @Expose
    private String error;
    public ServerError() {
    }

    public ServerError(String message) {
        super();
        this.error = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String message) {
        this.error = message;
    }

}