package ae.businessdna.iconference.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.microsoft.graph.authentication.IAuthenticationAdapter;
import com.microsoft.graph.authentication.MSAAuthAndroidAdapter;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.core.DefaultClientConfig;
import com.microsoft.graph.core.GraphErrorCodes;
import com.microsoft.graph.core.IBaseClient;
import com.microsoft.graph.core.IClientConfig;
import com.microsoft.graph.extensions.GraphServiceClient;
import com.microsoft.graph.extensions.IEventCollectionPage;
import com.microsoft.graph.extensions.IGraphServiceClient;
import com.microsoft.graph.http.BaseRequest;
import com.microsoft.graph.http.HttpMethod;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import ae.businessdna.iconference.R;
import ae.businessdna.iconference.adapters.MeetingAdapter;
import ae.businessdna.iconference.bean.MeetingHeader;
import ae.businessdna.iconference.fragments.MeetingFragment;
import ae.businessdna.iconference.fragments.MeetingInfoFragment;
import ae.businessdna.iconference.helper.ConferenceHelper;
import ae.businessdna.iconference.helper.MyExceptionHandler;
import ae.businessdna.iconference.receiver.MeetingReceiver;
import ae.businessdna.iconference.receiver.MyAdminReceiver;
import ae.businessdna.iconference.service.KioskService;
import ae.businessdna.iconference.utility.ClickableViewPager;
import ae.businessdna.iconference.utility.Constants;
import ae.businessdna.iconference.utility.DateUtil;
import ae.businessdna.iconference.utility.DepthPageTransformer;
import ae.businessdna.iconference.utility.HttpResponseParser;
import ae.businessdna.iconference.utility.NetworkUtil;
import ae.businessdna.iconference.utility.ResourceUtil;
import io.fabric.sdk.android.Fabric;

import static ae.businessdna.iconference.helper.ConferenceHelper.deleteAllMeetingAttendees;
import static ae.businessdna.iconference.helper.ConferenceHelper.deleteAllMeetingHeader;
import static ae.businessdna.iconference.utility.Constants.IDLE_TIME_IN_MILLIS;
import static ae.businessdna.iconference.utility.Constants.NIL;
import static ae.businessdna.iconference.utility.DateUtil.getCurrentDate;
import static ae.businessdna.iconference.utility.DateUtil.getNextDate;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, MeetingFragment.MeetingLoadListener, MeetingInfoFragment.MeetingInfoListener, Animation.AnimationListener {
    private static final String TAG = "MainActivity";

    //        private FrameLayout mFlVideo;
    private TextView mTvRoomTitle, mTvFloorTitle, mTvCurrentDate, mTvEmpty;
    //    private TextView mTvCurrentTime;
    private ImageView mIvLeft, mIvRight;
    private ImageButton mIbAccessSettings;
    private RelativeLayout mRlEmpty;
    private List<MeetingHeader> meetingHeaderList;
    //    private List<MeetingDetail> meetingDetailList;
    private int i = 0;
    private int selectedPosition = -1;
    private int globalWidth = 0;
    //    private String serviceUrl, companyId, floorId, roomId, email;
    private String loadMeetingId, setEmpName, createdByName, roomName, floorName;
    boolean settingsValid = false;
    boolean isPaused = false;
    private HttpResponseParser httpResponseParser;
    private ConferenceHelper conferenceHelper;
    //    LogUtil logUtil;
    //    private MeetingFragment meetingFragment;
//    private ViewPager viewPager;
    private ClickableViewPager viewPager;
    //    private ViewPagerArrowIndicator viewPagerArrowIndicator;
    private MeetingAdapter adapter;
    Fragment fragment;
    private MediaPlayer mp = null;
    private SurfaceView mSurfaceView = null;
    private ProgressDialog mProgressDialog;
    private AlertDialog alertDialog = null;
    //    private AsyncTaskGetMeetingDetail asyncTaskGetMeetingDetail;
    private Handler mHandler = new Handler();
    private Timer timer;
    private AlarmManager alarmMgr;
    PendingIntent fetchMeetingIntent;
    private Intent meetingIntent;
    static final int ACTIVATION_REQUEST_SETTINGS = 99;
    static final int ACTIVATION_REQUEST_ADMIN = 0;
    private Animation animFadeIn, animFadeOut;
    private LinearLayout mLlBottom;
    private Runnable decor_view_settings;
    private int viewFlags = 0;
    private customViewGroup view;
    private WindowManager windowManager;

    /* Azure AD Variables */
//    private PublicClientApplication meetingApp;
//    private AuthenticationResult authResult;
    private IAuthenticationAdapter iAuthenticationAdapter;
    private IClientConfig config;
    IGraphServiceClient client;
    private ICallback<IEventCollectionPage> callback;
    //    private ICallback<ICalendarCollectionPage> callback;
    JSONObject response;

    boolean deviceOwner, appLocked;
    DevicePolicyManager mDPM;
    ComponentName mComponentName;
    ActivityManager activityManager;
    String[] packages;
//    private static final String MY_AUTHORIZED_APP = "ae.businessdna.iconference";
//    private static final String[] AUTHORIZED_PINNING_APPS = {MY_AUTHORIZED_APP};
//    private static final String[] NO_AUTHORIZED_PINNING_APP = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
//        if (Build.VERSION.SDK_INT >= 19)    // KitKat{
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setupBars19();
//        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
//        Fabric.with(MainActivity.this, new Crashlytics());
        if (getIntent().getBooleanExtra("crash", false)) {
//            Toast.makeText(this, "App restarted after crash", Toast.LENGTH_SHORT).show();
            Crashlytics.log("Restarted after crash");

        }


        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        mDPM = (DevicePolicyManager) this.getSystemService(DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, MyAdminReceiver.class);
        packages = new String[]{this.getPackageName()};

//        if (!deviceOwner) {
//            try {
//                Runtime.getRuntime().exec("dpm set-device-owner ae.businessdna.iconference/ae.businessdna.iconference.receiver.MyAdminReceiver");
//                deviceOwner = true;
//            } catch (Exception e) {
//                Log.e(TAG, "device owner not set");
//                Log.e(TAG, e.toString());
//                e.printStackTrace();
//            }
//        }


        //        if (mDPM.isDeviceOwnerApp(this.getPackageName())) {
//            Log.d(TAG, "isDeviceOwnerApp: YES");
//            String[] packages = {this.getPackageName()};
//            mDPM.setLockTaskPackages(mComponentName, packages);
//        } else {
//            Log.d(TAG, "isDeviceOwnerApp: NO");
//        }
//
//        mDPM.setLockTaskPackages(mDeviceAdminRcvr, {"com.android.test.authorizedpinningapp"});
        initViews();
//        if(!conferenceHelper.getAllMeetingHeaders().isEmpty()) {
//            deleteAllMeetingHeader();
//            deleteAllMeetingAttendees();
//        }
        initMeetingCallback();


//        /* Configure your sample app and save state for this activity */
//        meetingApp = null;
//        if (meetingApp == null) {
//            meetingApp = new PublicClientApplication(
//                    this.getApplicationContext(),
//                    AppUtil.CLIENT_ID);
//        }
//
//        /* Attempt to get a user and acquireTokenSilent
//         * If this fails we do an interactive request
//         */
//        List<User> users;
//
//        try {
//            users = meetingApp.getUsers();
//
//            if (users != null && users.size() == 1) {
//                /* We have 1 user */
//
//                meetingApp.acquireTokenSilentAsync(SCOPES, users.get(0), getAuthSilentCallback());
//            } else {
//                /* We have no user */
//                onCallGraphClicked();
//            }
//        } catch (MsalClientException e) {
//            Log.d(TAG, "MSAL Exception Generated while getting users: " + e.toString());
//
//        } catch (IndexOutOfBoundsException e) {
//            Log.d(TAG, "User at this position does not exist: " + e.toString());
//        }
    }

//    @Override
//    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onPostCreate(savedInstanceState, persistentState);
//
//        requestAdminAccess();
//    }

    private void requestAdminAccess() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
        startActivityForResult(intent, ACTIVATION_REQUEST_ADMIN);
    }

    private void initMeetingCallback() {
        // Create a callback that can understand if AuthenticationFailure occurred.
        callback = new ICallback<IEventCollectionPage>() {
            @Override
            public void success(final IEventCollectionPage events) {
                List<MeetingHeader> parsedMeetingHeaderList;
                //Handle successful logout
                Log.d(TAG, "Events : " + events.toString());
                try {
                    response = new JSONObject(events.getRawObject().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                conferenceHelper.deleteAttendees();
                parsedMeetingHeaderList = conferenceHelper.parseMeetingHeaders(conferenceHelper.parseJSONObjectMeetingHeader(response));
                if (parsedMeetingHeaderList.size() > 0) {
                    conferenceHelper.saveMeetingHeader(parsedMeetingHeaderList);
                }
//                conferenceHelper.deleteFinishedMeetings();
                if (meetingHeaderList == null) {
                    meetingHeaderList = new ArrayList<>();
                } else {
                    meetingHeaderList.clear();
                }
                // Display meetings
                meetingHeaderList.addAll(conferenceHelper.getCurrentMeetingHeader());
                if (meetingHeaderList.size() > 0) {
                    mRlEmpty.setVisibility(View.GONE);
                    mTvEmpty.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                    setupViewPager();

                } else {
                    viewPager.setVisibility(View.GONE);
                    mIvLeft.setVisibility(View.GONE);
                    mIvRight.setVisibility(View.GONE);
                    mRlEmpty.setVisibility(View.VISIBLE);
                    mTvEmpty.setVisibility(View.VISIBLE);
                    mTvEmpty.setText(R.string.no_meetings);
                    roomName = "";
                    floorName = "";
                    setDateView();
//                    setRoomFloorInfo();
                }
                setReceiveMeetingsAlarm();
            }

            @Override
            public void failure(final ClientException ex) {
                if (ex.isError(GraphErrorCodes.AuthenticationFailure)) {
                    // Reset application to login again
//                    Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    showCustomDialog("Please try again");
                } else {
                    Log.d(TAG, "Client exception: " + ex.getMessage());
                }

                // Handle other failed causes
            }
        };
    }

    private void initAuthentication() {
        iAuthenticationAdapter = getAuthenticationAdapter();
        iAuthenticationAdapter.login(this, new ICallback<Void>() {
            @Override
            public void success(Void aVoid) {
                Log.d(TAG, "Logged in successfully");
                createGraphServiceClient();
            }

            @Override
            public void failure(ClientException ex) {
                if (ex.isError(GraphErrorCodes.AuthenticationFailure)) {
                    // Reset application to login again
//                    Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    showCustomDialog("Check app ID in settings and try again");
                } else if (ex.isError(GraphErrorCodes.AccessDenied)) {
                    showCustomDialog("Access denied");
                } else {
                    Crashlytics.logException(ex);
                }
            }
        });
    }

    private void createGraphServiceClient() {
        // Use the authentication provider previously defined within the project and create a configuration instance
        config = DefaultClientConfig.createWithAuthenticationProvider(iAuthenticationAdapter);

        // Create the service client from the configuration
        client = new GraphServiceClient.Builder()
                .fromConfig(config)
                .buildClient();

        // Make a request for the current user object
        createEventsRequest();
//        setReceiveMeetingsAlarm();

//        final List<Option> options = new LinkedList<>();
//        options.add(new HeaderOption("Authorization", "Bearer " + iAuthenticationAdapter.getAccessToken()));
//        CustomRequest request = new CustomRequest("https://graph.microsoft.com/v1.0/users/it.rd@nccauh.ae/events", client, options);
//        request.get();
//        request.get(resultICallback);
    }

    private void createEventsRequest() {

        final List<Option> options = new LinkedList<>();
        options.add(new QueryOption("startDateTime", getCurrentDate() + "T00:00:00"));
        options.add(new QueryOption("endDateTime", getNextDate() + "T00:00:00"));
        client
                .getMe()
//                .getUsers(Constants.EMAIL)
                .getCalendarView()
                .buildRequest(options)
                .get(callback);
    }

    /* Use MSAL to acquireToken for the end-user
     * Callback will call Graph api w/ access token & update UI
     */
//    private void onCallGraphClicked() {
//        meetingApp.acquireToken(MainActivity.this, SCOPES, getAuthInteractiveCallback());
//    }

    /* Callback used in for silent acquireToken calls.
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */
//    private AuthenticationCallback getAuthSilentCallback() {
//        return new AuthenticationCallback() {
//            @Override
//            public void onSuccess(AuthenticationResult authenticationResult) {
//                /* Successfully got a token, call graph now */
//                Log.d(TAG, "Successfully authenticated");
//
//                /* Store the authResult */
//                authResult = authenticationResult;
//
//                /* call graph */
//                callGraphAPI();
//            }
//
//            @Override
//            public void onError(MsalException exception) {
//                /* Failed to acquireToken */
//                Log.d(TAG, "Authentication failed: " + exception.toString());
//
//                if (exception instanceof MsalClientException) {
//                    /* Exception inside MSAL, more info inside MsalError.java */
//                } else if (exception instanceof MsalServiceException) {
//                    /* Exception when communicating with the STS, likely config issue */
//                } else if (exception instanceof MsalUiRequiredException) {
//                    /* Tokens expired or no session, retry with interactive */
//                }
//            }
//
//            @Override
//            public void onCancel() {
//                /* User canceled the authentication */
//                Log.d(TAG, "User cancelled login.");
//            }
//        };
//    }

    /* Callback used for interactive request.  If succeeds we use the access
     * token to call the Microsoft Graph. Does not check cache
     */
//    private AuthenticationCallback getAuthInteractiveCallback() {
//        return new AuthenticationCallback() {
//            @Override
//            public void onSuccess(AuthenticationResult authenticationResult) {
//                /* Successfully got a token, call graph now */
//                Log.d(TAG, "Successfully authenticated");
//                Log.d(TAG, "ID Token: " + authenticationResult.getIdToken());
//
//                /* Store the auth result */
//                authResult = authenticationResult;
//
//                /* call graph */
//                callGraphAPI();
//            }
//
//            @Override
//            public void onError(MsalException exception) {
//                /* Failed to acquireToken */
//                Log.d(TAG, "Authentication failed: " + exception.toString());
//
//                if (exception instanceof MsalClientException) {
//                    /* Exception inside MSAL, more info inside MsalError.java */
//                } else if (exception instanceof MsalServiceException) {
//                    /* Exception when communicating with the STS, likely config issue */
//                }
//            }
//
//            @Override
//            public void onCancel() {
//                /* User canceled the authentication */
//                Log.d(TAG, "User cancelled login.");
//            }
//        };
//    }

    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
//    private void callGraphAPI() {
//        Log.d(TAG, "Starting volley request to graph");
//
//        /* Make sure we have a token to send to graph */
//        if (authResult.getAccessToken() == null) {
//            return;
//        }
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        JSONObject parameters = new JSONObject();
//
//        try {
//            parameters.put("key", "value");
//        } catch (Exception e) {
//            Log.d(TAG, "Failed to put parameters: " + e.toString());
//        }
////        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MSGRAPH_URL + "/calendar",
////        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MSGRAPH_URL_USERS
//                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MSGRAPH_URL,
////                + "/events",
////                + "/events?$select=subject,bodyPreview,organizer,attendees,start,end,location",
////                + "/events?$top=" + 5 + "&$select=subject,bodyPreview,organizer,attendees,start,end,location",
////                        + "/events('AAMkAGZkNTRkMjFkLWVmNjYtNGNlNi1hMTc4LTY0NjliNjAxNGMyOABGAAAAAAB8nNbHfUO8Ta0jA1iA8X9DBwBtRfJD4C--RLxIpzzl_WUHAAAAAAENAABtRfJD4C--RLxIpzzl_WUHAAHicKSSAAA=')" +
////                        "?$select=subject,bodyPreview,organizer,attendees,start,end,location",
////                                                + "/events/AAMkAGZkNTRkMjFkLWVmNjYtNGNlNi1hMTc4LTY0NjliNjAxNGMyOABGAAAAAAB8nNbHfUO8Ta0jA1iA8X9DBwBtRfJD4C--RLxIpzzl_WUHAAAAAAENAABtRfJD4C--RLxIpzzl_WUHAAHicKSSAAA=",
////                        + "/calendarView?startDateTime=2018-01-31T00:00:00&endDateTime=2018-01-31T23:59:00",
////                        + "/calendarView/delta?startDateTime=2018-01-31T00:00:00&endDateTime=2018-01-31T23:59:00",
////                + "/calendarView/delta?$skiptoken=uwMsonT1N46Me49COq9SD_HSclDIxVXQJwua5DL9_QAJ18iUaLSzQ4YpRMZ93vl-7UlEXY58H6Ek0YXk9MluT39HnZE8helI2r3TuljzZaotrVnm45GWvGkd0tnpkSPqy3TlNZZwGwC46c7qOGpSb_8LOxLFlgwHHuG7eaDQaW41xIblT2e7VR0eQWtrx4cEZFJY3tqpZ-jq3k0mkP9JiQ.8vegf653hKhxBVpeoDgf7ftwCbEVTN6R9hxiwVubWQs",
////                + "/calendarView/delta?$deltatoken=uwMsonT1N46Me49COq9SD5Q_IgDBTojR6TiIxjgPERi97BPg4R2_qBGRuduBDWoGJ4VnOZ3eeAEk9K6r3mvHTAtc3EFh4YcOMVP7OXmr3IW3NK5hkbEMiXRz6HqeBMGfphzGVF-ycqTYRMXbpH_sKLeZ9BBvcFMiCoGD5K5cDjBywsCuNr7uW9B1RFJR5e-s_RScdK6Id9nX01b2FaBRHQ.2ln9rarWDSBKAZWBMketKgcWPW-NIW_QP_tRsslorgE",
//                parameters, response -> {
//                    /* Successfully called graph, process data and send to UI */
//            Log.d(TAG, "Response: " + response.toString());
//
////            conferenceHelper.parseJSONObjectMeetingHeader(response);
////
////            if(meetingHeaderList == null) {
////                meetingHeaderList = new ArrayList<>();
////            } else {
////                meetingHeaderList.clear();
////            }
////            // Display meetings
////            meetingHeaderList.addAll(conferenceHelper.getMeetingHeaders());
////            if (meetingHeaderList.size() > 0) {
////                mRlEmpty.setVisibility(View.GONE);
////                viewPager.setVisibility(View.VISIBLE);
////                setupViewPager();
////
////            } else {
////                viewPager.setVisibility(View.GONE);
////                mRlEmpty.setVisibility(View.VISIBLE);
////                mTvEmpty.setText(R.string.no_meetings);
////                roomName = "";
////                floorName = "";
////                setRoomFloorInfo();
////            }
//        }, error -> Log.d(TAG, "Error: " + error.toString())) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer " + authResult.getAccessToken());
//                return headers;
//            }
//        };
//
//        JsonObjectRequest requestUsers = new JsonObjectRequest(Request.Method.GET, MSGRAPH_URL_USERS
////                + "/events",
//                + "/it.rd@nccauh.ae/events",
////                + "/events?$top=" + 5 + "&$select=subject,bodyPreview,organizer,attendees,start,end,location",
////                        + "/events('AAMkAGZkNTRkMjFkLWVmNjYtNGNlNi1hMTc4LTY0NjliNjAxNGMyOABGAAAAAAB8nNbHfUO8Ta0jA1iA8X9DBwBtRfJD4C--RLxIpzzl_WUHAAAAAAENAABtRfJD4C--RLxIpzzl_WUHAAHicKSSAAA=')" +
////                        "?$select=subject,bodyPreview,organizer,attendees,start,end,location",
////                                                + "/events/AAMkAGZkNTRkMjFkLWVmNjYtNGNlNi1hMTc4LTY0NjliNjAxNGMyOABGAAAAAAB8nNbHfUO8Ta0jA1iA8X9DBwBtRfJD4C--RLxIpzzl_WUHAAAAAAENAABtRfJD4C--RLxIpzzl_WUHAAHicKSSAAA=",
////                        + "/calendarView?startDateTime=2018-01-31T00:00:00&endDateTime=2018-01-31T23:59:00",
////                        + "/calendarView/delta?startDateTime=2018-01-31T00:00:00&endDateTime=2018-01-31T23:59:00",
////                + "/calendarView/delta?$skiptoken=uwMsonT1N46Me49COq9SD_HSclDIxVXQJwua5DL9_QAJ18iUaLSzQ4YpRMZ93vl-7UlEXY58H6Ek0YXk9MluT39HnZE8helI2r3TuljzZaotrVnm45GWvGkd0tnpkSPqy3TlNZZwGwC46c7qOGpSb_8LOxLFlgwHHuG7eaDQaW41xIblT2e7VR0eQWtrx4cEZFJY3tqpZ-jq3k0mkP9JiQ.8vegf653hKhxBVpeoDgf7ftwCbEVTN6R9hxiwVubWQs",
////                + "/calendarView/delta?$deltatoken=uwMsonT1N46Me49COq9SD5Q_IgDBTojR6TiIxjgPERi97BPg4R2_qBGRuduBDWoGJ4VnOZ3eeAEk9K6r3mvHTAtc3EFh4YcOMVP7OXmr3IW3NK5hkbEMiXRz6HqeBMGfphzGVF-ycqTYRMXbpH_sKLeZ9BBvcFMiCoGD5K5cDjBywsCuNr7uW9B1RFJR5e-s_RScdK6Id9nX01b2FaBRHQ.2ln9rarWDSBKAZWBMketKgcWPW-NIW_QP_tRsslorgE",
//                parameters, response -> {
//                    /* Successfully called graph, process data and send to UI */
//            Log.d(TAG, "Response: " + response.toString());
//
////                    updateGraphUI(response);
////            conferenceHelper.parseJSONObjectMeetingHeader(response);
////
////            if(meetingHeaderList == null) {
////                meetingHeaderList = new ArrayList<>();
////            } else {
////                meetingHeaderList.clear();
////            }
////            // Display meetings
////            meetingHeaderList.addAll(conferenceHelper.getMeetingHeaders());
////            if (meetingHeaderList.size() > 0) {
////                mRlEmpty.setVisibility(View.GONE);
////                viewPager.setVisibility(View.VISIBLE);
////                setupViewPager();
////
////            } else {
////                viewPager.setVisibility(View.GONE);
////                mRlEmpty.setVisibility(View.VISIBLE);
////                mTvEmpty.setText(R.string.no_meetings);
////                roomName = "";
////                floorName = "";
////                setRoomFloorInfo();
////            }
//        }, error ->
//                Log.d(TAG, "Error: " + error.toString())) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer " + authResult.getAccessToken());
//                return headers;
//            }
//        };
//
//        Log.d(TAG, "Adding HTTP GET to Queue, Request: " + request.toString());
//
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                3000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        requestUsers.setRetryPolicy(new DefaultRetryPolicy(
//                3000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        queue.add(request);
//        queue.add(requestUsers);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        preventStatusBarExpansion(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("ae.businessdna.iconference.notification"));
        getUserSettings();
        Crashlytics.setUserName(Constants.ROOM_NAME);
        checkSettings();
        if (!checkSettings()) {
            cancelAlarm();
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else {
            if (isKioskModeActive(this)) {
                if (mDPM.isDeviceOwnerApp(getPackageName())) {
                    mDPM.setLockTaskPackages(mComponentName, packages);
                    startLockTask();
//                    appLocked = true;
                } else {
                    Log.d(TAG, "Not owner");
//                    getPackageManager().clearPackagePreferredActivities(getPackageName());
                }
            } else {
                if (activityManager.isInLockTaskMode()) {
                    stopLockTask();
//                    appLocked = false;

//                    mDPM.clearDeviceOwnerApp(packages[0]);
////                    deviceOwner = false;
                }
            }

//            // Set floor name
            setFloorNameView();
//            // Get authentication adapter
//
            if (NetworkUtil.isConnected(this)) {
                if (config == null && client == null) {
                    Log.d(TAG, "config, client null");
                    initAuthentication();
                } else {
                    Log.d(TAG, "config, client not null");
                    Log.d(TAG, "creating request");
                    createEventsRequest();
                }
            } else {
//                dismissAlertDialog();
//                showCustomDialog(ResourceUtil.getResourceString(MainActivity.this, R.string.no_connectivity));
                viewPager.setVisibility(View.GONE);
                mIvLeft.setVisibility(View.GONE);
                mIvRight.setVisibility(View.GONE);
                mRlEmpty.setVisibility(View.VISIBLE);
                mTvEmpty.setVisibility(View.VISIBLE);
                mTvEmpty.setText(R.string.no_connectivity);
                roomName = "";
                floorName = "";
                setDateView();
//                setRoomFloorInfo();
                setReceiveMeetingsAlarm();
            }
//            requestMeeting();
        }
    }

    private void setFloorNameView() {
        mTvFloorTitle.setText(this.getResources().getString(R.string.meeting_floor_info, Constants.ROOM_NAME.toUpperCase()));
        TextViewCompat.setAutoSizeTextTypeWithDefaults(mTvFloorTitle, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        dismissProgressDialog();
        cancelTimer();
        mHandler.removeCallbacks(mIdleRunnable);
        removeDisabledStatusView();
        if (mp.isPlaying()) {
            mp.pause();
            isPaused = true;
        }
        cancelAlarm();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(decor_view_settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
//                mFlVideo = (FrameLayout) findViewById(R.id.fl_video);
        mTvRoomTitle = findViewById(R.id.tv_room_info);
        mTvFloorTitle = findViewById(R.id.tv_floor_info);
//        mTvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        mTvEmpty = findViewById(R.id.tv_empty);
        mTvCurrentDate = findViewById(R.id.tv_current_date);
        mLlBottom = findViewById(R.id.ll_inner_bottom);
        mIvLeft = findViewById(R.id.iv_left);
        mIvRight = findViewById(R.id.iv_right);
        mIbAccessSettings = findViewById(R.id.settings_access);

//        TextView mTitle = findViewById(R.id.tv_title);
//        mTitle.setOnClickListener(v -> {
//            Crashlytics.getInstance().crash();
//            throw new RuntimeException("This is a crash");
//        });


        mIvLeft.setOnClickListener(v -> {
            int pos = viewPager.getCurrentItem();
            if (pos == 0) {
                viewPager.setCurrentItem(pos);
            } else {
                pos--;
                viewPager.setCurrentItem(pos);
            }
        });

        mIvRight.setOnClickListener(v -> {
            int pos = viewPager.getCurrentItem();
            pos++;
            viewPager.setCurrentItem(pos);
        });

        mIbAccessSettings.setOnClickListener(view -> {
            i++;
            Log.d(TAG, "i = " + i);
            if (i == 5) {
                i = 0;
                showAccessSettingsDialog();
            }
        });

        alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        viewPager = findViewById(R.id.viewpager);
//        viewPagerArrowIndicator = (ViewPagerArrowIndicator)
//                findViewById (R.id.viewpagerarrowındicatis);
        mRlEmpty = findViewById(R.id.rl_empty);

        conferenceHelper = new ConferenceHelper(this);
        httpResponseParser = new HttpResponseParser();
//        logUtil = new LogUtil();
//        meetingFragment = new MeetingFragment();

        mp = new MediaPlayer();
        mSurfaceView = findViewById(R.id.sv_video);
        mSurfaceView.getHolder().addCallback(this);

        viewPager.setPageTransformer(true, new DepthPageTransformer());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                viewPager.setCurrentItem(position);
                Log.d(TAG, "Page Selected : " + position);
                MeetingHeader meetingHeader;
                String locationValue;
                selectedPosition = position;
                if (meetingHeaderList.size() > 1) {
                    if (selectedPosition == 0) {
                        mIvLeft.setVisibility(View.INVISIBLE);
                        mIvRight.setVisibility(View.VISIBLE);
                    } else if (selectedPosition == meetingHeaderList.size() - 1) {
                        mIvLeft.setVisibility(View.VISIBLE);
                        mIvRight.setVisibility(View.INVISIBLE);
                    } else {
                        mIvLeft.setVisibility(View.VISIBLE);
                        mIvRight.setVisibility(View.VISIBLE);
                    }
                } else if (meetingHeaderList.size() == 1) {
                    mIvLeft.setVisibility(View.INVISIBLE);
                    mIvRight.setVisibility(View.INVISIBLE);
                }
                loadMeetingId = meetingHeaderList.get(position).meetingID;
                meetingHeader = conferenceHelper.getMeetingHeaderById(loadMeetingId);
                setMeetingDetailsView(meetingHeader);
                locationValue = meetingHeader.location;
//                String[] location;
//                if (!TextUtils.isEmpty(locationValue)) {
//                    if (locationValue.contains("-") || locationValue.contains(",")) {
//                        location = getSplitLocation(locationValue);
//                        if (location != null) {
//                            roomName = location[0].trim();
//                            floorName = location[1].trim();
//                        }
//                    } else {
//                        roomName = locationValue.trim();
//                        floorName = NIL;
//                    }
//                } else {
//                    roomName = NIL;
//                    floorName = NIL;
//                }


                if (TextUtils.isEmpty(locationValue)) {
                    roomName = NIL;
                } else {
                    roomName = locationValue;
                }
//                setRoomFloorInfo();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setOnItemClickListener(position -> {
            selectedPosition = position;
            openMeetingInfoDialog();
            cancelTimer();
            startTimer(IDLE_TIME_IN_MILLIS);
        });

        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        animFadeIn.setAnimationListener(fadeInListener);
        mLlBottom.startAnimation(animFadeIn);

//        mFlVideo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mFlVideo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                globalWidth = mFlVideo.getWidth();
//            }
//        });
    }

    private void openMeetingInfoDialog() {
        FragmentTransaction ft = MainActivity.this.getSupportFragmentManager().beginTransaction();

        Log.d(TAG, "Showing Save Item Fragment");
        // Create and show the dialog.
        DialogFragment newFragment = MeetingInfoFragment.newInstance(selectedPosition == -1 ?
                        getMeetingHeaderArray(meetingHeaderList.get(0)) :
                        getMeetingHeaderArray(meetingHeaderList.get(selectedPosition)),
                conferenceHelper.getAttendeesListById(loadMeetingId));
        newFragment.show(ft, "meeting_info_frag");
    }

    private String[] getMeetingHeaderArray(MeetingHeader passedHeader) {
        return new String[]{
                passedHeader.subject,
                passedHeader.createdByName,
                roomName,
                floorName,
        };
    }

    Animation.AnimationListener fadeInListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animFadeOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
            animFadeOut.setAnimationListener(fadeOutListener);
            mLlBottom.startAnimation(animFadeOut);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    Animation.AnimationListener fadeOutListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animFadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
            animFadeIn.setAnimationListener(fadeInListener);
            mLlBottom.startAnimation(animFadeIn);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    // Function retrieving values from persistent storage ( Shared Preference)
    private void getUserSettings() {
        SharedPreferences mSharedPref = PreferenceManager
                .getDefaultSharedPreferences(this);

//        String serviceUrlDefault = this.getResources().getString(R.string.pref_service_default);
//        String companyIdDefault = this.getResources().getString(R.string.pref_default);
//        String floorIdDefault = this.getResources().getString(R.string.pref_default);
//        String roomIdDefault = this.getResources().getString(R.string.pref_default);

        String emailDefault = this.getResources().getString(R.string.pref_email_default);
        String email, appID, roomName, fetchFrequency;

//        serviceUrl = mSharedPref.getString("pref_webservice_url", serviceUrlDefault).trim();

//        email = mSharedPref.getString("pref_email", emailDefault).trim();
//        Constants.EMAIL = email;

        appID = mSharedPref.getString("pref_app_id", emailDefault).trim();
        Constants.APP_ID = appID;

        roomName = mSharedPref.getString("pref_room", emailDefault).trim();
        Constants.ROOM_NAME = roomName;

        fetchFrequency = mSharedPref.getString("pref_freq", emailDefault).trim();
        Constants.FETCH_FREQ = fetchFrequency;

//        companyId = mSharedPref.getString("pref_company_id", companyIdDefault).trim();
//        floorId = mSharedPref.getString("pref_floor_id", floorIdDefault).trim();
//        roomId = mSharedPref.getString("pref_room_id", roomIdDefault).trim();
    }

    // Function checks the validation of setting parameters
    private boolean checkSettings() {
//        if (TextUtils.isEmpty(serviceUrl)) {
//            settingsValid = false;
//            dismissAlertDialog();
//            showToast(ResourceUtil.getResourceString(this, R.string.message_enter_url));
//        } else if (TextUtils.isEmpty(companyId)) {
//            settingsValid = false;
//            dismissAlertDialog();
//            showToast(ResourceUtil.getResourceString(this, R.string.message_enter_company_id));
//        } else if (TextUtils.isEmpty(floorId)) {
//            settingsValid = false;
//            dismissAlertDialog();
//            showToast(ResourceUtil.getResourceString(this, R.string.message_enter_floor_id));
//        } else if (TextUtils.isEmpty(roomId)) {
//            settingsValid = false;
//            dismissAlertDialog();
//            showToast(ResourceUtil.getResourceString(this, R.string.message_enter_room_id));
//        } else {
//            settingsValid = true;
//        }

        if (TextUtils.isEmpty(Constants.APP_ID)) {
            settingsValid = false;
            dismissAlertDialog();
            showToast(ResourceUtil.getResourceString(this, R.string.message_enter_app_id));
        } else if (TextUtils.isEmpty(Constants.ROOM_NAME)) {
            settingsValid = false;
            dismissAlertDialog();
            showToast(ResourceUtil.getResourceString(this, R.string.message_enter_room));
        } else if (TextUtils.isEmpty(Constants.FETCH_FREQ)) {
            settingsValid = false;
            dismissAlertDialog();
            showToast(ResourceUtil.getResourceString(this, R.string.message_enter_fetch_freq));
        } else {
            settingsValid = true;
        }
        return settingsValid;
    }

    //    @TargetApi(19)
    public void setupBars19() {
        viewFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        decor_view_settings = () -> {
            getWindow().getDecorView().setSystemUiVisibility(viewFlags);
        };

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(viewFlags);

        decorView.setOnSystemUiVisibilityChangeListener
                (visibility -> {
                    if ((visibility & View.SYSTEM_UI_FLAG_IMMERSIVE) == 0) {
                        mHandler.postDelayed(decor_view_settings, 5000);
                    }
                });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (Build.VERSION.SDK_INT < 19)    // KitKat
//            return;

        if (hasFocus)
            mHandler.postDelayed(decor_view_settings, 2000);
//        else
//            mHandler.removeCallbacks(decor_view_settings);

//        if (hasFocus) {
//            screenUtility.hideSystemUIOnVisible();
//        }
    }

    private void setReceiveMeetingsAlarm() {
        cancelAlarm();
        meetingIntent = new Intent(getApplicationContext(), MeetingReceiver.class);
        fetchMeetingIntent = PendingIntent.getBroadcast(this, 333, meetingIntent, 0);

// setRepeating() lets you specify a precise custom interval--in this case,
// 2 minutes.
        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 10 * 1000,
                60 * 1000, fetchMeetingIntent);
    }

    private void cancelAlarm() {
        if (meetingIntent != null) {
            PendingIntent.getBroadcast(this, 0, meetingIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT).cancel();
        }

        if (meetingIntent != null && fetchMeetingIntent != null) {
            meetingIntent = null;
            alarmMgr.cancel(fetchMeetingIntent);
        }
    }

    private void startTimer(int time) {
        if (timer == null) {
            timer = new Timer();
            idleTimer(time);
        }
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void idleTimer(int time) {
        if (timer != null) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.post(mIdleRunnable);
                }
            }, time);
        }
    }

    final Runnable mIdleRunnable = new Runnable() {
        @Override
        public void run() {
            MeetingInfoFragment fragment = (MeetingInfoFragment) getSupportFragmentManager().findFragmentByTag("meeting_info_frag");
            if (fragment != null) {
                fragment.dismiss();
            }
            viewPager.setCurrentItem(0);
            cancelTimer();
            startTimer(IDLE_TIME_IN_MILLIS);
        }
    };

    // Function for accessing settings activity provided a valid password
    public void showAccessSettingsDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogLayout = inflater.inflate(R.layout.custom_dialog_access_settings, null);

        final EditText mEtPassword = dialogLayout.findViewById(R.id.et_password);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogLayout);
        builder.setPositiveButton(ResourceUtil.getResourceString(this, R.string.title_ok), (dialog, which) -> {

            String password = mEtPassword.getText().toString().trim();
            Log.d(TAG, "Password Input :" + password);
            if (password.equals("")) {
                showToast(R.string.invalid_password);
            } else if (password.equals(ResourceUtil.getResourceString(MainActivity.this, R.string.default_settings_password))) {
                dialog.dismiss();
                alarmMgr.cancel(fetchMeetingIntent);
                startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), ACTIVATION_REQUEST_SETTINGS);
            } else {
                showToast(R.string.invalid_password);
            }
        })
                .setNegativeButton(ResourceUtil.getResourceString(MainActivity.this, R.string.title_cancel), (dialog, which) -> {

                    dialog.dismiss();
                    i = 0;

                }).create().show();
    }

    // Add Fragments to Tabs
    private void setupViewPager() {
//        meetingHeaderList = conferenceHelper.getMeetingHeaders();
        String locationValue;
        if (meetingHeaderList.size() > 0) {
            if (meetingHeaderList.size() == 1) {
                mIvLeft.setVisibility(View.GONE);
                mIvRight.setVisibility(View.GONE);
            } else {
                mIvLeft.setVisibility(View.INVISIBLE);
                mIvRight.setVisibility(View.VISIBLE);
            }
            adapter = new MeetingAdapter(getSupportFragmentManager());
            for (int i = 0; i < meetingHeaderList.size(); i++) {
                Fragment fragment = new MeetingFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("DETAIL", meetingHeaderList.get(i));
                fragment.setArguments(bundle);
                adapter.addFragment(fragment);
            }
            viewPager.setAdapter(adapter);
//            viewPagerArrowIndicator.bind(viewPager);
        }
        loadMeetingId = meetingHeaderList.get(0).meetingID;
        locationValue = meetingHeaderList.get(0).location;
//        String[] location;
//        if (!TextUtils.isEmpty(locationValue)) {
//            if (locationValue.contains("-") || locationValue.contains(",")) {
//                location = getSplitLocation(locationValue);
//                if (location != null) {
//                    roomName = location[0].trim();
//                    floorName = location[1].trim();
//                }
//            } else {
//                roomName = locationValue.trim();
//                floorName = NIL;
//            }
//        } else {
//            roomName = NIL;
//            floorName = NIL;
//        }

        if (TextUtils.isEmpty(locationValue)) {
            roomName = NIL;
        } else {
            roomName = locationValue;
        }
        setMeetingDetailsView(conferenceHelper.getMeetingHeaderById(loadMeetingId));
        setDateView();
//        setRoomFloorInfo();

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void requestMeeting() {
        if (settingsValid) {
            if (NetworkUtil.isConnected(this)) {
//                String[] params = new String[]{WebServices.SERVICE_URL + "/" + WebServices.GET_MEETING_DAY + "/" + companyId + "/" + floorId + "/" + roomId + "/" + DateUtil.getCurrentDate()};
//                AsyncTaskGetMeetingDay asyncTaskGetMeetingDay = new AsyncTaskGetMeetingDay();
//                asyncTaskGetMeetingDay.execute(params);
//                callGraphAPI();
                if (config == null && client == null) {
                    Log.d(TAG, "config, client null");
                    initAuthentication();
                } else {
                    Log.d(TAG, "config, client not null");
                    Log.d(TAG, "creating request");
                    createEventsRequest();
                }
            } else {
//                dismissAlertDialog();
//                showCustomDialog(ResourceUtil.getResourceString(MainActivity.this, R.string.no_connectivity));
                viewPager.setVisibility(View.GONE);
                mRlEmpty.setVisibility(View.VISIBLE);
                mTvEmpty.setVisibility(View.VISIBLE);
                mTvEmpty.setText(R.string.no_connectivity);
                roomName = "";
                floorName = "";
                setDateView();
//                setRoomFloorInfo();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.ncc);
        try {
            if (isPaused) {
//                //Get the dimensions of the video
                int videoWidth = mp.getVideoWidth();
                int videoHeight = mp.getVideoHeight();
//        int screenHeight = MainActivity.this.getResources().getDisplayMetrics().heightPixels;
                int screenWidth = dpToPx(360);
//                int screenWidth = globalWidth;

                //Get the width of the screen
//        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
//                int screenWidth = MainActivity.this.getResources().getDisplayMetrics().widthPixels;

                //Get the SurfaceView layout parameters
                android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();

                //Set the width of the SurfaceView to the width of the screen
//        lp.width = screenWidth;
                lp.width = screenWidth;

                //Set the height of the SurfaceView to match the aspect ratio of the video
                //be sure to cast these as floats otherwise the calculation will likely be 0
//        lp.height = (int) (((float) videoHeight / (float) videoWidth) * (float) screenWidth);
                lp.width = (int) (((float) videoWidth / (float) videoHeight) * (float) screenWidth);

                //Commit the layout parameters
                mSurfaceView.setLayoutParams(lp);

                //Start video
                mp.setDisplay(holder);
                mp.setLooping(true);
                mp.start();
                isPaused = false;
            } else {
                mp.setDataSource(MainActivity.this, video);
                mp.prepare();
                //Get the dimensions of the video
                int videoWidth = mp.getVideoWidth();
                int videoHeight = mp.getVideoHeight();
//        int screenHeight = MainActivity.this.getResources().getDisplayMetrics().heightPixels;
                int screenWidth = dpToPx(360);
//                int screenWidth = globalWidth;

                //Get the width of the screen
//        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
//                int screenWidth = MainActivity.this.getResources().getDisplayMetrics().widthPixels;

                //Get the SurfaceView layout parameters
                android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();

                //Set the width of the SurfaceView to the width of the screen
//        lp.width = screenWidth;
                lp.width = screenWidth;
//                lp.width = videoWidth;

                //Set the height of the SurfaceView to match the aspect ratio of the video
                //be sure to cast these as floats otherwise the calculation will likely be 0
//        lp.height = (int) (((float) videoHeight / (float) videoWidth) * (float) screenWidth);
                lp.width = (int) (((float) videoWidth / (float) videoHeight) * (float) screenWidth);

                //Commit the layout parameters
                mSurfaceView.setLayoutParams(lp);

                //Start video
                mp.setDisplay(holder);
                mp.setLooping(true);
                mp.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

//    @Override
//    public void loadDetails(String passedMeetingId) {
//        loadMeetingId = passedMeetingId;
////        if (conferenceHelper.meetingDetailExists(loadMeetingId)) {
//        parseMeetingDetail();
////        } else {
////            requestMeetingDetail(loadMeetingId);
////        }
//
////        if (meetingDetailList.size() > 0) {
////            for (MeetingDetail meetingDetail : meetingDetailList) {
////                if (loadMeetingId.equals(meetingDetail.meetingID)) {
////                    mTvRoomFloorView.setText(meetingDetail.roomName + " ," + meetingDetail.floorName);
////                    break;
////                }
////            }
////            mTvCurrentTime.setText(DateUtil.getCurrentTime());
////        }
//    }

    @Override
    public void showToastMain(String message) {

    }

//    private void requestMeetingDetail(String passedMeetingId) {
//        if (NetworkUtil.isConnected(this)) {
//            String[] params = new String[]{WebServices.SERVICE_URL + "/" + WebServices.GET_MEETING_DETAIL + "/" + passedMeetingId};
//            asyncTaskGetMeetingDetail = new AsyncTaskGetMeetingDetail();
//            asyncTaskGetMeetingDetail.execute(params);
//
//        } else {
//            showCustomDialog(ResourceUtil.getResourceString(MainActivity.this, R.string.no_connectivity));
//        }
//    }

    @Override
    public void hideNavigation() {
        mHandler.postDelayed(decor_view_settings, 2000);
    }

//    private class AsyncTaskGetMeetingDetail extends AsyncTask<String, Integer, JSONArray> {
//        AsyncTaskGetMeetingDetail() {
//            super();
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            dismissProgressDialog();
////            showProgressDialogIndeterminate("Fetching Meeting Detail...");
//        }
//
//        @Override
//        protected JSONArray doInBackground(String... params) {
//            HttpURLConnection httpURLConnection = null;
//            JSONArray jsonArray = null;
//            URL u = null;
//            int status = -1;
//            try {
//                u = new URL(params[0]);
//                httpURLConnection = (HttpURLConnection) u.openConnection();
//                httpURLConnection.setRequestMethod("GET");
//                httpURLConnection.setConnectTimeout(10000);
//                httpURLConnection.setReadTimeout(120 * 1000);
//                httpURLConnection.connect();
//                status = httpURLConnection.getResponseCode();
//                switch (status) {
//                    case HttpURLConnection.HTTP_OK: // Response OK
//                        try {
//                            // Parse incoming stream of data and add to list
//                            BufferedReader streamReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
//                            StringBuilder responseStrBuilder = new StringBuilder();
//
//                            String inputStr;
//                            while ((inputStr = streamReader.readLine()) != null)
//                                responseStrBuilder.append(inputStr);
//                            jsonArray = new JSONArray(responseStrBuilder.toString());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                    case HttpURLConnection.HTTP_BAD_REQUEST: //  Response 400
//                        Log.d(TAG, "Bad Request");
//                        // Send log to server
////                        logUtil.sendRemoteLog(FAILURE,
////                                GET_MEETING_DETAIL,
////                                String.valueOf(status),
////                                u.toString(),
////                                "");
////                        handler.sendEmptyMessage(2);
//                        break;
//                    case HttpURLConnection.HTTP_INTERNAL_ERROR: // Response 500
//                        Log.d(TAG, "Internal Server Error");
//                        // Send log to server
////                        logUtil.sendRemoteLog(FAILURE,
////                                GET_MEETING_DETAIL,
////                                String.valueOf(status),
////                                u.toString(),
////                                "");
////                        handler.sendEmptyMessage(3);
//                        break;
//                    case HttpURLConnection.HTTP_NOT_FOUND: // Response 404
//                        Log.d(TAG, "Page Not Found");
//                        // Send log to server
////                        logUtil.sendRemoteLog(FAILURE,
////                                GET_MEETING_DETAIL,
////                                String.valueOf(status),
////                                u.toString(),
////                                "");
////                        handler.sendEmptyMessage(4);
//                        break;
//                    default:
//                        Log.d(TAG, "Status Code : " + status); //  Unhandled response code
//                        // Send log to server
////                        logUtil.sendRemoteLog(FAILURE,
////                                GET_MEETING_DETAIL,
////                                String.valueOf(status),
////                                u.toString(),
////                                "");
////                        handler.sendEmptyMessage(5);
//                }
//
//            } catch (SocketTimeoutException se) {
////                handler.sendEmptyMessage(6);
//                // Send log to server
////                logUtil.sendRemoteLog(FAILURE,
////                        GET_MEETING_DETAIL,
////                        String.valueOf(status),
////                        u.toString(),
////                        "");
//                return null;
//            } catch (IOException ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            } finally {
//                if (httpURLConnection != null) {
//                    try {
//                        httpURLConnection.disconnect();
//                    } catch (Exception ex) {
//                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
//            return jsonArray;
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//        }
//
//        @Override
//        protected void onPostExecute(JSONArray result) {
//            super.onPostExecute(result);
//            // Dismiss any progress dialog
////            dismissProgressDialog();
//
//            if (result != null && result.length() > 0) { // If true, show success message
//                Log.d(TAG, "Inside GetItems Post Execute - True");
//                meetingDetailList = new ArrayList<>();
//                meetingDetailList.addAll(httpResponseParser.parseJSONObjectMeetingDetail(result));
//                if (meetingDetailList.size() > 0) {
////                    looped++;
//                    conferenceHelper.saveMeetingDetail(meetingDetailList);
//
////                    if (looped == headerSize) {
////                        setupViewPager(viewPager);
////                    }
//                    parseMeetingDetail();
//
//                } else {
//
//                }
////                    String empEmail = result.getString("EmployeeEmail");
////                    String empNo = result.getString("EmployeeNo");
////                    String empName = result.getString("Name");
////                    String show = "Employee Email : " + empEmail + "\n" + "Employee No : " + empNo + "\n" + "Employee Name : " + empName;
////                    Toast.makeText(MainActivity.this, show, Toast.LENGTH_LONG).show();
//                //                Toast.makeText(HomeActivity.this, "Update Success" + "\n" + itemsUpdated + "updated in product master", Toast.LENGTH_LONG).show();
////                showUpdateSuccessDialog(String.valueOf(itemsUpdated));
//
//            } else { // False, data did not save - connection time out
//                Log.d(TAG, "Inside GetItems Post Execute - False");
////                Toast.makeText(MainActivity.this, "No meetings for the day", Toast.LENGTH_LONG).show();
//                parseMeetingDetail();
//                dismissAlertDialog();
//                showToast(ResourceUtil.getResourceString(MainActivity.this, R.string.meeting_details_unavailable));
//            }
//        }
//    }
//
//    private void parseMeetingDetail() {
//        boolean found = false;
//        List<MeetingDetail> tempDetailList = conferenceHelper.getMeetingDetailListById(loadMeetingId);
//        if (tempDetailList != null && tempDetailList.size() > 0) {
//            for (MeetingDetail meetingDetail : tempDetailList) {
//                roomName = meetingDetail.roomName;
//                floorName = meetingDetail.floorName;
//                if (meetingDetail.isOrganizer.equals("true")) {
//                    found = true;
//                    setEmpName = meetingDetail.empName;
//                    break;
//                }
//            }
//        }
//
//        if (!found) {
//            setEmpName = conferenceHelper.getCreatedByNameFromHeader(loadMeetingId);
//        }
//
//        setMeetingDetailsView(conferenceHelper.getMeetingHeaderById(loadMeetingId));
////        setRoomFloorInfo();
//
//        cancelTimer();
//        startTimer(IDLE_TIME_IN_MILLIS);
//    }

//    private void setRoomFloorInfo() {
//        if (!TextUtils.isEmpty(roomName) || !TextUtils.isEmpty(floorName)) {
//
//            mTvRoomTitle.setText(this.getResources().getString(R.string.meeting_room_info, TextUtils.isEmpty(roomName) ? "--" : roomName.toUpperCase()));
////            mTvFloorTitle.setText(this.getResources().getString(R.string.meeting_floor_info, TextUtils.isEmpty(floorName) ? "--" : floorName.toUpperCase()));
//        } else {
//            mTvRoomTitle.setText("--");
////            mTvFloorTitle.setText("--");
//        }
//    }

    private void setDateView() {
        try {
            mTvCurrentDate.setText(DateUtil.getCurrentDateTwo());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setMeetingDetailsView(MeetingHeader passedDocumentHeader) {
//        fragment = adapter.getItem(viewPager.getCurrentItem());
        Log.d(TAG, "setMeetingDetailsView ");
        Log.d(TAG, "View pager - current : " + viewPager.getCurrentItem());
        fragment = adapter.getFragmentByPos(viewPager.getCurrentItem());
//        fragment = mFragmentList.get(viewPager.getCurrentItem());
        ((MeetingFragment) fragment).setMeetingDetails(passedDocumentHeader, passedDocumentHeader.createdByName);
    }

    private class AsyncTaskGetMeetingDay extends AsyncTask<String, Integer, JSONArray> {

        AsyncTaskGetMeetingDay() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgressDialogIndeterminate("Fetching Meeting Info...");
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            JSONArray jsonArray = null;
            try {
                URL u = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) u.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(120 * 1000);
                httpURLConnection.connect();
                int status = httpURLConnection.getResponseCode();
                switch (status) {
                    case HttpURLConnection.HTTP_OK: // Response OK
                        try {
                            // Parse incoming stream of data and add to list
                            BufferedReader streamReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                            StringBuilder responseStrBuilder = new StringBuilder();

                            String inputStr;
                            while ((inputStr = streamReader.readLine()) != null)
                                responseStrBuilder.append(inputStr);
                            jsonArray = new JSONArray(responseStrBuilder.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case HttpURLConnection.HTTP_BAD_REQUEST: //  Response 400
                        Log.d(TAG, "Bad Request");
                        // Send log to server
//                        logUtil.sendRemoteLog(FAILURE,
//                                GET_MEETING_DAY,
//                                String.valueOf(status),
//                                u.toString(),
//                                "");
//                        handler.sendEmptyMessage(2);
                        break;
                    case HttpURLConnection.HTTP_INTERNAL_ERROR: // Response 500
                        Log.d(TAG, "Internal Server Error");
                        // Send log to server
//                        logUtil.sendRemoteLog(FAILURE,
//                                GET_MEETING_DAY,
//                                String.valueOf(status),
//                                u.toString(),
//                                "");
//                        handler.sendEmptyMessage(3);
                        break;
                    case HttpURLConnection.HTTP_NOT_FOUND: // Response 404
                        Log.d(TAG, "Page Not Found");
                        // Send log to server
//                        logUtil.sendRemoteLog(FAILURE,
//                                GET_MEETING_DAY,
//                                String.valueOf(status),
//                                u.toString(),
//                                "");
//                        handler.sendEmptyMessage(4);
                        break;
                    default:
                        Log.d(TAG, "Status Code : " + status); //  Unhandled response code
                        // Send log to server
//                        logUtil.sendRemoteLog(FAILURE,
//                                GET_MEETING_DAY,
//                                String.valueOf(status),
//                                u.toString(),
//                                "");
//                        handler.sendEmptyMessage(5);
                }

            } catch (SocketTimeoutException se) {
//                handler.sendEmptyMessage(6);
                return null;
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (httpURLConnection != null) {
                    try {
                        httpURLConnection.disconnect();
                    } catch (Exception ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return jsonArray;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            // Dismiss any progress dialog
//            dismissProgressDialog();

            if (result != null && result.length() > 0) { // If true, show success message
                Log.d(TAG, "Inside GetItems Post Execute - True");
                List<MeetingHeader> parsedMeetingHeaderList;
//                looped = 0;
//                headerSize = 0;
                meetingHeaderList = new ArrayList<>();
                parsedMeetingHeaderList = conferenceHelper.parseMeetingHeaders(httpResponseParser.parseJSONObjectMeetingHeader(result));
                if (parsedMeetingHeaderList.size() > 0) {
                    conferenceHelper.saveMeetingHeader(parsedMeetingHeaderList);
                }

                meetingHeaderList.addAll(conferenceHelper.getMeetingHeaders());
                if (meetingHeaderList.size() > 0) {
                    mRlEmpty.setVisibility(View.GONE);
                    mTvEmpty.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
//                    headerSize = meetingHeaderList.size();
//                    for (MeetingHeader meetingHeader : meetingHeaderList) {
//                        requestMeetingDetail(meetingHeader.meetingID);
//                    }

                    Log.d(TAG, "Detail completed");

                    setupViewPager();

                } else {
                    viewPager.setVisibility(View.GONE);
                    mRlEmpty.setVisibility(View.VISIBLE);
                    mTvEmpty.setVisibility(View.VISIBLE);
                    mTvEmpty.setText(R.string.no_meetings);
                    roomName = "";
                    floorName = "";
//                    setRoomFloorInfo();
                }

//                setupViewPager(viewPager);
//                    String empEmail = result.getString("EmployeeEmail");
//                    String empNo = result.getString("EmployeeNo");
//                    String empName = result.getString("Name");
//                    String show = "Employee Email : " + empEmail + "\n" + "Employee No : " + empNo + "\n" + "Employee Name : " + empName;
//                    Toast.makeText(MainActivity.this, show, Toast.LENGTH_LONG).show();
                //                Toast.makeText(HomeActivity.this, "Update Success" + "\n" + itemsUpdated + "updated in product master", Toast.LENGTH_LONG).show();
//                showUpdateSuccessDialog(String.valueOf(itemsUpdated));

            } else { // False, data did not save - connection time out
                Log.d(TAG, "Inside GetItems Post Execute - False");
//                Toast.makeText(MainActivity.this, "No meetings for the day", Toast.LENGTH_LONG).show();
                viewPager.setVisibility(View.GONE);
                mRlEmpty.setVisibility(View.VISIBLE);
                mTvEmpty.setVisibility(View.VISIBLE);
                mTvEmpty.setText(R.string.no_meetings);
                roomName = "";
                floorName = "";
//                setRoomFloorInfo();
//                dismissAlertDialog();
//                showCustomDialog("No meetings for the day");
//                showToast("No meetings for the day");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVATION_REQUEST_ADMIN:
                if (resultCode == Activity.RESULT_OK) {
                    if (isKioskModeActive(MainActivity.this)) {
                        Log.d(TAG, "Device Owner: " + mDPM.isDeviceOwnerApp(getPackageName()));
                        if (mDPM.isDeviceOwnerApp(getPackageName()))
                            mDPM.setLockTaskPackages(mComponentName, new String[]{getPackageName()});
//                        startLockTask();
                    }
                }
                break;
        }
    }

    /* Handles the redirect from the System Browser */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        meetingApp.handleInteractiveRequestRedirect(requestCode, resultCode, data);
//    }

    private void showToast(int resId) {
        Toast.makeText(this, getResources().getString(resId), Toast.LENGTH_SHORT).show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialogIndeterminate(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void showCustomDialog(final String message) {
        alertDialog = new AlertDialog.Builder(this).setMessage(message).setTitle(R.string.title_alert_dialog).setNeutralButton(R.string.title_ok, (dialogInterface, i) -> dialogInterface.dismiss()).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void dismissAlertDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (checkSettings()) {
                dismissAlertDialog();
                requestMeeting();
            }
        }
    };

    private void startKioskService() { // ... and this method
        startService(new Intent(this, KioskService.class));
    }

    private void stopKioskService() {
        stopService(new Intent(this, KioskService.class));
    }

    private boolean isKioskModeActive(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(getResources().getString(R.string.pref_key_kiosk_mode), false);
    }

    public void preventStatusBarExpansion(Context context) {
        windowManager = ((WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        Activity activity = (Activity) context;
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                // this is to enable the notification to receive touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        //http://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels
        int resId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resId > 0) {
            result = activity.getResources().getDimensionPixelSize(resId);
        }

        localLayoutParams.height = result;

        localLayoutParams.format = PixelFormat.TRANSPARENT;

        view = new customViewGroup(context);

        windowManager.addView(view, localLayoutParams);
    }

    public class customViewGroup extends ViewGroup {

        public customViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            Log.v("customViewGroup", "**********Intercepted");
            return true;
        }
    }

    private void removeDisabledStatusView() {
        if (view != null) {
            windowManager.removeView(view);
            view = null;
        }
    }

    @Override
    public void onBackPressed() {
    }

    public class CustomRequest extends BaseRequest {

        public CustomRequest(final String requestUrl, final IBaseClient client, final java.util.List<Option> requestOptions) {
            super(requestUrl, client, requestOptions, Void.class);
        }

        public String get() throws ClientException {
            return send(HttpMethod.GET, null);
        }

//        public String get(ICallback<JSONObject> resultICallback) throws ClientException {
//            return send(HttpMethod.GET, resultICallback);
//        }
    }

    ICallback<JSONObject> resultICallback = new ICallback<JSONObject>() {
        @Override
        public void success(JSONObject result) {
            Log.d(TAG, "Response : " + result);
        }

        @Override
        public void failure(ClientException ex) {

        }
    };

    public IAuthenticationAdapter getAuthenticationAdapter() {

        return new MSAAuthAndroidAdapter(this.getApplication()) {
            @Override
            public String getClientId() {
//                return "f882194e-6340-49cb-be25-61ce4b9f4920";
                return Constants.APP_ID;
            }

            @Override
            public String[] getScopes() {
                return new String[]{
                        // An example set of scopes your application could use
                        "https://graph.microsoft.com/Calendars.Read",
                        "https://graph.microsoft.com/Calendars.Read.Shared",
                        "https://graph.microsoft.com/Calendars.ReadWrite",
                        "https://graph.microsoft.com/Calendars.ReadWrite.Shared",
                        "https://graph.microsoft.com/User.Read",
                        "https://graph.microsoft.com/User.ReadWrite",
                        "offline_access",
                        "openid"
                };
            }
        };
    }
}
