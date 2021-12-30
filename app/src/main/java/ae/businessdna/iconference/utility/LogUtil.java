//package ae.businessdna.iconference.utility;
//
//import android.os.Environment;
//import android.text.TextUtils;
//import android.util.Log;
//
//import org.acra.ACRA;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//import static ae.businessdna.iconference.utility.Constants.LOG_DIR;
//import static ae.businessdna.iconference.utility.Constants.LOG_TIME;
//import static ae.businessdna.iconference.utility.Constants.MESSAGE;
//import static ae.businessdna.iconference.utility.Constants.PARENT_DIR;
//import static ae.businessdna.iconference.utility.Constants.REQUEST_CODE;
//import static ae.businessdna.iconference.utility.Constants.REQUEST_NAME;
//import static ae.businessdna.iconference.utility.Constants.REQUEST_URI;
//import static ae.businessdna.iconference.utility.Constants.RESULT;
//
///**
// * Project : iConference
// * Created by rohith on 9/6/2017.
// */
//
//public class LogUtil {
//    private FileWriter logWriter = null;
//
//    public void sendACRA(Throwable throwable) {
//        // Sends log without alerting the user
//        ACRA.getErrorReporter().handleSilentException(throwable);
//    }
//
//    public void sendRemoteLog(String result, String requestName, String requestCode, String requestUri, String message) {
//        if (!TextUtils.isEmpty(result)) {
//            // Custom log data
//            ACRA.getErrorReporter().putCustomData(RESULT, result);
//        }
//        if (!TextUtils.isEmpty(requestName)) {
//            // Custom log data
//            ACRA.getErrorReporter().putCustomData(REQUEST_NAME, requestName);
//        }
//        if (!TextUtils.isEmpty(requestCode)) {
//            // Custom log data
//            ACRA.getErrorReporter().putCustomData(REQUEST_CODE, requestCode);
//        }
//        if (!TextUtils.isEmpty(requestUri)) {
//            // Custom log data
//            ACRA.getErrorReporter().putCustomData(REQUEST_URI, requestUri);
//        }
//        if (!TextUtils.isEmpty(message)) {
//            // Custom log data
//            ACRA.getErrorReporter().putCustomData(MESSAGE, message);
//        }
//
//
//        sendACRA(null);
//    }
//
//    // Function to generate log to be saved locally
//    public void generateLogMap(String result, String requestName, String requestCode, String requestUri, String message) {
//        HashMap<String, String> logMap = new HashMap<>();
//        if (!TextUtils.isEmpty(result)) {
//            logMap.put(RESULT, result);
//        }
//        if (!TextUtils.isEmpty(requestName)) {
//            logMap.put(REQUEST_NAME, requestName);
//        }
//        if (!TextUtils.isEmpty(requestCode)) {
//            logMap.put(REQUEST_CODE, requestCode);
//        }
//        if (!TextUtils.isEmpty(requestUri)) {
//            logMap.put(REQUEST_URI, requestUri);
//        }
//        logMap.put(MESSAGE, message);
//        logMap.put(LOG_TIME, DateUtil.getCurrentDateTime());
//        save(logMap);
//    }
//
//    private void initLogFile() {
//        File parentDir = new File(Environment.getExternalStorageDirectory(), PARENT_DIR);
//
//        if (!parentDir.isDirectory() || !parentDir.exists()) {
//            parentDir.mkdir();
//        }
//
//        File innerDir = new File(parentDir, LOG_DIR);
//
//        if (!innerDir.isDirectory() || !innerDir.exists()) {
//            innerDir.mkdir();
//        }
//
//        File logFile = new File(innerDir, DateUtil.getSplitDateString() + "_log.txt");
//        try {
//            logWriter = new FileWriter(logFile, true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void save(HashMap<String, String> logMap) {
//        initLogFile();
//        try {
//            BufferedWriter buf = new BufferedWriter(logWriter);
//
//            Set<Map.Entry<String, String>> set = logMap.entrySet();
//
//            for (Map.Entry<String, String> me : set) {
//                buf.append("[").append(me.getKey()).append("]=").append(me.getValue());
//            }
//
//            buf.flush();
//            buf.close();
//        } catch (IOException e) {
//            Log.e("TAG", "IO ERROR", e);
//        }
//    }
//}
