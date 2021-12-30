package ae.businessdna.iconference.utility;

import android.content.Context;

/**
 * Project : iConference
 * Created by rohith on 7/19/2017.
 */

public class ResourceUtil {
    public static String getResourceString(Context context, int resId) {
        return context.getResources().getString(resId);
    }
}
