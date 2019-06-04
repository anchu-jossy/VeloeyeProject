package com.veloeye.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image implements  Parcelable {

    @SerializedName("bikeid")
    @Expose
    private String bikeid;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("imgid")
    @Expose
    private Object imgid;

    protected Image(Parcel in) {
        bikeid = in.readString();
        img = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getBikeid() {
        return bikeid;
    }

    public void setBikeid(String bikeid) {
        this.bikeid = bikeid;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Object getImgid() {
        return imgid;
    }

    public void setImgid(Object imgid) {
        this.imgid = imgid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bikeid);
        dest.writeString(img);
    }
}
