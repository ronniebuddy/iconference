package ae.businessdna.iconference.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Project : iConference
 * Created by rohith on 2/15/2018.
 */

public class MyAdminReceiver extends DeviceAdminReceiver {
    private static final String TAG = "MyAdminReceiver";

//    public static ComponentName getComponentName(Context context) {
//        return new ComponentName(context.getApplicationContext(), MyAdminReceiver.class);
//    }
//
//    @Override
//    public void onLockTaskModeEntering(Context context, Intent intent, String pkg) {
//        super.onLockTaskModeEntering(context, intent, pkg);
//        Log.i(TAG, "Lock task mode entered");
//        Log.i(TAG, "action  : " + intent.getAction());
//        Log.i(TAG, "package : " + intent.getStringExtra(DeviceAdminReceiver.EXTRA_LOCK_TASK_PACKAGE));
//    }
//
//    @Override
//    public void onLockTaskModeExiting(Context context, Intent intent) {
//        super.onLockTaskModeExiting(context, intent);
//        Log.i(TAG, "Lock task mode exited");
//        Log.i(TAG, "action  : " + intent.getAction());
//        Log.i(TAG, "package : " + intent.getStringExtra(DeviceAdminReceiver.EXTRA_LOCK_TASK_PACKAGE));
//    }
}
