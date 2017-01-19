package com.framgia.mixrecorder.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.framgia.mixrecorder.R;

import java.io.File;

/**
 * Created by GIAKHANH on 1/18/2017.
 */
public class MediaStoreProcess {
    private static final String MIME_TYPE_3GPP = "audio/3gpp";

    public static void addRecordingToMediaLibrary(Context context, File outFile) {
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Audio.Media.TITLE, outFile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, current);
        values.put(MediaStore.Audio.Media.MIME_TYPE, MIME_TYPE_3GPP);
        values.put(MediaStore.Audio.Media.DATA, outFile.getAbsolutePath());
        ContentResolver contentResolver = context.getContentResolver();
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        Toast.makeText(context, newUri + context.getString(R.string.msg_file_saved),
            Toast.LENGTH_LONG).show();
    }

    public static void renameRecordingFromMediaLibrary(Context context, String currentpath,
                                                       String newName, String newPath) {
        ContentValues values = new ContentValues(2);
        values.put(MediaStore.Audio.Media.TITLE, newName);
        values.put(MediaStore.Audio.Media.DATA, newPath);
        context.getContentResolver().update(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values,
            String.format("%s = '%s'", MediaStore.Audio.Media.DATA, currentpath), null);
    }

    public static void deleteRecordingFromMediaLibrary(Context context, String path) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        context.getContentResolver()
            .delete(uri, String.format("%s = '%s'", MediaStore.Audio.Media.DATA, path),
                null);
    }

    public static boolean renameAudioFile(Context context, String currentPath, String newName) {
        String newPath = currentPath.substring(0, currentPath.lastIndexOf("/") + 1) + newName;
        File sourceFile = new File(currentPath);
        File destinationFile = new File(newPath);
        if (sourceFile.exists() && sourceFile.renameTo(destinationFile)) {
            MediaStoreProcess.renameRecordingFromMediaLibrary(context, currentPath, newName,
                newPath);
            return true;
        }
        return false;
    }

    public static boolean deleteAudioFile(Context context, String path) {
        File file = new File(path);
        if (file.exists() && file.delete()) {
            deleteRecordingFromMediaLibrary(context, path);
            return true;
        }
        return false;
    }
}
