package com.veloeye.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransferCheckResponse {

    @SerializedName("response")
    @Expose
    private Integer response;
    @SerializedName("validation")
    @Expose
    private String validation;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("newuser")
    @Expose
    private Integer newuser;
    @SerializedName("bikeid")
    @Expose
    private String bikeid;
    @SerializedName("txcode")
    @Expose
    private String txcode;
    @SerializedName("email")
    @Expose
    private String email;

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getNewuser() {
        return newuser;
    }

    public void setNewuser(Integer newuser) {
        this.newuser = newuser;
    }

    public String getBikeid() {
        return bikeid;
    }

    public void setBikeid(String bikeid) {
        this.bikeid = bikeid;
    }

    public String getTxcode() {
        return txcode;
    }

    public void setTxcode(String txcode) {
        this.txcode = txcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
