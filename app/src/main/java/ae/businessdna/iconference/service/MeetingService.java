package ae.businessdna.iconference.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Project : iConference
 * Created by rohith on 7/20/2017.
 */

public class MeetingService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
