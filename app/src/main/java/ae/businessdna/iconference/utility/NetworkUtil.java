package ae.businessdna.iconference.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Project : iConference
 * Created by rohith on 7/18/2017.
 */

public class NetworkUtil {
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork && activeNetwork.isAvailable()) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnected())
                return true; //1

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE && activeNetwork.isConnected())
                return true; //2

        }
        return false;
    }
}
