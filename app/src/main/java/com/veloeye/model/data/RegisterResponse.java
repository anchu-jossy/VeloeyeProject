package com.veloeye.model.data;

/**
 * Created by Florin Mihalache on 14.09.2015.
 */
public class RegisterResponse {
    private int response;
    private int userid;
    private String username;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
