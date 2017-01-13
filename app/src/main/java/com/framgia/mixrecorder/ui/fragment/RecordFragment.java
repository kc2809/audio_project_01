package com.framgia.mixrecorder.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.framgia.mixrecorder.R;
import com.framgia.mixrecorder.utils.Constant;
import com.framgia.mixrecorder.utils.Counter;
import com.framgia.mixrecorder.utils.RecordHelper;

public class RecordFragment extends Fragment implements View.OnClickListener {
    private Button mButtonRecord;
    private Button mButtonStop;
    private TextView mTextCounter;
    private boolean mIsRecording;
    private boolean mIsNewRecord = true;
    private boolean mIsResponsable = true;
    private RecordHelper mRecordHelper;
    private Counter mCounter;

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        mButtonRecord = (Button) getView().findViewById(R.id.btn_record);
        mButtonStop = (Button) getView().findViewById(R.id.btn_stop);
        mTextCounter = (TextView) getView().findViewById(R.id.text_counter);
        mButtonRecord.setOnClickListener(this);
        mButtonStop.setOnClickListener(this);
        mCounter = new Counter(getActivity(), Constant.COUNTER_TIME_FORMAT,
            new Counter.OnMillisencondListener() {
                @Override
                public void onMillisecond(String elapsedTime) {
                    mTextCounter.setText(elapsedTime);
                }
            });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        processStopEvent();
        mCounter.cancel();
    }

    private void processRecordEvent() {
        if (!mIsResponsable) return;
        if (mIsNewRecord) newAudio();
        if (!mIsRecording) record();
        else pause();
    }

    private void processStopEvent() {
        if (!mIsResponsable || mIsNewRecord) return;
        if (mIsRecording) pause();
        releaseAudio();
    }

    private void newAudio() {
        mRecordHelper = new RecordHelper();
        mCounter.start();
        mIsNewRecord = false;
        mTextCounter.setTextColor(getResources().getColor(R.color.color_red));
        mButtonStop.setBackgroundResource(R.drawable.ic_stop_active);
    }

    private void releaseAudio() {
        mRecordHelper = null;
        mCounter.reset();
        mIsNewRecord = true;
        mTextCounter.setTextColor(getResources().getColor(R.color.color_grey));
        mButtonStop.setBackgroundResource(R.drawable.ic_stop_inactive);
    }

    private void record() {
        mRecordHelper.record(new RecordHelper.OnRecordListener() {
            @Override
            public void onPreRecord() {
                mIsResponsable = false;
            }

            @Override
            public void onPostRecord() {
                mIsResponsable = true;
            }
        });
        mCounter.play();
        mIsRecording = true;
        mButtonRecord.setBackgroundResource(R.drawable.ic_pause_active);
    }

    private void pause() {
        mRecordHelper.stop(new RecordHelper.OnStopListener() {
            @Override
            public void onPreStop() {
                mIsResponsable = false;
            }

            @Override
            public void onPostStop() {
                mIsResponsable = true;
            }
        });
        mCounter.pause();
        mIsRecording = false;
        mButtonRecord.setBackgroundResource(R.drawable.ic_record_active);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_record:
                processRecordEvent();
                break;
            case R.id.btn_stop:
                processStopEvent();
                break;
            default:
        }
    }
}
