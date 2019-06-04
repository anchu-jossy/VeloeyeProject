package com.veloeye.model.data;

import java.io.Serializable;

/**
 * Created by Florin on 15.01.2016.
 */
public class ScanLocation implements Serializable {
    private String scandatetime;
    private double lat;
    private double lng;

    public String getScandatetime() {
        return scandatetime;
    }

    public void setScandatetime(String scandatetime) {
        this.scandatetime = scandatetime;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
