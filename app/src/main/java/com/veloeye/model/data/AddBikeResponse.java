package com.veloeye.model.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Florin Mihalache on 17.09.2015.
 */
public class AddBikeResponse implements Parcelable {
    private String response;
    private String bikeid;
    private String status;
    private String userid;

    protected AddBikeResponse(Parcel in) {
        response = in.readString();
        bikeid = in.readString();
        status = in.readString();
        userid = in.readString();
    }

    public static final Creator<AddBikeResponse> CREATOR = new Creator<AddBikeResponse>() {
        @Override
        public AddBikeResponse createFromParcel(Parcel in) {
            return new AddBikeResponse(in);
        }

        @Override
        public AddBikeResponse[] newArray(int size) {
            return new AddBikeResponse[size];
        }
    };

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(response);
        dest.writeString(bikeid);
        dest.writeString(status);
        dest.writeString(userid);
    }
}
