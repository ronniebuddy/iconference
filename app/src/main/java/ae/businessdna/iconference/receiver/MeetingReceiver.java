package ae.businessdna.iconference.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Project : iConference
 * Created by rohith on 7/20/2017.
 */

public class MeetingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final Intent notificationIntent = new Intent("ae.businessdna.iconference.notification");
        LocalBroadcastManager.getInstance(context).sendBroadcast(notificationIntent);
    }
}
