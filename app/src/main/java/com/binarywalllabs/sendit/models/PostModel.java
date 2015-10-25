package com.binarywalllabs.sendit.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.binarywalllabs.sendit.constants.AppConstants;
import com.binarywalllabs.sendit.utils.Apputility;

import java.util.Date;

/**
 * Created by Arun on 18-10-2015.
 */
public class PostModel implements Parcelable{
    public long postId;
    public String title;
    public String body;
    public String type;
    public Date createdAt;


    protected PostModel(Parcel in) {
        postId = in.readLong();
        title = in.readString();
        body = in.readString();
        type = in.readString();
        createdAt = Apputility.getDateFromString(in.readString(), AppConstants.WEB_SERVICE_DATE_FORMAT);
    }

    public static final Creator<PostModel> CREATOR = new Creator<PostModel>() {
        @Override
        public PostModel createFromParcel(Parcel in) {
            return new PostModel(in);
        }

        @Override
        public PostModel[] newArray(int size) {
            return new PostModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(postId);
        parcel.writeString(title);
        parcel.writeString(body);
        parcel.writeString(type);
        parcel.writeString(Apputility.getStringFromDate(createdAt, AppConstants.WEB_SERVICE_DATE_FORMAT));
    }
}
