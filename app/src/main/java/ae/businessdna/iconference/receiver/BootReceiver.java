package ae.businessdna.iconference.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ae.businessdna.iconference.activities.MainActivity;

/**
 * Project : iConference
 * Created by rohith on 1/3/2018.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent myIntent = new Intent(context, MainActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }
    }
}
