package com.coderschool.nytimes.Model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 6/21/2017.
 */

public class ApiResponse {
    @SerializedName("response")
    private JsonObject response;

    @SerializedName("status")
    private String status;

    public JsonObject getResponse() {
        if(null == response){
            return new JsonObject();
        }
        return response;
    }

    public String getStatus() {
        return status;
    }
}
