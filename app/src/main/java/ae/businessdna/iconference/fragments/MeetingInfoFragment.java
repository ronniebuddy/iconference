package ae.businessdna.iconference.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ae.businessdna.iconference.R;
import ae.businessdna.iconference.activities.MainActivity;
import ae.businessdna.iconference.adapters.MeetingInfoAdapter;
import ae.businessdna.iconference.utility.Constants;
import ae.businessdna.iconference.utility.ResourceUtil;

import static ae.businessdna.iconference.utility.Constants.NIL;

/**
 * Project : iConference
 * Created by Rohith on 8/21/2017.
 */

public class MeetingInfoFragment extends DialogFragment {
    private String meetingDesc, organiserName, floorName, roomName;
    private List<String> attendeesNameList;
    private TextView mTvMeetingDesc, mTvOrganiser, mTvFloorName, mTvRoomName;
    private RecyclerView mRvAttendees;
    private MeetingInfoAdapter meetingInfoAdapter;
    private LinearLayoutManager mLlManager;
    MeetingInfoListener meetingInfoListener;
    MainActivity context;

    public interface MeetingInfoListener {
        public void hideNavigation();
    }

    public MeetingInfoFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (MainActivity) context;
        }
        try {
            meetingInfoListener = this.context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnUpdateOrderListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog_extra_info, container);
        mTvMeetingDesc = view.findViewById(R.id.tv_dialog_meeting_desc);
        mTvOrganiser = view.findViewById(R.id.tv_dialog_organiser_name);
//        mTvFloorName = view.findViewById(R.id.tv_dialog_floor_name);
        mTvRoomName = view.findViewById(R.id.tv_dialog_room_name);
        mRvAttendees = view.findViewById(R.id.rv_attendees);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent_dialog)));
        return view;
    }

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static public MeetingInfoFragment newInstance(String[] meetingHeader, ArrayList<String> attendees) {
        MeetingInfoFragment addProduct = new MeetingInfoFragment();
        Bundle args = new Bundle();
        if (meetingHeader != null && attendees != null) {
            args.putString("subject", meetingHeader[0]);
            args.putString("organiser", meetingHeader[1]);
            args.putString("meetingRoom", meetingHeader[2]);
            args.putString("meetingFloor", meetingHeader[3]);
            args.putStringArrayList("attendees", attendees);
            addProduct.setArguments(args);
            return addProduct;
        }
        return addProduct;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyCustomTheme);
        init();
        if (getArguments() != null) {
            meetingDesc = getArguments().getString("subject");
            organiserName = getArguments().getString("organiser");
//            roomName = getArguments().getString("meetingRoom");
            floorName = getArguments().getString("meetingFloor");
            attendeesNameList = getArguments().getStringArrayList("attendees");
        }
//        else {
//            attendeesNameList = new ArrayList<>();
//            attendeesNameList.add("Rohith");
//            attendeesNameList.add("Vinoth");
//            attendeesNameList.add("Chikku");
//            attendeesNameList.add("Kanna");
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // safety check
        if (getDialog() == null) {
            return;
        }
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.MyCustomTheme;
    }

    private void init() {
        mLlManager = new LinearLayoutManager(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setViews();
        final View decorView = getActivity().getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(
                i -> meetingInfoListener.hideNavigation());
    }

    private void setViews() {
        mTvMeetingDesc.setText(TextUtils.isEmpty(meetingDesc) ? NIL : meetingDesc.toUpperCase());
        TextViewCompat.setAutoSizeTextTypeWithDefaults(mTvMeetingDesc, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        mTvOrganiser.setText(TextUtils.isEmpty(organiserName) ?
                getActivity().getResources().getString(R.string.meeting_organiser_name, NIL) :
                getActivity().getResources().getString(R.string.meeting_organiser_name, organiserName.toUpperCase()));
        mTvRoomName.setText(TextUtils.isEmpty(Constants.ROOM_NAME) ?
                getActivity().getResources().getString(R.string.meeting_room_info, NIL) :
                getActivity().getResources().getString(R.string.meeting_room_info, Constants.ROOM_NAME.toUpperCase()));
//        mTvFloorName.setText(TextUtils.isEmpty(floorName) ?
//                getActivity().getResources().getString(R.string.meeting_floor_info, NIL) :
//                getActivity().getResources().getString(R.string.meeting_floor_info, floorName.toUpperCase()));

//        mTvFloorName.setText(getActivity().getResources().getString(R.string.meeting_floor_info, Constants.ROOM_NAME.toUpperCase()));

        if(attendeesNameList == null || attendeesNameList.size() == 0) {

            Toast.makeText(getActivity(), ResourceUtil.getResourceString(getActivity(), R.string.no_attendees), Toast.LENGTH_SHORT).show();
        } else {
            meetingInfoAdapter = new MeetingInfoAdapter(getActivity(), attendeesNameList);
            mRvAttendees.setLayoutManager(mLlManager);
            mRvAttendees.setAdapter(meetingInfoAdapter);
        }

    }
}
