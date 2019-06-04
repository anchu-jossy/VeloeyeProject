package com.veloeye.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Enquiry implements Parcelable {
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("sent")
    @Expose
    private String sent;

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

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.response);
        dest.writeString(this.userid);
        dest.writeString(this.sent);
    }

    public Enquiry() {
    }

    protected Enquiry(Parcel in) {
        this.response = in.readString();
        this.userid = in.readString();
        this.sent = in.readString();
    }

    public static final Parcelable.Creator<Enquiry> CREATOR = new Parcelable.Creator<Enquiry>() {
        @Override
        public Enquiry createFromParcel(Parcel source) {
            return new Enquiry(source);
        }

        @Override
        public Enquiry[] newArray(int size) {
            return new Enquiry[size];
        }
    };
}
