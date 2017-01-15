package com.framgia.mixrecorder.ui.fragment;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.framgia.mixrecorder.R;
import com.framgia.mixrecorder.data.model.Song;
import com.framgia.mixrecorder.utils.Constant;
import com.framgia.mixrecorder.utils.Convert;

import java.io.FileInputStream;
import java.io.IOException;

public class PlayerFragment extends Fragment implements View.OnClickListener,
    SeekBar.OnSeekBarChangeListener {
    private OnPlayerFragInteractListener mListener;
    private TextView mTvAudioName;
    private TextView mTvTime;
    private ImageButton mBtnToggle;
    private ImageButton mBtnClose;
    private SeekBar mSeekBar;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private Song mSong;
    private Runnable mUpdateSeekBarTask = new Runnable() {
        @Override
        public void run() {
            if (mMediaPlayer == null || !mMediaPlayer.isPlaying()) return;
            mTvTime.setText(Convert.convertTime(mMediaPlayer.getCurrentPosition()) + "/" +
                Convert.convertTime(mMediaPlayer.getDuration()));
            mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
            mHandler.postDelayed(this, Constant.DELAY_TIME);
        }
    };

    public PlayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        mTvAudioName = (TextView) getView().findViewById(R.id.text_audio_name);
        mTvTime = (TextView) getView().findViewById(R.id.text_time);
        mBtnToggle = (ImageButton) getView().findViewById(R.id.button_toggle);
        mBtnClose = (ImageButton) getView().findViewById(R.id.button_close);
        mSeekBar = (SeekBar) getView().findViewById(R.id.seekbar);
        mBtnToggle.setOnClickListener(this);
        mBtnClose.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void playAudio(Song song) {
        mSong = song;
        releaseMediaPlayer();
        setupMediaPlayer(song);
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void setupMediaPlayer(Song song) {
        mMediaPlayer = new MediaPlayer();
        try {
            FileInputStream ips = new FileInputStream(song.getPath());
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(ips.getFD());
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    setupSeekBar();
                    updateSeekBar();
                    mMediaPlayer.start();
                    mBtnToggle.setImageResource(R.drawable.ic_pause_audio);
                }
            });
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupSeekBar() {
        mSeekBar.setMax(mMediaPlayer.getDuration());
        mSeekBar.setProgress(0);
        mTvAudioName.setText(mSong.getName());
    }

    private void updateSeekBar() {
        mHandler.postDelayed(mUpdateSeekBarTask, Constant.DELAY_TIME);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPlayerFragInteractListener) {
            mListener = (OnPlayerFragInteractListener) context;
        } else {
            throw new RuntimeException(
                context.toString() + getString(R.string.error_implement_listener));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_toggle:
                toggle();
                break;
            case R.id.button_close:
                close();
                break;
            default:
                break;
        }
    }

    private void toggle() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mBtnToggle.setImageResource(R.drawable.ic_play_audio);
        } else {
            mMediaPlayer.start();
            mBtnToggle.setImageResource(R.drawable.ic_pause_audio);
            updateSeekBar();
        }
    }

    private void close() {
        releaseMediaPlayer();
        mListener.onPlayerFragInteract();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mMediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public interface OnPlayerFragInteractListener {
        void onPlayerFragInteract();
    }
}