package com.veloeye.model.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Florin Mihalache on 14.09.2015.
 */
public class Bike implements Parcelable {
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("bikeid")
    @Expose
    private String bikeid;
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("makename")
    @Expose
    private String makename;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("qrcode")
    @Expose
    private String qrcode;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("framecode")
    @Expose
    private String framecode;
    @SerializedName("year")
    @Expose
    private Object year;
    @SerializedName("scans")
    @Expose
    private List<Scan> scans = null;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    protected Bike(Parcel in) {
        userid = in.readString();
        bikeid = in.readString();
        make = in.readString();
        makename = in.readString();
        model = in.readString();
        status = in.readString();
        image = in.readString();
        qrcode = in.readString();
        color = in.readString();
        framecode = in.readString();
    }

    public static final Creator<Bike> CREATOR = new Creator<Bike>() {
        @Override
        public Bike createFromParcel(Parcel in) {
            return new Bike(in);
        }

        @Override
        public Bike[] newArray(int size) {
            return new Bike[size];
        }
    };

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBikeid() {
        return bikeid;
    }

    public void setBikeid(String bikeid) {
        this.bikeid = bikeid;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getMakename() {
        return makename;
    }

    public void setMakename(String makename) {
        this.makename = makename;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFramecode() {
        return framecode;
    }

    public void setFramecode(String framecode) {
        this.framecode = framecode;
    }

    public Object getYear() {
        return year;
    }

    public void setYear(Object year) {
        this.year = year;
    }

    public List<Scan> getScans() {
        return scans;
    }

    public void setScans(List<Scan> scans) {
        this.scans = scans;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeString(bikeid);
        dest.writeString(make);
        dest.writeString(makename);
        dest.writeString(model);
        dest.writeString(status);
        dest.writeString(image);
        dest.writeString(qrcode);
        dest.writeString(color);
        dest.writeString(framecode);
    }
}


class Scan {

    @SerializedName("scandatetime")
    @Expose
    private String scandatetime;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("lat")
    @Expose
    private String lat;

    public String getScandatetime() {
        return scandatetime;
    }

    public void setScandatetime(String scandatetime) {
        this.scandatetime = scandatetime;
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
}
