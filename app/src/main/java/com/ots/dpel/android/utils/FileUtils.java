package com.ots.dpel.android.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by tasos on 10/23/17.
 */

public class FileUtils {

    private static final String TAG = FileUtils.class.getName();

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getDownloadsStorageDir() {
        // Get the directory for the user's public pictures directory.
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //File dir = Environment.getExternalStorageDirectory() + "/";
        if (dir.exists()) {
            Log.e(TAG, "Directory " + dir.toString() + " exists");
        }
        if (!dir.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return dir;
    }

    /**
     * Extracts the version number from the apk filename. For instance, for filename dpekloges-1.10-release.apk will
     * return 1.10. It will also extract the version number from the BuildConfig.VERSION_NAME gradle param even in cases
     * where there is a string appended to it e.g "1.10-prselec" string.
     * @param updateFileName
     * @return the version number in String
     */
    public static String getUpdateFileVersion(String updateFileName) {
        int startPos = updateFileName.indexOf("-");
        if (startPos == -1) {
            return updateFileName;
        }
        int endPos = updateFileName.indexOf("-", startPos + 1);
        if (endPos == -1) {
            endPos = startPos;
            startPos = 0;
        } else {
            startPos += 1;
        }
        return updateFileName.substring(startPos, endPos);
    }

    /**
     * Checks two version strings for equality. If A > B return 1, if A < B return -1 else 0
     * @param versionA
     * @param versionB
     * @return 1 if versionA greater than versionB, -1 if versionA less than versionB and 0 if they are equal
     */
    public static int checkVersionEquality(String versionA, String versionB) {
        float a = Float.parseFloat(versionA);
        float b = Float.parseFloat(versionB);

        if (a < b) return -1;
        if (a > b) return 1;
        return 0;
    }

}
