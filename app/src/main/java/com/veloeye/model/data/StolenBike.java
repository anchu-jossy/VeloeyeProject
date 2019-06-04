package com.veloeye.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StolenBike implements Parcelable {
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("bikeinfo")
    @Expose
    private String bikeinfo;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("datereported")
    @Expose
    private String datereported;
    @SerializedName("status")
    @Expose
    private String status;

    public StolenBike(Parcel in) {
        distance = in.readString();
        lng = in.readString();
        lat = in.readString();
        bikeinfo = in.readString();
        image = in.readString();
        datereported = in.readString();
        status = in.readString();
    }

    public static final Creator<StolenBike> CREATOR = new Creator<StolenBike>() {
        @Override
        public StolenBike createFromParcel(Parcel in) {
            return new StolenBike(in);
        }

        @Override
        public StolenBike[] newArray(int size) {
            return new StolenBike[size];
        }
    };

    public StolenBike() {

    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getBikeinfo() {
        return bikeinfo;
    }

    public void setBikeinfo(String bikeinfo) {
        this.bikeinfo = bikeinfo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDatereported() {
        return datereported;
    }

    public void setDatereported(String datereported) {
        this.datereported = datereported;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(distance);
        dest.writeString(lng);
        dest.writeString(lat);
        dest.writeString(bikeinfo);
        dest.writeString(image);
        dest.writeString(datereported);
        dest.writeString(status);
    }
}
