package com.framgia.mixrecorder.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.framgia.mixrecorder.R;
import com.framgia.mixrecorder.utils.Constant;
import com.framgia.mixrecorder.utils.Counter;
import com.framgia.mixrecorder.utils.MediaStoreProcess;
import com.framgia.mixrecorder.utils.RecordHelper;

import java.io.File;

public class RecordFragment extends Fragment
    implements View.OnClickListener, DeleteDialogFragment.DeleteDialogListener,
    RenameDialogFragment.RenameDialogListener {
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
    private ImageView mImageRename;
    private ImageView mImageDelete;
    private ImageView mImagePlay;
    private OnRecordFragmentListener mListener;

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
        mImageRename = (ImageView) getView().findViewById(R.id.image_rename);
        mImageDelete = (ImageView) getView().findViewById(R.id.image_delete);
        mImagePlay = (ImageView) getView().findViewById(R.id.image_play);
        mButtonRecord.setOnClickListener(this);
        mButtonStop.setOnClickListener(this);
        mImageRename.setOnClickListener(this);
        mImageDelete.setOnClickListener(this);
        mImagePlay.setOnClickListener(this);
        mRlAudioModifier.setVisibility(View.GONE);
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
        MediaStoreProcess.addRecordingToMediaLibrary(getContext(), new File(mFolderPath +
            mFileName));
        mListener.onRecordFragmentChange();
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
            case R.id.button_record:
                processRecordEvent();
                break;
            case R.id.button_stop:
                processStopEvent();
                break;
            case R.id.image_rename:
                RenameDialogFragment renameDialogFragment = RenameDialogFragment.newInstance
                    (mFileName);
                renameDialogFragment.setTargetFragment(this, REQUEST_CODE);
                renameDialogFragment.show(mFragmentManager, Constant.RENAME_DIALOG_FRAGMENT);
                break;
            case R.id.image_delete:
                DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment();
                deleteDialogFragment.setTargetFragment(this, REQUEST_CODE);
                deleteDialogFragment.show(mFragmentManager, Constant.DELETE_DIALOG_FRAGMENT);
                break;
            case R.id.image_play:
                mListener.playAudio();
                break;
            default:
        }
    }

    @Override
    public void onDialogPositiveClick() {
        String path = mFolderPath + mFileName;
        if (MediaStoreProcess.deleteAudioFile(getContext(), path)) {
            mRlAudioModifier.setVisibility(View.GONE);
            mListener.onRecordFragmentChange();
            Toast.makeText(getContext(), R.string.msg_delete_success, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), R.string.msg_delete_fail, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDialogRenameClick(String newName) {
        String currentPath = mFolderPath + mFileName;
        if (MediaStoreProcess.renameAudioFile(getContext(), currentPath, newName)) {
            mFileName = newName;
            mTextAudioName.setText(mFileName);
            mListener.onRecordFragmentChange();
            Toast.makeText(getContext(), R.string.msg_rename_success, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), R.string.msg_rename_fail, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecordFragmentListener) {
            mListener = (OnRecordFragmentListener) context;
        } else {
            throw new RuntimeException(
                context.toString() + getString(R.string.error_implement_listener));
        }
    }

    public interface OnRecordFragmentListener {
        void onRecordFragmentChange();
        void playAudio();
    }
}
