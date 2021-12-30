package ae.businessdna.iconference;

import android.app.Application;
import android.content.Context;

import ae.businessdna.iconference.helper.ConferenceDatabaseHelper;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Project : iConference
 * Created by rohith on 7/17/2017.
 */

/**
 * ACRA annotation providing necessary parameters for the logs created in the application
 */
//@ReportsCrashes(
//        formUri = "http://192.168.4.102:8732/MyRESTService.svc/SubmitACRA", // Test ( Web service not updated )
//        reportType = HttpSender.Type.PLAINTEXT,
//        httpMethod = HttpSender.Method.POST,
//        logcatArguments = {"-t", "100", "-v", "long", "ActivityManager:I", "iConference:D", "*:S"},
//        customReportContent = {
//                ReportField.APP_VERSION_CODE,
//                ReportField.APP_VERSION_NAME,
//                ReportField.ANDROID_VERSION,
//                ReportField.PACKAGE_NAME,
//                ReportField.DEVICE_ID,
//                ReportField.REPORT_ID,
//                ReportField.BUILD,
//                ReportField.CUSTOM_DATA,
//                ReportField.STACK_TRACE,
//                ReportField.LOGCAT
//        },
//        mode = ReportingInteractionMode.TOAST,
//        resToastText = R.string.acra_toast
//)


public class MyApplication extends Application {

    private static MyApplication myApplication;

    private static ConferenceDatabaseHelper sDatabaseInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        myApplication = this;
    }

    public static synchronized ConferenceDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sDatabaseInstance == null) {
            sDatabaseInstance = new ConferenceDatabaseHelper(context.getApplicationContext());
        }
        return sDatabaseInstance;
    }

    public static MyApplication getMyApplication() {
        return myApplication;
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        // Initializing ACRA
//        ACRA.init(this);
//    }
}
