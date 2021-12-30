package ae.businessdna.iconference.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ae.businessdna.iconference.MyApplication;
import ae.businessdna.iconference.bean.Attendees;
import ae.businessdna.iconference.bean.MeetingDetail;
import ae.businessdna.iconference.bean.MeetingHeader;
import ae.businessdna.iconference.bean.master.AttendeeMaster;
import ae.businessdna.iconference.bean.master.CompanyMaster;
import ae.businessdna.iconference.bean.master.FloorMaster;
import ae.businessdna.iconference.bean.master.RoomMaster;
import ae.businessdna.iconference.utility.DateUtil;
import nl.qbusict.cupboard.QueryResultIterable;

import static ae.businessdna.iconference.utility.DateUtil.validateCurrentMeetingTime;
import static ae.businessdna.iconference.utility.DateUtil.validateFinishedMeeting;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Project : iConference
 * Created by rohith on 7/13/2017.
 */

public class ConferenceHelper {
    private static final String TAG = "ConferenceHelper";

    private static SQLiteDatabase db;

    private static final String VALUE = "value";
    // Event ID
//    private static final String ID = "@odata.etag";
    private static final String ID = "id";
    // Created date time of event
    private static final String CREATED_DATE_TIME = "createdDateTime";
    // Last modified date time of event
    private static final String LAST_MODIFIED_DATE_TIME = "lastModifiedDateTime";
    // Reminder before start of event
    private static final String REMINDER_MIN_BEFORE_START = "reminderMinutesBeforeStart";
    // Event reminder ON
    private static final String IS_REMINDER_ON = "isReminderOn";
    // Event subject
    private static final String SUBJECT = "subject";
    // Body of event
    private static final String BODY_PREVIEW = "bodyPreview";

    // Is all day
    private static final String IS_ALL_DAY = "isAllDay";

    // Is cancelled
    private static final String IS_CANCELLED = "isCancelled";
    //
    private static final String IMPORTANCE = "importance";
    // Start date time of event
    private static final String START = "start";
    // End date time of event
    private static final String END = "end";
    // Location of event
    private static final String LOCATION = "location";
    // Attendees email and name
    private static final String ATTENDEES = "attendees";
    private static final String TYPE = "type";
    private static final String REQUIRED = "required";
    private static final String STATUS = "status";
    private static final String RESPONSE = "response";
    private static final String DECLINED = "declined";
    private static final String EMAIL_ADDRESS = "emailAddress";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    // Organizer name
    private static final String ORGANIZER = "organizer";

    // Email addresses to exclude
    private static final String ALERT = "alert@nccauh.ae";
    private static final String SCAN = "scan@nccauh.ae";

    private static final String CONF_ROOM_SEC = "confroom.secondfloor@nccauh.ae";
    private static final String CONF_ROOM_FIRST = "confroom.firstfloor@nccauh.ae";


    public ConferenceHelper(Context context) {
        ConferenceDatabaseHelper dbHelper = MyApplication.getInstance(context);
        db = dbHelper.getReadableDatabase();
    }

    //////////////////// Save Company Info ////////////////////

    /* Save company master */
    public boolean saveCompanyMaster(List<CompanyMaster> companyMasterList) {
        cupboard().withDatabase(db).delete(CompanyMaster.class, null);
        if (companyMasterList != null && companyMasterList.size() > 0) {
            cupboard().withDatabase(db).put(companyMasterList);
            Log.d(TAG, "Saved Company Master");
            return true;
        } else {
            return false;
        }
    }
    /* Save company master */


    //////////////////// Save Attendee Info ////////////////////

    /* Save attendee master */
    public boolean saveAttendeeMaster(List<AttendeeMaster> attendeeMasterList) {
        int result = cupboard().withDatabase(db).delete(AttendeeMaster.class, null);
        if (attendeeMasterList != null && attendeeMasterList.size() > 0) {
            cupboard().withDatabase(db).put(attendeeMasterList);
            Log.d(TAG, "Saved Attendee Master");
            return true;
        } else {
            return false;
        }
    }
    /* Save attendee master */


    //////////////////// Save Floor Info ////////////////////

    /* Save floor master */
    public boolean saveFloorMaster(List<FloorMaster> floorMasterList) {
        int result = cupboard().withDatabase(db).delete(FloorMaster.class, null);
        if (floorMasterList != null && floorMasterList.size() > 0) {
            cupboard().withDatabase(db).put(floorMasterList);
            Log.d(TAG, "Saved Floor Master");
            return true;
        } else {
            return false;
        }
    }
    /* Save floor master */


    //////////////////// Save Room Info ////////////////////

    /* Save room master */
//    public boolean saveRoomMaster(List<RoomMaster> roomMasterList) {
//        int result = cupboard().withDatabase(db).delete(RoomMaster.class, null);
//        if (roomMasterList != null && roomMasterList.size() > 0) {
//            cupboard().withDatabase(db).put(roomMasterList);
//            Log.d(TAG, "Saved Room Master");
//            return true;
//        } else {
//            return false;
//        }
//    }
    /* Save room master */


    //////////////////// Save Meeting Header ////////////////////

    /* Save meeting header */
    public boolean saveMeetingHeader(List<MeetingHeader> passedMeetingHeaderList) {

        if (passedMeetingHeaderList != null && passedMeetingHeaderList.size() > 0) {
            HashMap<String, MeetingHeader> passedMeetingHeaderMap = new HashMap<>();
            List<MeetingHeader> tempMeetingHeaderList = new ArrayList<>();
            tempMeetingHeaderList.addAll(passedMeetingHeaderList);

            for (MeetingHeader meetingHeader : passedMeetingHeaderList) {
                passedMeetingHeaderMap.put(meetingHeader.meetingID, meetingHeader);
            }


            List<MeetingHeader> existingMeetingHeaderList = getMeetingHeaders();

            if (existingMeetingHeaderList != null && existingMeetingHeaderList.size() > 0) {
                for (int i = 0; i < tempMeetingHeaderList.size(); i++) {
                    for (MeetingHeader meetingHeader : existingMeetingHeaderList) {
                        if (meetingHeader.meetingID.equals(tempMeetingHeaderList.get(i).meetingID)) {
//                            deleteMeetingHeader(meetingHeader.meetingID);
                            passedMeetingHeaderMap.remove(meetingHeader.meetingID);

                        }
                    }
//                    for (Map.Entry entry : passedMeetingHeaderMap.entrySet()) {
//                        MeetingHeader localHeader = (MeetingHeader) entry.getKey();
//
//                    }
                }

                if (passedMeetingHeaderMap.size() > 0) {
                    List<MeetingHeader> localHeaderList = new ArrayList<>();
                    for (Map.Entry<String, MeetingHeader> entry : passedMeetingHeaderMap.entrySet()) {
                        MeetingHeader localHeader = entry.getValue();
                        localHeaderList.add(localHeader);
                    }
                    cupboard().withDatabase(db).put(localHeaderList);
                }

                // Remove meetings that has passed the end time
                existingMeetingHeaderList = getMeetingHeaders();
                deleteMeetings(existingMeetingHeaderList);
            } else {
                cupboard().withDatabase(db).put(passedMeetingHeaderList);
                // Remove meetings that has passed the end time
                existingMeetingHeaderList = getMeetingHeaders();
                deleteMeetings(existingMeetingHeaderList);
            }
            return true;
        } else {
            return false;
        }


//        int result = cupboard().withDatabase(db).delete(MeetingHeader.class, null);
//        if (meetingHeaderList != null && meetingHeaderList.size() > 0) {
//            cupboard().withDatabase(db).put(meetingHeaderList);
//            Log.d(TAG, "Saved Meeting Headers");
//            return true;
//        } else {
//            return false;
//        }
    }

    public void deleteFinishedMeetings() {
        List<MeetingHeader> existingMeetingHeaderList = getMeetingHeaders();
        deleteMeetings(existingMeetingHeaderList);
    }

    private void deleteMeetings(List<MeetingHeader> existingMeetingHeaderList) {
        for (MeetingHeader meetingHeader : existingMeetingHeaderList) {
            try {
                if (DateUtil.validateFinishedMeeting(meetingHeader.endTime)) {
                    deleteMeetingHeader(meetingHeader.meetingID);
                    deleteMeetingAttendees(meetingHeader.meetingID);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    /* Save meeting header */

    public List<MeetingHeader> parseJSONObjectMeetingHeader(JSONObject response) {
        List<MeetingHeader> meetingHeaderList = null;
        if (response != null && response.length() > 0) {
            try {
                meetingHeaderList = new ArrayList<>();
                JSONArray innerArray = response.getJSONArray(VALUE);
                if (innerArray != null && innerArray.length() > 0) {
                    for (int i = 0; i < innerArray.length(); i++) {
                        MeetingHeader meetingHeader = parseSingleMeeting(innerArray.getJSONObject(i));
                        meetingHeaderList.add(meetingHeader);
                    }
//                    saveMeetingHeader(meetingHeaderList);
                } else {
                    Log.d(TAG, "Inner Array - null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return meetingHeaderList;

    }

    private MeetingHeader parseSingleMeeting(final JSONObject singleMeeting) {
        MeetingHeader meetingHeader = new MeetingHeader();
        String type, response;
        try {
            meetingHeader.meetingID = singleMeeting.getString(ID);
//            meetingHeader.createdDate = singleMeeting.getString(CREATED_DATE_TIME);
            meetingHeader.subject = singleMeeting.getString(SUBJECT);
            meetingHeader.bodyPreview = singleMeeting.getString(BODY_PREVIEW);
            meetingHeader.isAllDay = singleMeeting.getBoolean(IS_ALL_DAY);
            meetingHeader.isCancelled = singleMeeting.getBoolean(IS_CANCELLED);

            JSONObject startDateTime = singleMeeting.getJSONObject(START);
            meetingHeader.startTime = startDateTime.getString("dateTime");

            JSONObject endDateTime = singleMeeting.getJSONObject(END);
            meetingHeader.endTime = endDateTime.getString("dateTime");

            JSONObject location = singleMeeting.getJSONObject(LOCATION);
            meetingHeader.location = location.getString("displayName");

            JSONObject organizer = singleMeeting.getJSONObject(ORGANIZER);
            JSONObject email = organizer.getJSONObject(EMAIL_ADDRESS);
            meetingHeader.createdByName = email.getString(NAME);

            JSONArray attendeesJSON = singleMeeting.getJSONArray(ATTENDEES);
            if (attendeesJSON != null && attendeesJSON.length() > 0) {
                List<Attendees> attendeesList = new ArrayList<>();
                for (int i = 0; i < attendeesJSON.length(); i++) {
                    Attendees attendees = new Attendees();
                    JSONObject status = attendeesJSON.getJSONObject(i);
                    JSONObject responseJSON = status.getJSONObject(STATUS);
                    type = status.getString(TYPE);
                    response = responseJSON.getString(RESPONSE);
                    if(type.equals(REQUIRED) && !response.equals(DECLINED)) {
                        JSONObject attendeesEmail = status.getJSONObject(EMAIL_ADDRESS);
                        if(attendeesEmail.getString(ADDRESS).trim().equals(ALERT) ||
                                attendeesEmail.getString(ADDRESS).trim().equals(SCAN) ||
                                attendeesEmail.getString(ADDRESS).trim().equals(CONF_ROOM_FIRST) ||
                                attendeesEmail.getString(ADDRESS).trim().equals(CONF_ROOM_SEC)) {

                        } else {
                            attendees.meetingID = meetingHeader.meetingID;
                            attendees.name = attendeesEmail.getString(NAME).trim();
                            attendeesList.add(attendees);
                        }
                    }
                }
                saveAttendees(attendeesList);
            } else {
                Log.d(TAG, "Inner Array - null");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return meetingHeader;
    }

    //////////////////// Save Attendees ////////////////////

    /* Save meeting details */
    public boolean saveAttendees(List<Attendees> attendeesList) {
        if (attendeesList.size() > 0) {
            //Check if meeting ID already exists
            if (getAttendeesListById(attendeesList.get(0).meetingID).size() != 0) {
                deleteMeetingAttendees(attendeesList.get(0).meetingID);
                cupboard().withDatabase(db).put(attendeesList);
            } else {
                cupboard().withDatabase(db).put(attendeesList);
            }
            Log.d(TAG, "Saved Attendee Details");
            return true;
        } else {
            return false;
        }
    }
    /* Save meeting details */


    //////////////////// Save Meeting Detail ////////////////////

    /* Save meeting details */
    public boolean saveMeetingDetail(List<MeetingDetail> meetingDetailList) {
        if (meetingDetailList.size() > 0) {
            cupboard().withDatabase(db).put(meetingDetailList);
            Log.d(TAG, "Saved Meeting Details");
            return true;
        } else {
            return false;
        }
    }
    /* Save meeting details */


    //////////////////// Get Meeting Header Info ////////////////////

    /* Get meeting headers list */
    public List<MeetingHeader> getMeetingHeaders() {
        final QueryResultIterable<MeetingHeader> iter = cupboard().withDatabase(db).query(MeetingHeader.class).query();
        return getMeetingHeaderListFromQuery(iter);
    }

    private static List<MeetingHeader> getMeetingHeaderListFromQuery(QueryResultIterable<MeetingHeader> iter) {

        final List<MeetingHeader> meetingHeaderList = new ArrayList<>();
        for (MeetingHeader meetingHeader : iter) {
            try {
//                if(validateCurrentMeetingTime(meetingHeader.startTime)) {
                    if(!validateFinishedMeeting(meetingHeader.endTime)) {
                    meetingHeaderList.add(meetingHeader);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        iter.close();

        return meetingHeaderList;
    }
    /* Get meeting headers list */

    /* Get meeting headers list */
    public List<MeetingHeader> getAllMeetingHeaders() {
        final QueryResultIterable<MeetingHeader> iter = cupboard().withDatabase(db).query(MeetingHeader.class).query();
        return getAllMeetingHeaderListFromQuery(iter);
    }

    private static List<MeetingHeader> getAllMeetingHeaderListFromQuery(QueryResultIterable<MeetingHeader> iter) {

        final List<MeetingHeader> meetingHeaderList = new ArrayList<>();
        for (MeetingHeader meetingHeader : iter) {
            //                if(validateCurrentMeetingTime(meetingHeader.startTime)) {
//                if(!validateFinishedMeeting(meetingHeader.endTime)) {
            meetingHeaderList.add(meetingHeader);
//                }
        }
        iter.close();

        return meetingHeaderList;
    }
    /* Get meeting headers list */

    /* Get meeting header by id */
    public MeetingHeader getMeetingHeaderById(String meetingId) {
        return cupboard().withDatabase(db).query(MeetingHeader.class).withSelection("meetingID = ? ", meetingId).get();
    }
    /* Get created by name from meeting header */

    /* Get meeting headers list */
    public List<MeetingHeader> getCurrentMeetingHeader() {
        final QueryResultIterable<MeetingHeader> iter = cupboard().withDatabase(db).query(MeetingHeader.class).query();
        return getCurrentMeetingHeaderList(iter);
    }

    private List<MeetingHeader> getCurrentMeetingHeaderList(QueryResultIterable<MeetingHeader> iter) {

        final List<MeetingHeader> meetingHeaderList = new ArrayList<>();
        for (MeetingHeader meetingHeader : iter) {
//            try {
//                if(validateCurrentMeetingTime(meetingHeader.startTime) && !meetingHeader.isCancelled) {
            try {
                // TODO Update in next build
                if(!meetingHeader.isCancelled && !validateFinishedMeeting(meetingHeader.endTime)) {
                        meetingHeaderList.add(meetingHeader);
                    }
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
        }
        iter.close();

        if (meetingHeaderList.size() > 1) {
//            Collections.sort(meetingHeaderList, new MeetingTimeComparator());
            Collections.sort(meetingHeaderList, Collections.reverseOrder(new MeetingTimeComparator()));
        }

        return meetingHeaderList;
    }
    /* Get meeting headers list */

    private class MeetingTimeComparator implements Comparator<MeetingHeader> {
        @Override
        public int compare(MeetingHeader o1, MeetingHeader o2) {
            try {
                if (
//                        DateUtil.validateStartMeetingTime(o1.createdDate, o1.startTime) &&
//                        DateUtil.validateStartMeetingTime(o2.createdDate, o2.startTime) &&
                        DateUtil.compareMeetingStartTime(o1.endTime, o2.endTime)) {
                    return 1;
                } else {
                    return -1;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }


    /* Get created by name from meeting header */
    public String getCreatedByNameFromHeader(String meetingId) {
        MeetingHeader meetingHeader = cupboard().withDatabase(db).query(MeetingHeader.class).withSelection("meetingID = ? ", meetingId).get();
        return meetingHeader.createdByName;
    }
    /* Get created by name from meeting header */

    public List<MeetingHeader> parseMeetingHeaders(List<MeetingHeader> passedMeetingHeader) {
        List<MeetingHeader> tempHeaderList = new ArrayList<>();
        if(passedMeetingHeader != null && passedMeetingHeader.size() > 0) {
            HashMap<String, MeetingHeader> tempHeaderMap = new HashMap<>();

            for (MeetingHeader meetingHeader : passedMeetingHeader) {
                tempHeaderMap.put(meetingHeader.meetingID, meetingHeader);
            }

            List<MeetingHeader> existingMeetingHeaders = getMeetingHeaders();
            if (existingMeetingHeaders != null && existingMeetingHeaders.size() > 0) {
                for (MeetingHeader meetingHeader : existingMeetingHeaders) {
                    try {
                        if (DateUtil.validateFinishedMeeting(meetingHeader.endTime)) {
                            deleteMeetingHeader(meetingHeader.meetingID);
                            if (tempHeaderMap.size() > 0) {
                                tempHeaderMap.remove(meetingHeader.meetingID);
                            }
                        }

                        for (Map.Entry<String, MeetingHeader> entry : tempHeaderMap.entrySet()) {
                            String meetingId = entry.getKey();
                            if (meetingHeader.meetingID.equals(meetingId)) {
//                        tempHeaderMap.remove(meetingId);
                                deleteMeetingHeader(meetingHeader.meetingID);
                                break;
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            for (MeetingHeader meetingHeader : passedMeetingHeader) {
                try {
                    if (DateUtil.validateFinishedMeeting(meetingHeader.endTime)) {
                        deleteMeetingHeader(meetingHeader.meetingID);
                        if (tempHeaderMap.size() > 0) {
                            tempHeaderMap.remove(meetingHeader.meetingID);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (tempHeaderMap.size() > 0) {
                for (Map.Entry<String, MeetingHeader> entry : tempHeaderMap.entrySet()) {
                    tempHeaderList.add(entry.getValue());
                }
            }
        } else {
            deleteFinishedMeetings();
        }

        return tempHeaderList;
    }

    public void deleteAttendees() {
        List<MeetingHeader> existingMeetingHeaders = getMeetingHeaders();
        if (existingMeetingHeaders != null && existingMeetingHeaders.size() > 0) {
            for (MeetingHeader meetingHeader : existingMeetingHeaders) {
                try {
                    if (DateUtil.validateFinishedMeeting(meetingHeader.endTime)) {
                        deleteMeetingAttendees(meetingHeader.meetingID);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //////////////////// Get Meeting Detail Info ////////////////////

    /* Get meeting details list */
    public List<MeetingDetail> getMeetingDetailListById(String meetingId) {
        final QueryResultIterable<MeetingDetail> iter = cupboard().withDatabase(db).query(MeetingDetail.class).withSelection("meetingID = ? ", meetingId).query();
        return getMeetingDetailListFromQuery(iter);
    }

    private static List<MeetingDetail> getMeetingDetailListFromQuery(QueryResultIterable<MeetingDetail> iter) {

        final List<MeetingDetail> meetingDetailList = new ArrayList<>();
        for (MeetingDetail meetingDetail : iter) {
            meetingDetailList.add(meetingDetail);
        }
        iter.close();

        return meetingDetailList;
    }
    /* Get meeting details list */

//    /* Get meeting details list */
//    public ArrayList<String> getAttendeesListById(String meetingId) {
//        final QueryResultIterable<MeetingDetail> iter = cupboard().withDatabase(db).query(MeetingDetail.class).withSelection("meetingID = ? ", meetingId).query();
//        return getAttendeesListFromQuery(iter);
//    }
//
//    private static ArrayList<String> getAttendeesListFromQuery(QueryResultIterable<MeetingDetail> iter) {
//
//        final ArrayList<String> meetingDetailList = new ArrayList<>();
//        for (MeetingDetail meetingDetail : iter) {
//            meetingDetailList.add(meetingDetail.empName);
//        }
//        iter.close();
//
//        return meetingDetailList;
//    }
//    /* Get meeting details list */

    /* Get meeting details list */
    public ArrayList<String> getAttendeesListById(String meetingId) {
        final QueryResultIterable<Attendees> iter = cupboard().withDatabase(db).query(Attendees.class).withSelection("meetingID = ? ", meetingId).query();
        return getAttendeesListFromQuery(iter);
    }

    private static ArrayList<String> getAttendeesListFromQuery(QueryResultIterable<Attendees> iter) {

        final ArrayList<String> attendeesList = new ArrayList<>();
        for (Attendees attendees : iter) {
            attendeesList.add(attendees.name);
        }
        iter.close();

        return attendeesList;
    }
    /* Get meeting details list */

    /* Check meeting detail exists */
//    public boolean meetingDetailExists(String meetingId) {
//        MeetingDetail meetingDetail = cupboard().withDatabase(db).query(MeetingDetail.class).withSelection("meetingID = ? ", meetingId).get();
//        return meetingDetail != null && !TextUtils.isEmpty(meetingDetail.meetingID);
//    }
    /* Check meeting detail exists */


    //////////////////// Get Company Info ////////////////////

    /* Get company name by code*/
//    public String getCompanyName(String companyCode) {
//        CompanyMaster companyMaster = cupboard().withDatabase(db).query(CompanyMaster.class).withSelection("companyCode = ? ", companyCode).get();
//        return companyMaster.companyName;
//    }
    /* Get company name by code*/


    //////////////////// Get Attendee Info ////////////////////

    /* Get attendee title by badge no */
//    public String getAttendeeTitle(String badgeNo) {
//        AttendeeMaster attendeeMaster = cupboard().withDatabase(db).query(AttendeeMaster.class).withSelection("badgeNo = ? ", badgeNo).get();
//        return attendeeMaster.title;
//    }
    /* Get attendee title by badge no */

    /* Get attendee name by badge no */
//    public String getAttendeeName(String badgeNo) {
//        AttendeeMaster attendeeMaster = cupboard().withDatabase(db).query(AttendeeMaster.class).withSelection("badgeNo = ? ", badgeNo).get();
//        return attendeeMaster.empName;
//    }
    /* Get attendee name by badge no */


    //////////////////// Get Floor Info ////////////////////

    /* Get floor number by id */
//    public String getFloorNumberById(String floorId) {
//        FloorMaster floorMaster = cupboard().withDatabase(db).query(FloorMaster.class).withSelection("floorId = ? ", floorId).get();
//        return floorMaster.floorNumber;
//    }
    /* Get floor number by id */

    /* Get floor number by company id */
//    public String getFloorNumberByCompanyId(String companyId) {
//        FloorMaster floorMaster = cupboard().withDatabase(db).query(FloorMaster.class).withSelection("companyId = ?", companyId).get();
//        return floorMaster.floorNumber;
//    }
    /* Get floor number by company id */

    /* Get floor name by id */
//    public String getFloorNameById(String floorId) {
//        FloorMaster floorMaster = cupboard().withDatabase(db).query(FloorMaster.class).withSelection("floorId = ? ", floorId).get();
//        return floorMaster.floorName;
//    }
    /* Get floor name by id */

    /* Get floor name by company id */
//    public String getFloorNameByCompanyId(String companyId) {
//        FloorMaster floorMaster = cupboard().withDatabase(db).query(FloorMaster.class).withSelection("companyId = ?", companyId).get();
//        return floorMaster.floorName;
//    }
    /* Get floor name by company id */

    /* Get floor active company id */
//    public String getFloorActive(String companyId) {
//        FloorMaster floorMaster = cupboard().withDatabase(db).query(FloorMaster.class).withSelection("companyId = ?", companyId).get();
//        return floorMaster.isActive;
//    }
    /* Get floor active by company id */


    //////////////////// Get Room Info ////////////////////

    /* Get room name by id */
//    public String getRoomNameById(String roomId) {
//        RoomMaster roomMaster = cupboard().withDatabase(db).query(RoomMaster.class).withSelection("roomId = ? ", roomId).get();
//        return roomMaster.roomName;
//    }
    /* Get room name by id */

    /* Get room name by floor id */
//    public String getRoomNameByFloorId(String floorId) {
//        RoomMaster roomMaster = cupboard().withDatabase(db).query(RoomMaster.class).withSelection("floorId = ?", floorId).get();
//        return roomMaster.roomName;
//    }
    /* Get room name by floor id */

    /* Get room type by floor id */
//    public String getRoomTypeByFloorId(String floorId) {
//        RoomMaster roomMaster = cupboard().withDatabase(db).query(RoomMaster.class).withSelection("floorId = ?", floorId).get();
//        return roomMaster.roomType;
//    }
    /* Get room type by floor id */

    /* Get room capacity by floor id */
//    public String getRoomCapacityByFloorId(String floorId) {
//        RoomMaster roomMaster = cupboard().withDatabase(db).query(RoomMaster.class).withSelection("floorId = ?", floorId).get();
//        return roomMaster.roomCapacity;
//    }
    /* Get room capacity by floor id */

    /* Get room active floor id */
//    public String getRoomActive(String floorId) {
//        RoomMaster roomMaster = cupboard().withDatabase(db).query(RoomMaster.class).withSelection("floorId = ?", floorId).get();
//        return roomMaster.isActive;
//    }
    /* Get floor active by floor id */


    //////////////////// Get Meeting Header Info ////////////////////


    //////////////////// Delete Meeting Header and Detail Info ////////////////////

    /* Delete all meeting header and detail */
    public static boolean deleteAllMeetingHeader() {
        boolean deleteHeader = cupboard().withDatabase(db).delete(MeetingHeader.class);

        return deleteHeader;
    }
    /* Delete all meeting header and detail */

    /* Delete meeting header and detail by id */
    public static boolean deleteMeetingHeader(String meetingId) {
        int deleteHeader = cupboard().withDatabase(db).delete(MeetingHeader.class, "meetingID = ?", meetingId);

        return deleteHeader > 0;
    }
    /* Delete meeting header and detail by id */

    /* Delete all meeting attendees */
    public static boolean deleteAllMeetingAttendees() {

        boolean deleteAttendees = cupboard().withDatabase(db).delete(Attendees.class);

        return deleteAttendees;
    }
    /* Delete all meeting attendees */

    /* Delete meeting header and detail by id */
    public static boolean deleteMeetingAttendees(String meetingId) {

        int deleteAttendess = cupboard().withDatabase(db).delete(Attendees.class, "meetingID = ?", meetingId);

        return deleteAttendess > 0;
    }
    /* Delete meeting header and detail by id */

    //////////////////// Delete Meeting Header and Detail Info ////////////////////


}
