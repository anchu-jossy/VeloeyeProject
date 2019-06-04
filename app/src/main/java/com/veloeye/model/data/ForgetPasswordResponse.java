package com.veloeye.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgetPasswordResponse {

    @SerializedName("response")
    @Expose
    private Integer response;
    @SerializedName("userid")
    @Expose
    private Integer userid;

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
