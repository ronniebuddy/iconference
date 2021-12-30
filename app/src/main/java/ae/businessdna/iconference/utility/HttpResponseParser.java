package ae.businessdna.iconference.utility;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ae.businessdna.iconference.bean.MeetingDay;
import ae.businessdna.iconference.bean.MeetingDetail;
import ae.businessdna.iconference.bean.MeetingHeader;
import ae.businessdna.iconference.bean.master.AttendeeMaster;
import ae.businessdna.iconference.bean.master.CompanyMaster;
import ae.businessdna.iconference.bean.master.FloorMaster;
import ae.businessdna.iconference.bean.master.RoomMaster;

/**
 * Project : iConference
 * Created by rohith on 7/17/2017.
 */

public class HttpResponseParser {
    private static final String TAG = "HttpResponseParser";
    // Attendee Master
    private final String BADGE_NO = "BadgeNo";
    private final String TITLE = "title";
    private final String NAME = "EmpName";

    // Company Master
    private final String COMPANY_CODE = "CompanyID";
    private final String COMPANY_NAME = "CompName";

    // Floor Master
    private final String FLOOR_ID = "FloorID";
    //    private final String COMP_CODE = "comp_id";
    private final String FLOOR_NAME = "FloorName";
    private final String FLOOR_NO = "floor_no";
    private final String IS_ACTIVE = "is_active";

    // Room Master
    private final String ROOM_ID = "RoomID";
    // private final String FLOOR_ID = "floor_id";
    private final String ROOM_NAME = "RoomName";
//    private final String ROOM_TYPE = "room_type";
//    private final String ROOM_CAPACITY = "room_capacity";
    // private final String IS_ACTIVE = "is_active";

    // Meeting Header
    private final String MEETING_ID = "MeetingID";
    // private final String COMPANY_CODE = "comp_code";
    // private final String FLOOR_ID = "floor_id";
    // private final String ROOM_ID = "room_id";
    private final String MEETING_NAME = "MeetingName";
    private final String MEETING_DATE = "MeetingDate";
    private final String MEETING_DESC = "MeetingDesc";
    private final String MEETING_STATUS = "MeetingStatus";
    // private final String IS_ACTIVE = "is_active";
    private final String START_TIME = "StartTime";
    private final String END_TIME = "EndTime";
//    private final String CREATED_BY = "created_by";
//    private final String CREATED_DATE = "created_date";

    // Meeting Detail
    //private final String MEETING_DETAIl_ID = "meeting_dt_id";
    // private final String MEETING_ID = "meeting_id";
//    private final String RECIPIENT_BADGE_NO = "BadgeNo";
    private final String IS_ORGANISER = "IsOrganizer";
    // private final String IS_ACTIVE = "is_active";
    private final String CREATED_BY = "CreatedBy";
    private final String CREATED_BY_NAME = "CreatedByName";
    // private final String CREATED_DATE = "created_date";

    public List<AttendeeMaster> parseJSONObjectAttendeeMaster(JSONArray response) {
        ArrayList<AttendeeMaster> attendeeMasterList = null;
        if (response != null && response.length() > 0) {
            try {
                attendeeMasterList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    AttendeeMaster attendeeMaster = parseSingleAttendee(response.getJSONObject(i));
                    if (attendeeMaster != null) {
                        attendeeMasterList.add(attendeeMaster);
                    } else {
                        Log.e(TAG, "onSuccess: parsing attendee master at " + i + " failed");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return attendeeMasterList;

    }

    private AttendeeMaster parseSingleAttendee(final JSONObject singleAttendee) {
        final AttendeeMaster attendee = new AttendeeMaster();

        try {
            if (singleAttendee.has(BADGE_NO) && !singleAttendee.isNull(BADGE_NO)) {
                attendee.badgeNo = singleAttendee.getString(BADGE_NO).trim();
            } else {
                return null;
            }
            if (singleAttendee.has(TITLE) && !singleAttendee.isNull(TITLE)) {
                attendee.title = singleAttendee.getString(TITLE).trim();
            } else {
                return null;
            }
            if (singleAttendee.has(NAME) && !singleAttendee.isNull(NAME)) {
                attendee.empName = singleAttendee.getString(NAME).trim();
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "parseSingleAttendee: Error parsing attendee entry");
        }

        return attendee;
    }

    public List<CompanyMaster> parseJSONObjectCompanyMaster(JSONArray response) {
        ArrayList<CompanyMaster> companyMasterList = null;
        if (response != null && response.length() > 0) {
            try {
                companyMasterList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    CompanyMaster companyMaster = parseSingleCompany(response.getJSONObject(i));
                    if (companyMaster != null) {
                        companyMasterList.add(companyMaster);
                    } else {
                        Log.e(TAG, "onSuccess: parsing company master at " + i + " failed");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return companyMasterList;

    }

    private CompanyMaster parseSingleCompany(final JSONObject singleCompany) {
        final CompanyMaster companyMaster = new CompanyMaster();

        try {
            if (singleCompany.has(COMPANY_CODE) && !singleCompany.isNull(COMPANY_CODE)) {
                companyMaster.companyCode = singleCompany.getString(COMPANY_CODE).trim();
            } else {
                return null;
            }
            if (singleCompany.has(COMPANY_NAME) && !singleCompany.isNull(COMPANY_NAME)) {
                companyMaster.companyName = singleCompany.getString(COMPANY_NAME).trim();
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "parseSingleCompany: Error parsing company entry");
        }

        return companyMaster;
    }

    public List<FloorMaster> parseJSONObjectFloorMaster(JSONArray response) {
        ArrayList<FloorMaster> floorMasterList = null;
        if (response != null && response.length() > 0) {
            try {
                floorMasterList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    FloorMaster floorMaster = parseSingleFloor(response.getJSONObject(i));
                    if (floorMaster != null) {
                        floorMasterList.add(floorMaster);
                    } else {
                        Log.e(TAG, "onSuccess: parsing floor master at " + i + " failed");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return floorMasterList;

    }

    private FloorMaster parseSingleFloor(final JSONObject singleFloor) {
        final FloorMaster floorMaster = new FloorMaster();

        try {
            if (singleFloor.has(FLOOR_ID) && !singleFloor.isNull(FLOOR_ID)) {
                floorMaster.floorId = singleFloor.getString(FLOOR_ID).trim();
            } else {
                return null;
            }
//            if (singleFloor.has(COMP_CODE) && !singleFloor.isNull(COMP_CODE)) {
//                floorMaster.companyId = singleFloor.getString(COMP_CODE).trim();
//            } else {
//                return null;
//            }
            if (singleFloor.has(FLOOR_NAME) && !singleFloor.isNull(FLOOR_NAME)) {
                floorMaster.floorName = singleFloor.getString(FLOOR_NAME).trim();
            } else {
                return null;
            }
            if (singleFloor.has(FLOOR_NO) && !singleFloor.isNull(FLOOR_NO)) {
                floorMaster.floorNumber = singleFloor.getString(FLOOR_NO).trim();
            } else {
                return null;
            }
            if (singleFloor.has(IS_ACTIVE) && !singleFloor.isNull(IS_ACTIVE)) {
                floorMaster.isActive = singleFloor.getString(IS_ACTIVE).trim();
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "parseSingleFloor: Error parsing floor entry");
        }

        return floorMaster;
    }

    public List<RoomMaster> parseJSONObjectRoomMaster(JSONArray response) {
        ArrayList<RoomMaster> roomMasterList = null;
        if (response != null && response.length() > 0) {
            try {
                roomMasterList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    RoomMaster roomMaster = parseSingleRoom(response.getJSONObject(i));
                    if (roomMaster != null) {
                        roomMasterList.add(roomMaster);
                    } else {
                        Log.e(TAG, "onSuccess: parsing room master at " + i + " failed");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return roomMasterList;

    }

    private RoomMaster parseSingleRoom(final JSONObject singleRoom) {
        final RoomMaster roomMaster = new RoomMaster();

        try {
            if (singleRoom.has(ROOM_ID) && !singleRoom.isNull(ROOM_ID)) {
                roomMaster.roomId = singleRoom.getString(ROOM_ID).trim();
            } else {
                return null;
            }
            if (singleRoom.has(FLOOR_ID) && !singleRoom.isNull(FLOOR_ID)) {
                roomMaster.floorId = singleRoom.getString(FLOOR_ID).trim();
            } else {
                return null;
            }
            if (singleRoom.has(ROOM_NAME) && !singleRoom.isNull(ROOM_NAME)) {
                roomMaster.roomName = singleRoom.getString(ROOM_NAME).trim();
            } else {
                return null;
            }
//            if (singleRoom.has(ROOM_TYPE) && !singleRoom.isNull(ROOM_TYPE)) {
//                roomMaster.roomType = singleRoom.getString(ROOM_TYPE).trim();
//            } else {
//                return null;
//            }
//            if (singleRoom.has(ROOM_CAPACITY) && !singleRoom.isNull(ROOM_CAPACITY)) {
//                roomMaster.roomCapacity = singleRoom.getString(ROOM_CAPACITY).trim();
//            } else {
//                return null;
//            }
            if (singleRoom.has(IS_ACTIVE) && !singleRoom.isNull(IS_ACTIVE)) {
                roomMaster.isActive = singleRoom.getString(IS_ACTIVE).trim();
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "parseSingleRoom: Error parsing room entry");
        }

        return roomMaster;
    }

    public List<MeetingHeader> parseJSONObjectMeetingHeader(JSONArray response) {
        ArrayList<MeetingHeader> meetingHeaderList = null;
        if (response != null && response.length() > 0) {
            try {
                meetingHeaderList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    MeetingHeader meetingHeader = parseSingleMeetingHeader(response.getJSONObject(i));
                    if (meetingHeader != null) {
                        meetingHeaderList.add(meetingHeader);
                    } else {
                        Log.e(TAG, "onSuccess: parsing meeting header at " + i + " failed");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return meetingHeaderList;

    }

    private MeetingHeader parseSingleMeetingHeader(final JSONObject singleMeetingHeader) {
        final MeetingHeader meetingHeader = new MeetingHeader();

        try {
            if (singleMeetingHeader.has(MEETING_ID) && !singleMeetingHeader.isNull(MEETING_ID)) {
                meetingHeader.meetingID = singleMeetingHeader.getString(MEETING_ID).trim();
            } else {
                return null;
            }
//            if (singleMeetingHeader.has(COMPANY_CODE) && !singleMeetingHeader.isNull(COMPANY_CODE)) {
//                meetingHeader.companyID = singleMeetingHeader.getString(COMPANY_CODE).trim();
//            } else {
//                return null;
//            }
//            if (singleMeetingHeader.has(FLOOR_ID) && !singleMeetingHeader.isNull(FLOOR_ID)) {
//                meetingHeader.floorID = singleMeetingHeader.getString(FLOOR_ID).trim();
//            } else {
//                return null;
//            }
//            if (singleMeetingHeader.has(ROOM_ID) && !singleMeetingHeader.isNull(ROOM_ID)) {
//                meetingHeader.roomID = singleMeetingHeader.getString(ROOM_ID).trim();
//            } else {
//                return null;
//            }
            if (singleMeetingHeader.has(MEETING_NAME) && !singleMeetingHeader.isNull(MEETING_NAME)) {
                meetingHeader.subject = singleMeetingHeader.getString(MEETING_NAME).trim();
            } else {
                return null;
            }
            if (singleMeetingHeader.has(MEETING_DATE) && !singleMeetingHeader.isNull(MEETING_DATE)) {
                meetingHeader.createdDate = singleMeetingHeader.getString(MEETING_DATE).trim();
            } else {
                return null;
            }
            if (singleMeetingHeader.has(START_TIME) && !singleMeetingHeader.isNull(START_TIME)) {
                meetingHeader.startTime = singleMeetingHeader.getString(START_TIME).trim();
            } else {
                return null;
            }
            if (singleMeetingHeader.has(END_TIME) && !singleMeetingHeader.isNull(END_TIME)) {
                meetingHeader.endTime = singleMeetingHeader.getString(END_TIME).trim();
            } else {
                return null;
            }
            if (singleMeetingHeader.has(MEETING_DESC) && !singleMeetingHeader.isNull(MEETING_DESC)) {
                meetingHeader.bodyPreview = singleMeetingHeader.getString(MEETING_DESC).trim();
            } else {
                return null;
            }
            if (singleMeetingHeader.has(MEETING_STATUS) && !singleMeetingHeader.isNull(MEETING_STATUS)) {
                meetingHeader.meetingStatus = singleMeetingHeader.getString(MEETING_STATUS).trim();
            } else {
                return null;
            }
//            if (singleMeetingHeader.has(CREATED_BY) && !singleMeetingHeader.isNull(CREATED_BY)) {
//                meetingHeader.createdBy = singleMeetingHeader.getString(CREATED_BY).trim();
//            } else {
//                return null;
//            }
            if (singleMeetingHeader.has(CREATED_BY_NAME) && !singleMeetingHeader.isNull(CREATED_BY_NAME)) {
                meetingHeader.createdByName = singleMeetingHeader.getString(CREATED_BY_NAME).trim();
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "parseSingleMeetingHeader: Error parsing header entry");
        }

        return meetingHeader;
    }

    public List<MeetingDetail> parseJSONObjectMeetingDetail(JSONArray response) {
        ArrayList<MeetingDetail> meetingDetailList = null;
        if (response != null && response.length() > 0) {
            try {
                meetingDetailList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    MeetingDetail meetingDetail = parseSingleMeetingDetail(response.getJSONObject(i));
                    if (meetingDetail != null) {
                        meetingDetailList.add(meetingDetail);
                    } else {
                        Log.e(TAG, "onSuccess: parsing meeting detail at " + i + " failed");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return meetingDetailList;

    }

    private MeetingDetail parseSingleMeetingDetail(final JSONObject singleMeetingDetail) {
        final MeetingDetail meetingDetail = new MeetingDetail();

        try {
//            if (singleMeetingDetail.has(MEETING_DETAIl_ID) && !singleMeetingDetail.isNull(MEETING_DETAIl_ID)) {
//                meetingDetail.meetingDetailId = singleMeetingDetail.getString(MEETING_DETAIl_ID).trim();
//            } else {
//                return null;
//            }
            if (singleMeetingDetail.has(BADGE_NO) && !singleMeetingDetail.isNull(BADGE_NO)) {
                meetingDetail.badgeNo = singleMeetingDetail.getString(BADGE_NO).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(COMPANY_CODE) && !singleMeetingDetail.isNull(COMPANY_CODE)) {
                meetingDetail.companyID = singleMeetingDetail.getString(COMPANY_CODE).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(COMPANY_NAME) && !singleMeetingDetail.isNull(COMPANY_NAME)) {
                meetingDetail.compName = singleMeetingDetail.getString(COMPANY_NAME).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(NAME) && !singleMeetingDetail.isNull(NAME)) {
                meetingDetail.empName = singleMeetingDetail.getString(NAME).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(START_TIME) && !singleMeetingDetail.isNull(START_TIME)) {
                meetingDetail.startTime = singleMeetingDetail.getString(START_TIME).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(END_TIME) && !singleMeetingDetail.isNull(END_TIME)) {
                meetingDetail.endTime = singleMeetingDetail.getString(END_TIME).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(FLOOR_ID) && !singleMeetingDetail.isNull(FLOOR_ID)) {
                meetingDetail.floorID = singleMeetingDetail.getString(FLOOR_ID).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(FLOOR_NAME) && !singleMeetingDetail.isNull(FLOOR_NAME)) {
                meetingDetail.floorName = singleMeetingDetail.getString(FLOOR_NAME).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(IS_ORGANISER) && !singleMeetingDetail.isNull(IS_ORGANISER)) {
                meetingDetail.isOrganizer = singleMeetingDetail.getString(IS_ORGANISER).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(MEETING_ID) && !singleMeetingDetail.isNull(MEETING_ID)) {
                meetingDetail.meetingID = singleMeetingDetail.getString(MEETING_ID).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(MEETING_DATE) && !singleMeetingDetail.isNull(MEETING_DATE)) {
                meetingDetail.meetingDate = singleMeetingDetail.getString(MEETING_DATE).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(MEETING_DESC) && !singleMeetingDetail.isNull(MEETING_DESC)) {
                meetingDetail.meetingDesc = singleMeetingDetail.getString(MEETING_DESC).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(MEETING_NAME) && !singleMeetingDetail.isNull(MEETING_NAME)) {
                meetingDetail.meetingName = singleMeetingDetail.getString(MEETING_NAME).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(MEETING_STATUS) && !singleMeetingDetail.isNull(MEETING_STATUS)) {
                meetingDetail.meetingStatus = singleMeetingDetail.getString(MEETING_STATUS).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(ROOM_ID) && !singleMeetingDetail.isNull(ROOM_ID)) {
                meetingDetail.roomID = singleMeetingDetail.getString(ROOM_ID).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(ROOM_NAME) && !singleMeetingDetail.isNull(ROOM_NAME)) {
                meetingDetail.roomName = singleMeetingDetail.getString(ROOM_NAME).trim();
            } else {
                return null;
            }
//            if (singleMeetingDetail.has(IS_ACTIVE) && !singleMeetingDetail.isNull(IS_ACTIVE)) {
//                meetingDetail.isActive = singleMeetingDetail.getString(IS_ACTIVE).trim();
//            } else {
//                return null;
//            }
//            if (singleMeetingDetail.has(CREATED_BY) && !singleMeetingDetail.isNull(CREATED_BY)) {
//                meetingDetail.createdBy = singleMeetingDetail.getString(CREATED_BY).trim();
//            } else {
//                return null;
//            }
//            if (singleMeetingDetail.has(CREATED_DATE) && !singleMeetingDetail.isNull(CREATED_DATE)) {
//                meetingDetail.createdDate = singleMeetingDetail.getString(CREATED_DATE).trim();
//            } else {
//                return null;
//            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "parseSingleMeetingDetail: Error parsing detail entry");
        }

        return meetingDetail;
    }

    public List<MeetingDay> parseJSONObjectMeetingDay(JSONArray response) {
        ArrayList<MeetingDay> meetingDetailList = null;
        if (response != null && response.length() > 0) {
            try {
                meetingDetailList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    MeetingDay meetingDetail = parseSingleMeetingDay(response.getJSONObject(i));
                    if (meetingDetail != null) {
                        meetingDetailList.add(meetingDetail);
                    } else {
                        Log.e(TAG, "onSuccess: parsing meeting day at " + i + " failed");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return meetingDetailList;

    }

    private MeetingDay parseSingleMeetingDay(final JSONObject singleMeetingDetail) {
        final MeetingDay meetingDetail = new MeetingDay();

        try {
//            if (singleMeetingDetail.has(MEETING_DETAIl_ID) && !singleMeetingDetail.isNull(MEETING_DETAIl_ID)) {
//                meetingDetail.meetingDetailId = singleMeetingDetail.getString(MEETING_DETAIl_ID).trim();
//            } else {
//                return null;
//            }
            if (singleMeetingDetail.has(COMPANY_CODE) && !singleMeetingDetail.isNull(COMPANY_CODE)) {
                meetingDetail.companyID = singleMeetingDetail.getString(COMPANY_CODE).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(END_TIME) && !singleMeetingDetail.isNull(END_TIME)) {
                meetingDetail.endTime = singleMeetingDetail.getString(END_TIME).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(FLOOR_ID) && !singleMeetingDetail.isNull(FLOOR_ID)) {
                meetingDetail.floorID = singleMeetingDetail.getString(FLOOR_ID).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(MEETING_DATE) && !singleMeetingDetail.isNull(MEETING_DATE)) {
                meetingDetail.meetingDate = singleMeetingDetail.getString(MEETING_DATE).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(MEETING_DESC) && !singleMeetingDetail.isNull(MEETING_DESC)) {
                meetingDetail.meetingDesc = singleMeetingDetail.getString(MEETING_DESC).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(MEETING_ID) && !singleMeetingDetail.isNull(MEETING_ID)) {
                meetingDetail.meetingID = singleMeetingDetail.getString(MEETING_ID).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(MEETING_NAME) && !singleMeetingDetail.isNull(MEETING_NAME)) {
                meetingDetail.meetingName = singleMeetingDetail.getString(MEETING_NAME).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(MEETING_STATUS) && !singleMeetingDetail.isNull(MEETING_STATUS)) {
                meetingDetail.meetingStatus = singleMeetingDetail.getString(MEETING_STATUS).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(ROOM_ID) && !singleMeetingDetail.isNull(ROOM_ID)) {
                meetingDetail.roomID = singleMeetingDetail.getString(ROOM_ID).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(START_TIME) && !singleMeetingDetail.isNull(START_TIME)) {
                meetingDetail.startTime = singleMeetingDetail.getString(START_TIME).trim();
            } else {
                return null;
            }
            if (singleMeetingDetail.has(CREATED_BY_NAME) && !singleMeetingDetail.isNull(CREATED_BY_NAME)) {
                meetingDetail.createdByName = singleMeetingDetail.getString(CREATED_BY_NAME).trim();
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "parseSingleMeetingDay: Error parsing meeting day entry");
        }

        return meetingDetail;
    }
}
