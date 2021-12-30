package ae.businessdna.iconference.helper;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.crashlytics.android.Crashlytics;

import ae.businessdna.iconference.MyApplication;
import ae.businessdna.iconference.activities.MainActivity;

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Activity activity;

    public MyExceptionHandler(Activity a) {
        activity = a;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Crashlytics.logException(e);
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(MyApplication.getMyApplication().getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager mgr = (AlarmManager) MyApplication.getMyApplication().getBaseContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);

        activity.finish();
        System.exit(2);
    }
}
