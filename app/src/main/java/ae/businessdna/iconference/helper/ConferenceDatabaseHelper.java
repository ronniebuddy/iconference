package ae.businessdna.iconference.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ae.businessdna.iconference.bean.Attendees;
import ae.businessdna.iconference.bean.MeetingDetail;
import ae.businessdna.iconference.bean.MeetingHeader;
import ae.businessdna.iconference.bean.master.AttendeeMaster;
import ae.businessdna.iconference.bean.master.CompanyMaster;
import ae.businessdna.iconference.bean.master.FloorMaster;
import ae.businessdna.iconference.bean.master.RoomMaster;
import nl.qbusict.cupboard.CupboardBuilder;
import nl.qbusict.cupboard.CupboardFactory;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Project : iConference
 * Created by rohith on 7/13/2017.
 */

public class ConferenceDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "conference.db";
    private static final int DATABASE_VERSION = 1;

//    private static final String TEXT_TYPE = " TEXT";
//    private static final String COMMA = ",";
//
//    // Create Table Query - Item
//    private static final String SQL_CREATE_MEETING_HEADER_ENTRIES =
//            "CREATE TABLE " + MeetingHeaderContract.MeetingHeaderEntry.TABLE_NAME + " (" +
//                    MeetingHeaderContract.MeetingHeaderEntry._ID + " INTEGER PRIMARY KEY," +
//                    MeetingHeaderContract.MeetingHeaderEntry.COLUMN_MEETING_ID + TEXT_TYPE + COMMA +
//                    MeetingHeaderContract.MeetingHeaderEntry.COLUMN_COMPANY_ID + TEXT_TYPE + COMMA +
//                    MeetingHeaderContract.MeetingHeaderEntry.COLUMN_FLOOR_ID + TEXT_TYPE + COMMA +
//                    MeetingHeaderContract.MeetingHeaderEntry.COLUMN_ROOM_ID + TEXT_TYPE + COMMA +
//                    MeetingHeaderContract.MeetingHeaderEntry.COLUMN_MEETING_DATE + TEXT_TYPE + COMMA +
//                    MeetingHeaderContract.MeetingHeaderEntry.COLUMN_MEETING_NAME + TEXT_TYPE + COMMA +
//                    MeetingHeaderContract.MeetingHeaderEntry.COLUMN_MEETING_DESC + TEXT_TYPE + COMMA +
//                    MeetingHeaderContract.MeetingHeaderEntry.COLUMN_MEETING_STATUS + TEXT_TYPE + COMMA +
//                    MeetingHeaderContract.MeetingHeaderEntry.COLUMN_START_TIME + TEXT_TYPE + COMMA +
//                    MeetingHeaderContract.MeetingHeaderEntry.COLUMN_END_TIME + TEXT_TYPE + COMMA +
//                    MeetingHeaderContract.MeetingHeaderEntry.COLUMN_CREATED_BY + TEXT_TYPE + COMMA +
//                    MeetingHeaderContract.MeetingHeaderEntry.COLUMN_CREATED_BY_NAME + TEXT_TYPE + ");";
//
//    // Drop Table Query - Item
//    private static final String SQL_DELETE_MEETING_HEADER_ENTRIES =
//            "DROP TABLE IF EXISTS " + MeetingHeaderContract.MeetingHeaderEntry.TABLE_NAME;
//
//    // Create Table Query - Item
//    private static final String SQL_CREATE_MEETING_DETAIL_ENTRIES =
//            "CREATE TABLE " + MeetingDetailContract.MeetingDetailEntry.TABLE_NAME + " (" +
//                    MeetingDetailContract.MeetingDetailEntry._ID + " INTEGER PRIMARY KEY," +
//                    MeetingDetailContract.MeetingDetailEntry.COLUMN_MEETING_ID + TEXT_TYPE + COMMA +
//                    MeetingDetailContract.MeetingDetailEntry.COLUMN_COMPANY_ID + TEXT_TYPE + COMMA +
//                    MeetingDetailContract.MeetingDetailEntry.COLUMN_COMPANY_NAME + TEXT_TYPE + COMMA +
//                    MeetingDetailContract.MeetingDetailEntry.COLUMN_FLOOR_ID + TEXT_TYPE + COMMA +
//                    MeetingDetailContract.MeetingDetailEntry.COLUMN_FLOOR_NAME + TEXT_TYPE + COMMA +
//                    MeetingDetailContract.MeetingDetailEntry.COLUMN_ROOM_ID + TEXT_TYPE + COMMA +
//                    MeetingDetailContract.MeetingDetailEntry.COLUMN_ROOM_NAME + TEXT_TYPE + COMMA +
//                    MeetingDetailContract.MeetingDetailEntry.COLUMN_MEETING_DATE + TEXT_TYPE + COMMA +
//                    MeetingDetailContract.MeetingDetailEntry.COLUMN_MEETING_NAME + TEXT_TYPE + COMMA +
//                    MeetingDetailContract.MeetingDetailEntry.COLUMN_MEETING_DESC + TEXT_TYPE + COMMA +
//                    MeetingDetailContract.MeetingDetailEntry.COLUMN_MEETING_STATUS + TEXT_TYPE + COMMA +
//                    MeetingDetailContract.MeetingDetailEntry.COLUMN_START_TIME + TEXT_TYPE + COMMA +
//                    MeetingDetailContract.MeetingDetailEntry.COLUMN_END_TIME + TEXT_TYPE + COMMA +
//                    MeetingDetailContract.MeetingDetailEntry.COLUMN_BADGE_NO + TEXT_TYPE + COMMA +
//                    MeetingDetailContract.MeetingDetailEntry.COLUMN_EMP_NAME + TEXT_TYPE + COMMA +
//                    MeetingDetailContract.MeetingDetailEntry.COLUMN_IS_ORGANISER + TEXT_TYPE + ");";
//
//    // Drop Table Query - Item
//    private static final String SQL_DELETE_MEETING_DETAIL_ENTRIES =
//            "DROP TABLE IF EXISTS " + MeetingDetailContract.MeetingDetailEntry.TABLE_NAME;

    public ConferenceDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static {
        // Register our models with Cupboard as usual
//        cupboard().register(AttendeeMaster.class);
//        cupboard().register(CompanyMaster.class);
//        cupboard().register(FloorMaster.class);
//        cupboard().register(RoomMaster.class);
        cupboard().register(MeetingHeader.class);
//        cupboard().register(MeetingDetail.class);
        cupboard().register(Attendees.class);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(SQL_CREATE_MEETING_HEADER_ENTRIES);
//        db.execSQL(SQL_CREATE_MEETING_DETAIL_ENTRIES);
        CupboardFactory.setCupboard(new CupboardBuilder(cupboard()).useAnnotations().build());
        cupboard().withDatabase(db).createTables();
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL(SQL_DELETE_MEETING_HEADER_ENTRIES);
//        sqLiteDatabase.execSQL(SQL_DELETE_MEETING_DETAIL_ENTRIES);
    }
}
