//package ae.businessdna.iconference.sync;
//
//import android.accounts.Account;
//import android.content.AbstractThreadedSyncAdapter;
//import android.content.ContentProviderClient;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.OperationApplicationException;
//import android.content.SyncResult;
//import android.os.Bundle;
//import android.os.RemoteException;
//import android.util.Log;
//
//import org.json.JSONException;
//
//import java.io.IOException;
//
///**
// * Project : iConference
// * Created by rohith on 7/20/2017.
// */
//
//public class SyncAdapter extends AbstractThreadedSyncAdapter {
//    private static final String TAG = "SYNC_ADAPTER";
//
//    /**
//     * This gives us access to our local data source.
//     */
//    private final ContentResolver resolver;
//
//
//    public SyncAdapter(Context c, boolean autoInit) {
//        this(c, autoInit, false);
//    }
//
//    public SyncAdapter(Context c, boolean autoInit, boolean parallelSync) {
//        super(c, autoInit, parallelSync);
//        this.resolver = c.getContentResolver();
//    }
//
//    /**
//     * This method is run by the Android framework, on a new Thread, to perform a sync.
//     * @param account Current account
//     * @param extras Bundle extras
//     * @param authority Content authority
//     * @param provider {@link ContentProviderClient}
//     * @param syncResult Object to write stats to
//     */
//    @Override
//    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
//        Log.w(TAG, "Starting synchronization...");
//
//        try {
//            // Synchronize our news feed
//            syncNewsFeed(syncResult);
//
//            // Add any other things you may want to sync
//
//        } catch (IOException ex) {
//            Log.e(TAG, "Error synchronizing!", ex);
//            syncResult.stats.numIoExceptions++;
//        } catch (JSONException ex) {
//            Log.e(TAG, "Error synchronizing!", ex);
//            syncResult.stats.numParseExceptions++;
//        } catch (RemoteException |OperationApplicationException ex) {
//            Log.e(TAG, "Error synchronizing!", ex);
//            syncResult.stats.numAuthExceptions++;
//        }
//
//        Log.w(TAG, "Finished synchronization!");
//    }
//}
