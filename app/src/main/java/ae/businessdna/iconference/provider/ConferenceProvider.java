//package ae.businessdna.iconference.provider;
//
//import android.content.ContentProvider;
//import android.content.ContentValues;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteQueryBuilder;
//import android.net.Uri;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//
//import ae.businessdna.iconference.datacontract.MeetingDetailContract;
//import ae.businessdna.iconference.datacontract.MeetingHeaderContract;
//import ae.businessdna.iconference.helper.ConferenceDatabaseHelper;
//
///**
// * Project : iConference
// * Created by rohith on 7/20/2017.
// */
//
//public class ConferenceProvider extends ContentProvider {
//
//    // database
//    private ConferenceDatabaseHelper database;
//
//    // Used for the Uri Matcher
//    public static final int M_MEETING_HEADER = 10;
//    public static final int M_MEETING_DETAIL = 20;
//
//    public static final String AUTHORITY = "ae.businessdna.iconference.contentprovider";
//    public static final String BASE_MEETING_HEADER = "MEETING_HEADER";
//    public static final String BASE_MEETING_DETAIL = "MEETING_DETAIL";
//
//    public static final Uri CONTENT_URI_MEETING_HEADER = Uri.parse("content://"
//            + AUTHORITY + "/" + BASE_MEETING_HEADER);
//    public static final Uri CONTENT_URI_MEETING_DETAIL = Uri.parse("content://"
//            + AUTHORITY + "/" + BASE_MEETING_DETAIL);
//
//    private static final UriMatcher sURIMatcher = new UriMatcher(
//            UriMatcher.NO_MATCH);
//
//    static {
//        sURIMatcher.addURI(AUTHORITY, BASE_MEETING_HEADER, M_MEETING_HEADER);
//        sURIMatcher.addURI(AUTHORITY, BASE_MEETING_DETAIL, M_MEETING_DETAIL);
//    }
//
//    @Override
//    public boolean onCreate() {
//        database = new ConferenceDatabaseHelper(getContext());
//        return false;
//    }
//
//    @Nullable
//    @Override
//    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
//        Cursor cursor;
//
//        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
//        SQLiteDatabase db = database.getWritableDatabase();
//
//        int uriType = sURIMatcher.match(uri);
//        switch (uriType) {
//            case M_MEETING_HEADER:
//                queryBuilder.setTables(MeetingHeaderContract.MeetingHeaderEntry.TABLE_NAME);
//                cursor = queryBuilder.query(db, projection, selection,
//                        selectionArgs, null, null, sortOrder);
//                break;
//            case M_MEETING_DETAIL:
//                queryBuilder.setTables(MeetingDetailContract.MeetingDetailEntry.TABLE_NAME);
//                cursor = queryBuilder.query(db, projection, selection,
//                        selectionArgs, null, null, sortOrder);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//
//        cursor.setNotificationUri(getContext().getContentResolver(), uri);
//        return cursor;
//    }
//
//    @Nullable
//    @Override
//    public String getType(@NonNull Uri uri) {
//        return null;
//    }
//
//    @Nullable
//    @Override
//    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
//        int uriType = sURIMatcher.match(uri);
//        SQLiteDatabase sqlDB = database.getWritableDatabase();
//
//        long id = 0;
//        switch (uriType) {
//            case M_MEETING_HEADER:
//                id = sqlDB.insert(MeetingHeaderContract.MeetingHeaderEntry.TABLE_NAME, null,
//                        values);
//                break;
//            case M_MEETING_DETAIL:
//                id = sqlDB.insert(MeetingDetailContract.MeetingDetailEntry.TABLE_NAME, null,
//                        values);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
//        return Uri.parse(String.valueOf(id));
//    }
//
//    @Override
//    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
//        int uriType = sURIMatcher.match(uri);
//        SQLiteDatabase sqlDB = database.getWritableDatabase();
//        int rowsDeleted;
//        switch (uriType) {
//            case M_MEETING_HEADER:
//                rowsDeleted = sqlDB.delete(MeetingHeaderContract.MeetingHeaderEntry.TABLE_NAME, selection,
//                        selectionArgs);
//                break;
//            case M_MEETING_DETAIL:
//                rowsDeleted = sqlDB.delete(MeetingDetailContract.MeetingDetailEntry.TABLE_NAME, selection,
//                        selectionArgs);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
//        return rowsDeleted;
//    }
//
//    @Override
//    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
//        int uriType = sURIMatcher.match(uri);
//        SQLiteDatabase sqlDB = database.getWritableDatabase();
//        int rowsUpdated;
//        switch (uriType) {
//            case M_MEETING_HEADER:
//                rowsUpdated = sqlDB.update(MeetingHeaderContract.MeetingHeaderEntry.TABLE_NAME, values, selection,
//                        selectionArgs);
//                break;
//            case M_MEETING_DETAIL:
//                rowsUpdated = sqlDB.update(MeetingDetailContract.MeetingDetailEntry.TABLE_NAME, values, selection,
//                        selectionArgs);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//
//        getContext().getContentResolver().notifyChange(uri, null);
//        return rowsUpdated;
//    }
//}
