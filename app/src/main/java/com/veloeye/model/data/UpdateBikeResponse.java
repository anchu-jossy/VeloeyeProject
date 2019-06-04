package com.veloeye.model.data;

/**
 * Created by Florin Mihalache on 17.09.2015.
 */
public class UpdateBikeResponse {
    private String bikeid;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    private String  response;
    public String getBike_status() {
        return bike_status;
    }

    public void setBike_status(String bike_status) {
        this.bike_status = bike_status;
    }

    private String bike_status;

    public String getBikeid() {
        return bikeid;
    }

    public void setBikeid(String bikeid) {
        this.bikeid = bikeid;
    }
}
