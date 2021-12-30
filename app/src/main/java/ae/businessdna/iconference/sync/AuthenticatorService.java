//package ae.businessdna.iconference.sync;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//
///**
// * Project : iConference
// * Created by rohith on 7/20/2017.
// */
//
//public class AuthenticatorService extends Service {
//    private AccountAuthenticator authenticator;
//
//
//    @Override
//    public void onCreate() {
//        // Instantiate our authenticator when the service is created
//        this.authenticator = new AccountAuthenticator(this);
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        // Return the authenticator's IBinder
//        return authenticator.getIBinder();
//    }
//}
