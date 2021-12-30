package ae.businessdna.iconference.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Project : iConference
 * Created by rohith on 7/13/2017.
 */

public class MeetingHeader implements Parcelable {
    public Long _id;
//    public String meetingID;
//    public String companyID;
//    public String floorID;
//    public String roomID;
//    public String subject;
//    public String createdDate;
//    public String startTime;
//    public String endTime;
//    public String bodyPreview;
//    public String meetingStatus;
//    public String isActive;
//    public String createdBy;
//    public String createdDate;

    public String meetingID;
//    public String companyID;
//    public String floorID;
//    public String roomID;
    public String createdDate;
    public String subject;
    public String bodyPreview;
    public boolean isAllDay;
    public boolean isCancelled;
    public String meetingStatus;
    public String startTime;
    public String endTime;
    public String location;

//    public String createdBy;
    public String createdByName;

    public MeetingHeader() {
    }

    protected MeetingHeader(Parcel in) {
        meetingID = in.readString();
//        companyID = in.readString();
//        floorID = in.readString();
//        roomID = in.readString();
        createdDate = in.readString();
        subject = in.readString();
        bodyPreview = in.readString();
        isAllDay = in.readByte() != 0;
        isCancelled = in.readByte() != 0;
        meetingStatus = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        location = in.readString();
//        createdBy = in.readString();
        createdByName = in.readString();
    }

    public static final Creator<MeetingHeader> CREATOR = new Creator<MeetingHeader>() {
        @Override
        public MeetingHeader createFromParcel(Parcel in) {
            return new MeetingHeader(in);
        }

        @Override
        public MeetingHeader[] newArray(int size) {
            return new MeetingHeader[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(meetingID);
//        dest.writeString(companyID);
//        dest.writeString(floorID);
//        dest.writeString(roomID);
        dest.writeString(createdDate);
        dest.writeString(subject);
        dest.writeString(bodyPreview);
        dest.writeByte((byte) (isAllDay ? 1 : 0));
        dest.writeByte((byte) (isCancelled ? 1 : 0));
        dest.writeString(meetingStatus);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(location);
//        dest.writeString(createdBy);
        dest.writeString(createdByName);
    }

}
