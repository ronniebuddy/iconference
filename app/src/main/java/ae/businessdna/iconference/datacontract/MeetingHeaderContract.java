package ae.businessdna.iconference.datacontract;

import android.provider.BaseColumns;

/**
 * Project : iConference
 * Created by rohith on 7/20/2017.
 */

public class MeetingHeaderContract {
    private MeetingHeaderContract() {
    }

    public static class MeetingHeaderEntry implements BaseColumns {

        public static final String TABLE_NAME = "MeetingHeader";
        public static final String COLUMN_MEETING_ID = "MeetingID";
        public static final String COLUMN_COMPANY_ID = "CompanyID";
        public static final String COLUMN_FLOOR_ID = "FloorID";
        public static final String COLUMN_ROOM_ID = "RoomID";
        public static final String COLUMN_MEETING_DATE = "MeetingDate";
        public static final String COLUMN_MEETING_NAME = "MeetingName";
        public static final String COLUMN_MEETING_DESC = "MeetingDesc";
        public static final String COLUMN_MEETING_STATUS = "MeetingStatus";
        public static final String COLUMN_START_TIME = "StartTime";
        public static final String COLUMN_END_TIME = "EndTime";
        public static final String COLUMN_CREATED_BY = "CreatedBy";
        public static final String COLUMN_CREATED_BY_NAME = "CreatedByName";

    }
}
