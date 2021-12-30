package ae.businessdna.iconference.utility;

import android.support.annotation.NonNull;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Project : iConference
 * Created by rohith on 7/13/2017.
 */

public class DateUtil {
    private static final String TAG = "DateUtil";

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getCurrentDateTwo() throws ParseException {
        DateFormat dfSource = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = dfSource.parse(getCurrentDate());

        DateFormat dfDest = new SimpleDateFormat("dd MMM, yyyy", Locale.US);
        return dfDest.format(date);
    }

    public static String getCurrentDateTwo(String passedDate) throws ParseException {
        DateFormat dfSource = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = dfSource.parse(getEndDateTimeTwo(passedDate));

        DateFormat dfDest = new SimpleDateFormat("dd MMM, yyyy", Locale.US);
        return dfDest.format(date);
    }

    public static String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getCurrentDateTimeTwo() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getNextDate() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = null;
        try {
            date = simpleDateFormat.parse(getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(Constants.FETCH_FREQ));
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getEndDateTime(String endDateTime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date endDate = format.parse(endDateTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return simpleDateFormat.format(endDate);
    }

    public static String getEndDateTimeTwo(String endDateTime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date endDate = format.parse(endDateTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return simpleDateFormat.format(endDate);
    }

    public static String getCurrentDateTimeTwo(String endDateTime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date endDate = format.parse(endDateTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        return simpleDateFormat.format(endDate);
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String parseMeetingTime(String retrievedDate) throws ParseException {
//        DateFormat dfSource = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
//        Date date = dfSource.parse(retrievedDate);
//
//        DateFormat dfDest = new SimpleDateFormat("HH:mm a", Locale.US);
//        return dfDest.format(date);

        SimpleDateFormat currentDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date endDate = currentDateTimeFormat.parse(getEndDateTime(retrievedDate));

        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm a", Locale.US); //this format changeable
        dateFormatter.setTimeZone(TimeZone.getDefault());

        return dateFormatter.format(endDate);
    }

    public static Date parseMeetingTimeToDate(String retrievedTime) throws ParseException {
        DateFormat dfSource = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date;
        date = dfSource.parse(getCurrentDate() + " " + retrievedTime);
        return date;
    }

    public static String parseMeetingDate(String retrievedDate) throws ParseException {
        int[] split = getSplitDate(retrievedDate);
        DateFormat dfSource = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String appendedDate = getAppendedDate(split[0], split[1], split[2]);
        Date date = dfSource.parse(appendedDate);

//        DateFormat dfDest = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//        return dfDest.format(date);
        return appendedDate;
    }

    public static String getTimeDifference(String endTime) throws ParseException {
        SimpleDateFormat currentDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date endDate = currentDateTimeFormat.parse(getEndDateTime(endTime));

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US); //this format changeable
        dateFormatter.setTimeZone(TimeZone.getDefault());

        String passedDate = dateFormatter.format(endDate);
        endDate = dateFormatter.parse(passedDate);

        Date currentDateTime = currentDateTimeFormat.parse(getCurrentDateTime());
        long difference = endDate.getTime() - currentDateTime.getTime();
        return String.valueOf(TimeUnit.MILLISECONDS.toMinutes(difference));
    }

        public static boolean validateCurrentMeetingTime(String startTime) throws ParseException {
            SimpleDateFormat currentDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date startDate = currentDateTimeFormat.parse(getEndDateTimeTwo(startTime));

            Date currentDateTime = currentDateTimeFormat.parse(getCurrentDate());
            return startDate.compareTo(currentDateTime) == 0;
    }

//    public static boolean validateStartMeetingTime(String startTime) throws ParseException {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
//        Date currentDate = format.parse(getCurrentDateTime());
//        Date startDate = format.parse(parseMeetingDate(meetingDate) + " " + startTime);
//        return startDate.before(currentDate);
//    }

    public static boolean validateFinishedMeeting(String endDateTime) throws ParseException {
//        DateFormat dfSource = new SimpleDateFormat("HH:mm:ss", Locale.US);
//        Date date = dfSource.parse(retrievedDate);
//
//        DateFormat dfDest = new SimpleDateFormat("HH:mm a", Locale.US);
//        return dfDest.format(date);

        SimpleDateFormat currentDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date endDate = currentDateTimeFormat.parse(getEndDateTime(endDateTime));

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US); //this format changeable
        dateFormatter.setTimeZone(TimeZone.getDefault());

        String passedDate = dateFormatter.format(endDate);
        endDate = dateFormatter.parse(passedDate);

        Date currentDateTime = currentDateTimeFormat.parse(getCurrentDateTime());
        Log.d(TAG, "End date time before current date time : " + endDate.before(currentDateTime));
        return endDate.before(currentDateTime);
    }

    public static boolean compareMeetingStartTime(String meetingOneEndTime, String meetingTwoEndTime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date dateOne = format.parse(getEndDateTime(meetingOneEndTime));
        Date dateTwo = format.parse(getEndDateTime(meetingTwoEndTime));

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US); //this format changeable
        dateFormatter.setTimeZone(TimeZone.getDefault());

        String passedDateOne = dateFormatter.format(dateOne);
        String passedDateTwo = dateFormatter.format(dateTwo);

        dateOne = dateFormatter.parse(passedDateOne);
        dateTwo = dateFormatter.parse(passedDateTwo);
        return dateOne.before(dateTwo);
    }

    private static int[] getSplitDate(String requiredDate) {
        String[] params = requiredDate.split(" ");
        String[] params2 = params[0].split("/");
//        String[] params3 = params[1].split(":");

        int year = Integer.parseInt(params2[2]);
        int month = Integer.parseInt(params2[0]);
        int day = Integer.parseInt(params2[1]);

//        int hour = Integer.parseInt(params3[0]);
//        int min = Integer.parseInt(params3[1]);
//        int sec = (int) Float.parseFloat(params3[2]);

        return new int[]{year, month, day};

    }

    @NonNull
    private static String getAppendedDate(int passedYear, int passedMonth, int passedDay) {
        String appendedDateMonth, appendedDateDay, date;
        if (passedMonth < 10) {
            appendedDateMonth = "0" + String.valueOf(passedMonth);
        } else {
            appendedDateMonth = String.valueOf(passedMonth);
        }

        if (passedDay < 10) {
            appendedDateDay = "0" + String.valueOf(passedDay);
        } else {
            appendedDateDay = String.valueOf(passedDay);
        }

        date = passedYear
                + "-" + appendedDateMonth
                + "-" + appendedDateDay;
        return date;
    }

    public static boolean validateDateTwo(int yearOne, int monthOne, int dayOne,
                                          DateTime passedDateTime) {

        LocalDate startDate = new DateTime(yearOne, monthOne, dayOne, 0, 0, 0, 0).toLocalDate();
        LocalDate passedDate = passedDateTime.toLocalDate();
//        DateTime startDate = new DateTime(yearOne, monthOne, dayOne, 0, 0, 0, 0);

        return (!startDate.isBefore(passedDate) && !startDate.isAfter(passedDate));
//        return (!startDate.withDate(yearOne, monthOne, dayOne).isBefore(passedDateTime) && !startDate.withDate(yearOne, monthOne, dayOne).isAfter(passedDateTime));
//        return startDate.withDate(yearOne, monthOne, dayOne).isEqual(passedDateTime);
    }

    public static String[] getSplitLocation(String passedLocation) {
        if (passedLocation.contains("-")) {
            return passedLocation.split("-");
        } else if(passedLocation.contains(",")){
            return passedLocation.split(",");
        }
        return null;
    }

    public static String getSplitDateString() {
        String appendedDate;
        String[] params = getCurrentDateTime().split("T");
        String[] params2 = params[0].split("-");
        String[] params3 = params[1].split(":");

        int year = Integer.parseInt(params2[0]);
        int month = Integer.parseInt(params2[1]);
        int day = Integer.parseInt(params2[2]);

        int hour = Integer.parseInt(params3[0]);
        int min = Integer.parseInt(params3[1]);
        int sec = (int) Float.parseFloat(params3[2]);

        appendedDate = getValueAsString(year) + "_" +
                getValueAsString(month) + "_" +
                getValueAsString(day) + "_" +
                getValueAsString(hour) + "_" +
                getValueAsString(min);

        return appendedDate;

    }

    private static String getValueAsString(int value) {
        return String.valueOf(value);
    }
}
