package ae.businessdna.iconference.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;

import ae.businessdna.iconference.R;
import ae.businessdna.iconference.activities.MainActivity;
import ae.businessdna.iconference.bean.MeetingHeader;
import ae.businessdna.iconference.utility.Constants;
import ae.businessdna.iconference.utility.DateUtil;
import ae.businessdna.iconference.utility.ResourceUtil;

import static ae.businessdna.iconference.utility.Constants.EMPTY;
import static ae.businessdna.iconference.utility.Constants.NIL;

/**
 * Project : iConference
 * Created by rohith on 7/18/2017.
 */

public class MeetingFragment extends Fragment {
    TextView mTvStatus, mTvMeetingDesc, mTvEmpName, mTvVenue, mTvDuration, mTvEndsIn;
    MeetingHeader meetingHeader;
    //    int pageNo;
//    String createdByName;
    MainActivity context;
    MeetingLoadListener meetingLoadListener;
//    ConferenceHelper conferenceHelper;
//    private static final String PAGE_NO = "page_no";

    public interface MeetingLoadListener {
//        public void loadDetails(String meetingId);

        public void showToastMain(String message);
    }

    public MeetingFragment() {
    }

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
//    static public MeetingFragment newInstance(int position) {
//        MeetingFragment meetingFragment = new MeetingFragment();
//        Bundle args = new Bundle();
//            args.putInt(PAGE_NO, position);
//            meetingFragment.setArguments(args);
//        return meetingFragment;
//    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (MainActivity) context;
        }
        try {
            meetingLoadListener = this.context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnUpdateOrderListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        createdByName = getArguments().getString("CREATED_BY");
        meetingHeader = getArguments().getParcelable("DETAIL");
//        conferenceHelper = new ConferenceHelper(getActivity());
//        pageNo = getArguments().getInt(PAGE_NO);
//        meetingHeader = conferenceHelper.getMeetingHeaders().get(pageNo);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meeting_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvStatus = view.findViewById(R.id.tv_status);
        mTvMeetingDesc = view.findViewById(R.id.tv_meeting_desc);
        mTvEmpName = view.findViewById(R.id.tv_emp_name);
        mTvVenue = view.findViewById(R.id.tv_venue);
        mTvDuration = view.findViewById(R.id.tv_duration);
        mTvEndsIn = view.findViewById(R.id.tv_ends_in);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (meetingHeader != null) {
            if (TextUtils.isEmpty(meetingHeader.meetingID)) {
                meetingLoadListener.showToastMain("Meeting ID empty.\nUnable to fetch meeting details");
            } else {
//                meetingLoadListener.loadDetails(meetingHeader.meetingID);
            }

//            try {
//                mTvEndsIn.setText("Ends in " +DateUtil.getTimeDifference(DateUtil.getCurrentDate() + " " +meetingHeader.endTime, DateUtil.getCurrentDate() + " " + meetingHeader.startTime) + " min");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

        }
    }

    public void setMeetingDetails(MeetingHeader meetingHeader, String empName) {
        boolean meetingStarted = false;
        try {
            if(!meetingHeader.isAllDay) {
                meetingStarted = DateUtil.validateFinishedMeeting(meetingHeader.startTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (meetingHeader.isAllDay || meetingStarted) {
//            mTvStatus.setText(ResourceUtil.getResourceString(getActivity(), R.string.in_progress));
            mTvStatus.setText("IN PROGRESS");
        } else {
//            mTvStatus.setText(ResourceUtil.getResourceString(getActivity(), R.string.upcoming));
            mTvStatus.setText("UPCOMING");
        }
        if(TextUtils.isEmpty(meetingHeader.subject)) {
            mTvMeetingDesc.setText(NIL);
        } else {
            mTvMeetingDesc.setPaintFlags(mTvMeetingDesc.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            mTvMeetingDesc.setText(TextUtils.isEmpty(meetingHeader.subject) ? NIL : meetingHeader.subject.toUpperCase());
            TextViewCompat.setAutoSizeTextTypeWithDefaults(mTvMeetingDesc, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }

        if (!TextUtils.isEmpty(empName)) {
            mTvEmpName.setText(empName);
        } else {
            mTvEmpName.setText(NIL);
        }

        if (!TextUtils.isEmpty(Constants.ROOM_NAME)) {
            mTvVenue.setText(Constants.ROOM_NAME);
//            TextViewCompat.setAutoSizeTextTypeWithDefaults(mTvVenue, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        } else {
            mTvVenue.setText(NIL);
        }

        try {
            if(meetingHeader.isAllDay) {
                mTvDuration.setText(ResourceUtil.getResourceString(getActivity(), R.string.meeting_booked_all_day));
//                mTvDuration.setText("Booked : All Day");
            } else {
//                mTvDuration.setText("Booked " + DateUtil.parseMeetingTime(meetingHeader.startTime) +
//                        " - " + DateUtil.parseMeetingTime(meetingHeader.endTime));
                mTvDuration.setText(getActivity().getResources().getString(R.string.meeting_booked,
                        !DateUtil.validateCurrentMeetingTime(meetingHeader.startTime) ? DateUtil.getCurrentDateTwo(meetingHeader.startTime) + "\n" : "",
                        DateUtil.parseMeetingTime(meetingHeader.startTime),
                        DateUtil.parseMeetingTime(meetingHeader.endTime)));
//                TextViewCompat.setAutoSizeTextTypeWithDefaults(mTvDuration, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
//            if(meetingHeader.isCancelled) {
//                mTvEndsIn.setText(ResourceUtil.getResourceString(getActivity(), R.string.meeting_canceled));
//            }
            if (meetingStarted) {
                mTvEndsIn.setText(getActivity().getResources().getString(R.string.meeting_ends, DateUtil.getTimeDifference(meetingHeader.endTime)));
            } else {
                mTvEndsIn.setVisibility(View.GONE);
//                mTvEndsIn.setText(EMPTY);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
