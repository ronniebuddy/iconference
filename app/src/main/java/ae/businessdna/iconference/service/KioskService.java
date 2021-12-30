package ae.businessdna.iconference.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ae.businessdna.iconference.R;
import ae.businessdna.iconference.activities.MainActivity;

/**
 * Project : iConference
 * Created by rohith on 7/31/2017.
 */

public class KioskService extends Service {
    private static final String TAG = "KioskService";

    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(1); // periodic interval to check in seconds -> 2 seconds
//    private static final long INTERVAL = 2000;

    private Context ctx = null;
    private boolean running = false;

    @Override
    public void onDestroy() {
        Log.i(TAG, "Stopping service 'KioskService'");
        running = true;
        Intent intent = new Intent("restartApps");
        sendBroadcast(intent);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Starting service 'KioskService'");
        running = true;
        ctx = this;

        // start a thread that periodically checks if your app is in the foreground
        Thread t = new Thread(() -> {
            do {
                handleKioskMode();
                try {
                    Thread.sleep(INTERVAL);

                } catch (InterruptedException e) {
                    Log.i(TAG, "Thread interrupted: 'KioskService'");
                }
            } while (running);
//                stopSelf();
        });

        t.start();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        onDestroy();
    }

    private void handleKioskMode() {
        // is Kiosk Mode active?
        if (isKioskModeActive(ctx)) {
            // is App in background?
            Log.d(TAG, "Kiosk mode active");
            if (isInBackground()) {
                restoreApp(); // restore!
            }
        } else {

        }
    }

    private boolean isKioskModeActive(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(getResources().getString(R.string.pref_key_kiosk_mode), false);
    }

    private boolean isInBackground() {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

//        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return (!ctx.getApplicationContext().getPackageName().equals(componentInfo.getPackageName()));
    }

    private void restoreApp() {
        // Restart activity
        Intent i = new Intent(ctx, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(i);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
