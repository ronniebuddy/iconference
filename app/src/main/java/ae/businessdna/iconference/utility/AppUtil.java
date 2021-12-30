package ae.businessdna.iconference.utility;

import android.content.Context;

import com.microsoft.graph.authentication.IAuthenticationAdapter;
import com.microsoft.graph.authentication.MSAAuthAndroidAdapter;

/**
 * Project : iConference
 * Created by rohith on 2/1/2018.
 */

public class AppUtil {
    public final static String CLIENT_ID = "f882194e-6340-49cb-be25-61ce4b9f4920";
    public final static String SCOPES[] = {"https://graph.microsoft.com/User.Read",
            "https://graph.microsoft.com/Calendars.Read"};
    public final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me";
    public final static String MSGRAPH_URL_USERS = "https://graph.microsoft.com/v1.0/users";
}
