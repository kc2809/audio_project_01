package com.framgia.mixrecorder.utils;

import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by hoang on 1/12/2017.
 */
public class RecordHelper {
    private static final int FILE_BUFFER_SIZE = 1024;
    private static final String TEMPORARY = "/temporary";
    private String mTemporaryPath;
    private String mFilePath;
    private MediaRecorder mRecorder;
    private String mFileName;
    private String mFolderPath;

    public RecordHelper() {
        mFileName = getCurrentTime() + Constant.AUDIO_TYPE;
        mFolderPath = getAbsolutePath() + Constant.FOLDER;
        mFilePath = mFolderPath + mFileName;
        mTemporaryPath = getAbsolutePath() + TEMPORARY + Constant.AUDIO_TYPE;
    }

    private void append(String mainFileName, String anotherFileName) {
        try {
            File targetFile = new File(mainFileName);
            File anotherFile = new File(anotherFileName);
            if (targetFile.exists() && targetFile.length() > 0) {
                String tmpFileName = mainFileName + ".tmp";
                appendWithNewFile(mainFileName, anotherFileName, tmpFileName);
                copyFile(mainFileName, tmpFileName);
                anotherFile.delete();
            } else {
                targetFile.getParentFile().mkdirs();
                targetFile.createNewFile();
                copyFile(mainFileName, anotherFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendWithNewFile(final String firstFile, final String secondFile,
                                   final String newFile) throws IOException {
        final Movie movieA = MovieCreator.build(new FileDataSourceImpl(secondFile));
        final Movie movieB = MovieCreator.build(new FileDataSourceImpl(firstFile));
        final Movie finalMovie = new Movie();
        final List<Track> movieOneTracks = movieA.getTracks();
        final List<Track> movieTwoTracks = movieB.getTracks();
        for (int i = 0; i < movieOneTracks.size() || i < movieTwoTracks.size(); i++) {
            finalMovie.addTrack(new AppendTrack(movieTwoTracks.get(i), movieOneTracks.get(i)));
        }
        final Container container = new DefaultMp4Builder().build(finalMovie);
        final FileOutputStream fos = new FileOutputStream(new File(String.format(newFile)));
        final WritableByteChannel bb = Channels.newChannel(fos);
        container.writeContainer(bb);
        fos.close();
    }

    private void copyFile(String destination, String from) throws IOException {
        FileInputStream in = new FileInputStream(from);
        FileOutputStream out = new FileOutputStream(destination);
        byte[] buf = new byte[FILE_BUFFER_SIZE];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        new File(from).delete();
    }

    private String getCurrentTime() {
        return new SimpleDateFormat(Constant.TIME_FORMAT).format(Calendar.getInstance().getTime());
    }

    private String getAbsolutePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public void record(OnRecordListener onRecordListener) {
        new RecordAsyncTask().execute(onRecordListener);
    }

    public void stop(OnStopListener onStopListener) {
        new StopAsyncTask().execute(onStopListener);
    }

    public String getFileName() {
        return mFileName;
    }

    public String getFolderPath() {
        return mFolderPath;
    }

    public interface OnRecordListener {
        void onPreRecord();
        void onPostRecord();
    }

    public interface OnStopListener {
        void onPreStop();
        void onPostStop();
    }

    private class RecordAsyncTask extends AsyncTask<OnRecordListener, Void, Void> {
        private OnRecordListener mOnRecordListener;

        @Override
        protected Void doInBackground(OnRecordListener... onRecordListeners) {
            mOnRecordListener = onRecordListeners[0];
            onRecordListeners[0].onPreRecord();
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(mTemporaryPath);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            try {
                mRecorder.prepare();
                mRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mOnRecordListener.onPostRecord();
        }
    }

    private class StopAsyncTask extends AsyncTask<OnStopListener, Void, Void> {
        private OnStopListener mOnStopListener;

        @Override
        protected Void doInBackground(OnStopListener... onStopListeners) {
            mOnStopListener = onStopListeners[0];
            onStopListeners[0].onPreStop();
            try {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            append(mFilePath, mTemporaryPath);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mOnStopListener.onPostStop();
        }
    }
}
