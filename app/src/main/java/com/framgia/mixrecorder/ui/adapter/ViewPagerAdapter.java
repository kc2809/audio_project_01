package com.framgia.mixrecorder.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.framgia.mixrecorder.R;
import com.framgia.mixrecorder.ui.fragment.RecordFragment;
import com.framgia.mixrecorder.ui.fragment.RecordingsFragment;

/**
 * Created by GIAKHANH on 1/10/2017.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public static final int TAB_COUNT = 2;
    public static final int FRAGMENT_RECORD = 0;
    public static final int FRAGMENT_RECORDINGS = 1;
    private Context mContext;
    private RecordFragment mRecordFragment;
    private RecordingsFragment mRecordingsFragment;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        mRecordFragment = new RecordFragment();
        mRecordingsFragment = new RecordingsFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FRAGMENT_RECORD:
                return mRecordFragment;
            case FRAGMENT_RECORDINGS:
                return mRecordingsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case FRAGMENT_RECORD:
                return mContext.getString(R.string.tab_record);
            case FRAGMENT_RECORDINGS:
                return mContext.getString(R.string.tab_recordings);
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
