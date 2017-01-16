package com.framgia.mixrecorder.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.framgia.mixrecorder.R;
import com.framgia.mixrecorder.utils.Constant;
import com.framgia.mixrecorder.utils.Counter;
import com.framgia.mixrecorder.utils.RecordHelper;

import java.io.File;

public class RecordFragment extends Fragment
    implements View.OnClickListener, DeleteDialogFragment.DeleteDialogListener,
    RenameDialogFragment.RenameDialogListener {
    private static final String DELETE_DIALOG_FRAGMENT = "DeleteDialogFragment";
    private static final String RENAME_DIALOG_FRAGMENT = "RenameDialogFragment";
    private static final int REQUEST_CODE = 1;
    private FragmentManager mFragmentManager;
    private String mFileName;
    private String mFolderPath;
    private Button mButtonRecord;
    private Button mButtonStop;
    private TextView mTextCounter;
    private boolean mIsRecording;
    private boolean mIsNewRecord = true;
    private boolean mIsResponsable = true;
    private RecordHelper mRecordHelper;
    private Counter mCounter;
    private RelativeLayout mRlAudioModifier;
    private TextView mTextAudioName;
    private Button mButtonRename;
    private Button mButtonDelete;
    private Button mButtonCrop;

    public RecordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        mFragmentManager = getFragmentManager();
        initViews();
        mCounter = new Counter(getActivity(), Constant.COUNTER_TIME_FORMAT,
            new Counter.OnMillisencondListener() {
                @Override
                public void onMillisecond(String elapsedTime) {
                    mTextCounter.setText(elapsedTime);
                }
            });
        mCounter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        processStopEvent();
        mCounter.cancel();
    }

    private void initViews() {
        mButtonRecord = (Button) getView().findViewById(R.id.button_record);
        mButtonStop = (Button) getView().findViewById(R.id.button_stop);
        mTextCounter = (TextView) getView().findViewById(R.id.text_counter);
        mRlAudioModifier = (RelativeLayout) getView().findViewById(R.id.relative_audio_modifier);
        mTextAudioName = (TextView) getView().findViewById(R.id.text_audio);
        mButtonRename = (Button) getView().findViewById(R.id.button_rename);
        mButtonDelete = (Button) getView().findViewById(R.id.button_delete);
        mButtonCrop = (Button) getView().findViewById(R.id.button_crop);
        mButtonRecord.setOnClickListener(this);
        mButtonStop.setOnClickListener(this);
        mButtonRename.setOnClickListener(this);
        mButtonDelete.setOnClickListener(this);
        mButtonCrop.setOnClickListener(this);
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
        mRlAudioModifier.setVisibility(View.GONE);
        mTextCounter.setTextColor(getResources().getColor(R.color.color_red));
        mButtonStop.setBackgroundResource(R.drawable.ic_stop_active);
    }

    private void releaseAudio() {
        mFileName = mRecordHelper.getFileName();
        mFolderPath = mRecordHelper.getFolderPath();
        mTextAudioName.setText(mFileName);
        mRecordHelper = null;
        mCounter.reset();
        mIsNewRecord = true;
        mRlAudioModifier.setVisibility(View.VISIBLE);
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

    private void showToast(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_record:
                processRecordEvent();
                break;
            case R.id.button_stop:
                processStopEvent();
                break;
            case R.id.button_rename:
                RenameDialogFragment renameDialogFragment = RenameDialogFragment.newInstance
                    (mFileName);
                renameDialogFragment.setTargetFragment(this, REQUEST_CODE);
                renameDialogFragment.show(mFragmentManager, RENAME_DIALOG_FRAGMENT);
                break;
            case R.id.button_delete:
                DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment();
                deleteDialogFragment.setTargetFragment(this, REQUEST_CODE);
                deleteDialogFragment.show(mFragmentManager, DELETE_DIALOG_FRAGMENT);
                break;
            case R.id.button_crop:
                // TODO crop function
                break;
            default:
        }
    }

    @Override
    public void onDialogPositiveClick() {
        File file = new File(mFolderPath + mFileName);
        if (file.exists() && file.delete()) {
            mRlAudioModifier.setVisibility(View.GONE);
            showToast(R.string.msg_delete_success);
        } else showToast(R.string.msg_delete_fail);
    }

    @Override
    public void onDialogRenameClick(String newName) {
        File from = new File(mFolderPath + mFileName);
        File to = new File(mFolderPath + newName);
        if (from.renameTo(to)) {
            mFileName = newName;
            mTextAudioName.setText(mFileName);
            showToast(R.string.msg_rename_success);
        } else showToast(R.string.msg_rename_fail);
    }
}
