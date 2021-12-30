package ae.businessdna.iconference.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Project : iConference
 * Created by rohith on 7/13/2017.
 */

public class MeetingDetail implements Parcelable {
    public Long _id;
//    public String meetingDetailId;
//    public String meetingId;
//    public String recipientBadgeNo;
//    public String isOrganiser;
//    public String isActive;
//    public String createdBy;
//    public String createdDate;

    public String badgeNo;
    public String compName;
    public String companyID;
    public String empName;
    public String endTime;
    public String floorID;
    public String floorName;
    public String isOrganizer;
    public String meetingDate;
    public String meetingDesc;
    public String meetingID;
    public String meetingName;
    public String meetingStatus;
    public String roomID;
    public String roomName;
    public String startTime;

    public MeetingDetail() {
    }

    protected MeetingDetail(Parcel in) {
        badgeNo = in.readString();
        compName = in.readString();
        companyID = in.readString();
        empName = in.readString();
        endTime = in.readString();
        floorID = in.readString();
        floorName = in.readString();
        isOrganizer = in.readString();
        meetingDate = in.readString();
        meetingDesc = in.readString();
        meetingID = in.readString();
        meetingName = in.readString();
        meetingStatus = in.readString();
        roomID = in.readString();
        roomName = in.readString();
        startTime = in.readString();
    }

    public static final Creator<MeetingDetail> CREATOR = new Creator<MeetingDetail>() {
        @Override
        public MeetingDetail createFromParcel(Parcel in) {
            return new MeetingDetail(in);
        }

        @Override
        public MeetingDetail[] newArray(int size) {
            return new MeetingDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(badgeNo);
        dest.writeString(compName);
        dest.writeString(companyID);
        dest.writeString(empName);
        dest.writeString(endTime);
        dest.writeString(floorID);
        dest.writeString(floorName);
        dest.writeString(isOrganizer);
        dest.writeString(meetingDate);
        dest.writeString(meetingDesc);
        dest.writeString(meetingID);
        dest.writeString(meetingName);
        dest.writeString(meetingStatus);
        dest.writeString(roomID);
        dest.writeString(roomName);
        dest.writeString(startTime);
    }
}
