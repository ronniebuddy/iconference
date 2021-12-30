package ae.businessdna.iconference.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Project : iConference
 * Created by rohith on 7/19/2017.
 */

public class MeetingDay implements Parcelable {
    public String companyID;
    public String endTime;
    public String floorID;
    public String meetingDate;
    public String meetingDesc;
    public String meetingID;
    public String meetingName;
    public String meetingStatus;
    public String roomID;
    public String startTime;
    public String createdByName;

    public MeetingDay() {
    }

    protected MeetingDay(Parcel in) {
        companyID = in.readString();
        endTime = in.readString();
        floorID = in.readString();
        meetingDate = in.readString();
        meetingDesc = in.readString();
        meetingID = in.readString();
        meetingName = in.readString();
        meetingStatus = in.readString();
        roomID = in.readString();
        startTime = in.readString();
        createdByName = in.readString();
    }

    public static final Creator<MeetingDay> CREATOR = new Creator<MeetingDay>() {
        @Override
        public MeetingDay createFromParcel(Parcel in) {
            return new MeetingDay(in);
        }

        @Override
        public MeetingDay[] newArray(int size) {
            return new MeetingDay[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(companyID);
        dest.writeString(endTime);
        dest.writeString(floorID);
        dest.writeString(meetingDate);
        dest.writeString(meetingDesc);
        dest.writeString(meetingID);
        dest.writeString(meetingName);
        dest.writeString(meetingStatus);
        dest.writeString(roomID);
        dest.writeString(startTime);
        dest.writeString(createdByName);
    }
}
