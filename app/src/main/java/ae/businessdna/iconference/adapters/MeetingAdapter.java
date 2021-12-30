package ae.businessdna.iconference.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Project : iConference
 * Created by rohith on 7/17/2017.
 */

public class MeetingAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "MeetingAdapter";
    private List<Fragment> mFragmentList;

    public MeetingAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList = new ArrayList<>();
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem - creating new fragment at pos :" + position);
        return getFragmentByPos(position);
    }

    public Fragment getFragmentByPos(int pos) {
        Log.d(TAG, "getFragmentByPos :" + pos);
        return mFragmentList.get(pos);
    }

    @Override
    public int getItemPosition(Object object) {

        int index = mFragmentList.indexOf(object);
        if (index == -1) {
            Log.d(TAG, "getItemPosition : " + index);
            return POSITION_NONE;
        } else {
            Log.d(TAG, "getItemPosition : " + index);
            return index;
        }
    }


}
