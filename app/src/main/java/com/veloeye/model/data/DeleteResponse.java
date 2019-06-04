package com.veloeye.model.data;

public class DeleteResponse {
    private String response;
    private String bikeid;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getBikeid() {
        return bikeid;
    }

    public void setBikeid(String bikeid) {
        this.bikeid = bikeid;
    }

    public String getBike_status() {
        return bike_status;
    }

    public void setBike_status(String bike_status) {
        this.bike_status = bike_status;
    }

    private String bike_status;

}
