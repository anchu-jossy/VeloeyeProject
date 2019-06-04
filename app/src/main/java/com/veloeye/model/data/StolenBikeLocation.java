package com.veloeye.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StolenBikeLocation implements Parcelable {
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("status")
    @Expose
    private String status;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.response);
        dest.writeString(this.userid);
        dest.writeString(this.status);
    }

    public StolenBikeLocation() {

    }

    protected StolenBikeLocation(Parcel in) {
        this.response = in.readString();
        this.userid = in.readString();
        this.status = in.readString();
    }

    public static final Parcelable.Creator<StolenBikeLocation> CREATOR = new Parcelable.Creator<StolenBikeLocation>() {
        @Override
        public StolenBikeLocation createFromParcel(Parcel source) {
            return new StolenBikeLocation(source);
        }

        @Override
        public StolenBikeLocation[] newArray(int size) {
            return new StolenBikeLocation[size];
        }
    };
}
