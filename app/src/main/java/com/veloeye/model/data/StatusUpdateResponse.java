package com.veloeye.model.data;

/**
 * Created by Florin Mihalache on 17.09.2015.
 */
public class StatusUpdateResponse {
    private String userid;
    private String status;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
