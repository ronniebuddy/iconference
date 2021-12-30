package ae.businessdna.iconference.datacontract;

import android.provider.BaseColumns;

/**
 * Project : iConference
 * Created by rohith on 7/20/2017.
 */

public class MeetingDetailContract {
    private MeetingDetailContract() {
    }

    public static class MeetingDetailEntry implements BaseColumns {

        public static final String TABLE_NAME = "MeetingHeader";
        public static final String COLUMN_MEETING_ID = "MeetingID";
        public static final String COLUMN_COMPANY_ID = "CompanyID";
        public static final String COLUMN_COMPANY_NAME = "CompanyName";
        public static final String COLUMN_FLOOR_ID = "FloorID";
        public static final String COLUMN_FLOOR_NAME = "FloorName";
        public static final String COLUMN_ROOM_ID = "RoomID";
        public static final String COLUMN_ROOM_NAME = "RoomName";
        public static final String COLUMN_MEETING_DATE = "MeetingDate";
        public static final String COLUMN_MEETING_NAME = "MeetingName";
        public static final String COLUMN_MEETING_DESC = "MeetingDesc";
        public static final String COLUMN_MEETING_STATUS = "MeetingStatus";
        public static final String COLUMN_START_TIME = "StartTime";
        public static final String COLUMN_END_TIME = "EndTime";
        public static final String COLUMN_BADGE_NO = "BadgeNo";
        public static final String COLUMN_EMP_NAME = "EmpName";
        public static final String COLUMN_IS_ORGANISER = "IsOrganizer";

    }
}
