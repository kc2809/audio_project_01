package com.framgia.mixrecorder.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.framgia.mixrecorder.R;
import com.framgia.mixrecorder.utils.RecordHelper;

public class RecordFragment extends Fragment implements View.OnClickListener {
    private Button mButtonRecord;
    private Button mButtonStop;
    private boolean mIsRecording;
    private boolean mIsNewRecord = true;
    private boolean mIsResponsable = true;
    private RecordHelper mRecordHelper;

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
        mButtonRecord.setOnClickListener(this);
        mButtonStop.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        processStopEvent();
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
        mIsNewRecord = false;
        mButtonStop.setBackgroundResource(R.drawable.ic_stop_active);
    }

    private void releaseAudio() {
        mRecordHelper = null;
        mIsNewRecord = true;
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
